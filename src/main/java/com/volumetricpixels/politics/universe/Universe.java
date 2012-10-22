/*
 * This file is part of Politics.
 *
 * Copyright (c) 2012-2012, VolumetricPixels <http://volumetricpixels.com/>
 * Politics is licensed under the Affero General Public License Version 3.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.volumetricpixels.politics.universe;

import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TLongObjectHashMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.spout.api.Server;
import org.spout.api.Spout;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;
import org.bson.types.BasicBSONList;

import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.PoliticsPlugin;
import com.volumetricpixels.politics.data.Storable;
import com.volumetricpixels.politics.group.Citizen;
import com.volumetricpixels.politics.group.Group;
import com.volumetricpixels.politics.group.level.GroupLevel;
import com.volumetricpixels.politics.world.PoliticsWorld;

/**
 * Represents a headless group of all groups within its scope.
 */
public class Universe implements Storable {
    /**
     * The name of the universe.
     */
    private final String name;

    /**
     * Contains the rules of this universe.
     */
    private final UniverseRules rules;

    /**
     * Contains the worlds in which this universe is part of.
     */
    private List<PoliticsWorld> worlds;

    /**
     * The groups in this universe manager.
     */
    private List<Group> groups;

    /**
     * Contains the immediate children of each group.
     */
    private Map<Group, Set<Group>> children;

    /**
     * Groups in the given levels.
     */
    private Map<GroupLevel, List<Group>> levels;

    /**
     * Cache containing citizens.
     */
    private LoadingCache<String, Set<Group>> citizenGroupCache;

    /**
     * C'tor
     * 
     * @param name
     * @param properties
     */
    public Universe(String name, UniverseRules properties) {
        this(name, properties, new ArrayList<PoliticsWorld>(), new ArrayList<Group>(), new HashMap<Group, Set<Group>>());
    }

    /**
     * C'tor
     * 
     * @param name
     * @param properties
     * @param worlds
     * @param groups
     * @param children
     */
    public Universe(String name, UniverseRules properties, List<PoliticsWorld> worlds, List<Group> groups, Map<Group, Set<Group>> children) {
        this.name = name;
        this.rules = properties;
        this.worlds = worlds;
        this.groups = groups;
        this.children = children;

        buildCitizenCache();

        levels = new HashMap<GroupLevel, List<Group>>();
        for (Group group : groups) {
            getInternalGroups(group.getLevel()).add(group);
        }
    }

    /**
     * Builds the citizen cache.
     */
    private void buildCitizenCache() {
        // Build cache
        CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder();

        builder.maximumSize(((Server) Spout.getEngine()).getMaxPlayers());
        builder.expireAfterAccess(10L, TimeUnit.MINUTES);

        citizenGroupCache = builder.build(new CacheLoader<String, Set<Group>>() {
            @Override
            public Set<Group> load(String name) throws Exception {
                Set<Group> myGroups = new HashSet<Group>();
                for (Group group : groups) {
                    if (group.isImmediateMember(name)) {
                        myGroups.add(group);
                    }
                }
                return myGroups;
            }
        });
    }

    /**
     * Gets the name of this Universe.
     * 
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the rules of this universe.
     * 
     * @return
     */
    public UniverseRules getRules() {
        return rules;
    }

    /**
     * Gets a list of all groups in the universe.
     * 
     * @return
     */
    public List<Group> getGroups() {
        return new ArrayList<Group>(groups);
    }

    /**
     * Gets all groups with the given property.
     * 
     * @param property
     * @param value
     * @return
     */
    public List<Group> getGroupsByProperty(int property, Object value) {
        List<Group> groups = new ArrayList<Group>();
        for (Group group : getGroups()) {
            if (group.getProperty(property).equals(value)) {
                groups.add(group);
            }
        }
        return groups;
    }

    /**
     * Gets all groups of a certain level with the given property.
     * 
     * @param level
     * @param property
     * @param value
     * @return
     */
    public List<Group> getGroupsByProperty(GroupLevel level, int property, Object value) {
        List<Group> groups = new ArrayList<Group>();
        for (Group group : getGroups(level)) {
            if (group.getProperty(property).equals(value)) {
                groups.add(group);
            }
        }
        return groups;
    }

    /**
     * Gets the first group found with the given property.
     * 
     * @param property
     * @param value
     * @return
     */
    public Group getFirstGroupByProperty(int property, Object value) {
        for (Group group : getGroups()) {
            if (group.getProperty(property).equals(value)) {
                return group;
            }
        }
        return null;
    }

    /**
     * Gets the first group found of a certain level with the given property.
     * 
     * @param level
     * @param property
     * @param value
     * @return
     */
    public Group getFirstGroupByProperty(GroupLevel level, int property, Object value) {
        for (Group group : getGroups(level)) {
            if (group.getProperty(property).equals(value)) {
                return group;
            }
        }
        return null;
    }

    /**
     * Adds the given PoliticsWorld to this Universe.
     * 
     * @param world
     * @return True if the add was successful
     */
    public boolean addWorld(PoliticsWorld world) {
        List<GroupLevel> levels = rules.getGroupLevels();
        // Check if the rules are already there
        for (GroupLevel level : world.getLevels()) {
            if (levels.contains(level)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets a list of all worlds this universe is part of.
     * 
     * @return
     */
    public List<PoliticsWorld> getWorlds() {
        return new ArrayList<PoliticsWorld>(worlds);
    }

    /**
     * Gets a list of all groups with the given level in this universe.
     * 
     * @param level
     * @return
     */
    public List<Group> getGroups(GroupLevel level) {
        return new ArrayList<Group>(getInternalGroups(level));
    }

    /**
     * Gets the internal groups corresponding with the given level.
     * 
     * @param level
     * @return
     */
    private List<Group> getInternalGroups(GroupLevel level) {
        List<Group> levelGroups = levels.get(level);
        if (levelGroups == null) {
            levelGroups = new ArrayList<Group>();
            levels.put(level, levelGroups);
        }
        return levelGroups;
    }

    /**
     * Gets the child groups of the given group.
     * 
     * @param group
     * @return
     */
    public Set<Group> getChildGroups(Group group) {
        return new HashSet<Group>(getInternalChildGroups(group));
    }

    /**
     * Gets the internal child groups of the given group.
     * 
     * @param group
     * @return
     */
    private Set<Group> getInternalChildGroups(Group group) {
        if (group == null) {
            return new HashSet<Group>();
        }
        Set<Group> childs = children.get(group);
        if (childs == null) {
            return new HashSet<Group>();
        }
        return childs;
    }

    /**
     * Adds the given child as a child for the given group.
     * 
     * @param group
     * @param child
     * @return True if the group could be made a child
     */
    public boolean addChildGroup(Group group, Group child) {
        if (!group.getLevel().canBeChild(child.getLevel())) {
            return false;
        }

        Set<Group> childs = children.get(group);
        if (childs == null) {
            childs = new HashSet<Group>();
        }
        childs.add(child);
        return true;
    }

    /**
     * Removes the given child group from the children of the given group.
     * 
     * @param group
     * @param child
     * @return True if the child was removed, false if the child was not a child
     *         in the first place
     */
    public boolean removeChildGroup(Group group, Group child) {
        Set<Group> childs = children.get(group);
        if (childs == null) {
            return false;
        }
        return childs.remove(child);
    }

    /**
     * Creates a new group with the given level.
     * 
     * @param level
     * @return
     */
    public Group createGroup(GroupLevel level) {
        Group group = new Group(Politics.getUniverseManager().nextId(), level);

        groups.add(group);
        getInternalGroups(level).add(group);

        return group;
    }

    /**
     * Destroys the given group and removes it from memory.
     * 
     * @param group
     */
    public void destroyGroup(Group group) {
        destroyGroup(group, false);
    }

    /**
     * Destroys the given group and removes it from memory.
     * 
     * @param group
     *            The group to destroy
     * @param deep
     *            True if child groups should be deleted
     */
    public void destroyGroup(Group group, boolean deep) {
        groups.remove(group);
        getInternalGroups(group.getLevel()).remove(group);
        for (String member : group.getPlayers()) {
            invalidateCitizenGroups(member);
        }
        if (deep) {
            for (Group child : group.getGroups()) {
                destroyGroup(child, true);
            }
        }

        children.remove(group);
        // This can be expensive -- beware!
        for (Set<Group> childrenOfAGroup : children.values()) {
            if (childrenOfAGroup.contains(group)) {
                childrenOfAGroup.remove(group);
            }
        }
    }

    /**
     * Gets the citizen corresponding with the given player name.
     * 
     * @param player
     *            The player name, case-sensitive.
     * @return
     */
    public Citizen getCitizen(String player) {
        return new Citizen(player, this);
    }

    /**
     * Gets the groups of the given citizen.
     * 
     * @param player
     * @return
     */
    public Set<Group> getCitizenGroups(String player) {
        try {
            return new HashSet<Group>(citizenGroupCache.get(player));
        } catch (ExecutionException e) {
            PoliticsPlugin.logger().log(Level.SEVERE, "Could not load a set of citizen groups! This is a PROBLEM!", e);
            return null;
        }
    }

    /**
     * Invalidates the given Set of groups for the given citizen.
     * 
     * @param citizen
     */
    public void invalidateCitizenGroups(String citizen) {
        citizenGroupCache.invalidate(citizen);
    }

    @Override
    public BasicBSONObject toBSONObject() {
        BasicBSONObject bson = new BasicBSONObject();

        bson.put("name", name);
        bson.put("rules", rules.getName());

        BasicBSONList groupsBson = new BasicBSONList();
        BasicBSONObject childrenBson = new BasicBSONObject();

        for (Group group : groups) {
            // groups
            groupsBson.add(group.toBSONObject());

            // children
            BasicBSONList children = new BasicBSONList();
            for (Group child : group.getGroups()) {
                children.add(child.getUid());
            }
            childrenBson.put(Long.toHexString(group.getUid()), children);
        }
        bson.put("groups", groupsBson);
        bson.put("children", childrenBson);

        return bson;
    }

    /**
     * Converts the given bson object into a new Universe.
     * 
     * @param object
     * @return
     */
    public static Universe fromBSONObject(BSONObject object) {
        if (!(object instanceof BasicBSONObject)) {
            throw new IllegalStateException("object is not a BasicBsonObject! ERROR ERROR ERROR!");
        }
        BasicBSONObject bobject = (BasicBSONObject) object;

        String aname = bobject.getString("name");
        String rulesName = bobject.getString("rules");
        UniverseRules rules = Politics.getUniverseManager().getRules(rulesName);

        if (rules == null) {
            throw new IllegalStateException("Rules do not exist!");
        }

        List<PoliticsWorld> worlds = new ArrayList<PoliticsWorld>();
        Object worldsObj = bobject.get("worlds");
        if (!(worldsObj instanceof BasicBSONList)) {
            throw new IllegalStateException("GroupWorlds object is not a list!!! ASDFASDF");
        }
        BasicBSONList worldsBson = (BasicBSONList) worldsObj;
        for (Object worldName : worldsBson) {
            String name = worldName.toString();
            PoliticsWorld world = Politics.getPlotManager().getWorld(name);
            if (world == null) {
                PoliticsPlugin.logger().log(Level.WARNING, "GroupWorld `" + name + "' could not be found! (Did you delete it?)");
            } else {
                worlds.add(world);
            }
        }

        Object groupsObj = bobject.get("groups");
        if (!(groupsObj instanceof BasicBSONList)) {
            throw new IllegalStateException("groups isn't a list?! wtfhax?");
        }
        BasicBSONList groupsBson = (BasicBSONList) groupsObj;

        TLongObjectMap<Group> groups = new TLongObjectHashMap<Group>();
        for (Object groupBson : groupsBson) {
            if (!(groupBson instanceof BasicBSONObject)) {
                throw new IllegalStateException("Invalid group!");
            }
            Group c = Group.fromBSONObject(rules, (BasicBSONObject) groupBson);
            groups.put(c.getUid(), c);
        }

        Map<Group, Set<Group>> children = new HashMap<Group, Set<Group>>();
        Object childrenObj = bobject.get("children");
        if (!(childrenObj instanceof BasicBSONObject)) {
            throw new IllegalStateException("Missing children report!");
        }
        BasicBSONObject childrenBson = (BasicBSONObject) childrenObj;
        for (Entry<String, Object> childEntry : childrenBson.entrySet()) {
            String groupId = childEntry.getKey();
            long uid = Long.parseLong(groupId, 16);
            Group c = groups.get(uid);
            if (c == null) {
                throw new IllegalStateException("Unknown group id " + Long.toHexString(uid));
            }

            Object childsObj = childEntry.getValue();
            if (!(childsObj instanceof BasicBSONList)) {
                throw new IllegalStateException("No bson list found for childsObj");
            }
            Set<Group> childrenn = new HashSet<Group>();
            BasicBSONList childs = (BasicBSONList) childsObj;
            for (Object childN : childs) {
                long theuid = (Long) childN;
                Group ch = groups.get(theuid);
                childrenn.add(ch);
            }
            children.put(c, childrenn);
        }

        List<Group> groupz = new ArrayList<Group>(groups.valueCollection());
        Universe universe = new Universe(aname, rules, worlds, groupz, children);
        for (Group group : groupz) {
            group.initialize(universe);
        }
        return universe;
    }
}
