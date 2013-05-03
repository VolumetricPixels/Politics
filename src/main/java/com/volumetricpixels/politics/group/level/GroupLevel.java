/*
 * This file is part of Politics.
 * 
 * Copyright (c) 2012-2012, VolumetricPixels <http://volumetricpixels.com/>
 * Politics is licensed under the Affero General Public License Version 3.
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.volumetricpixels.politics.group.level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;

import org.spout.api.util.config.ConfigurationNode;

import com.volumetricpixels.politics.PoliticsPlugin;
import com.volumetricpixels.politics.group.privilege.Privilege;

/**
 * Represents a level of organization of a group
 */
public final class GroupLevel {
    private static final String DEFAULT_TRACK = "default";

    /**
     * The ID of the GroupLevel.
     */
    private final String id;
    /**
     * The name of the GroupLevel.
     */
    private final String name;
    /**
     * The rank of this GroupLevel. Smaller means lower. For example, in Towny,
     * a Nation would have a higher rank than a Town.
     */
    private final int rank;
    /**
     * Contains the allowed children for this GroupLevel.
     */
    private Set<GroupLevel> allowedChildren;
    /**
     * Roles mapped to their ids.
     */
    private final Map<String, Role> roles;
    /**
     * Plural form of this group level name.
     */
    private final String plural;
    /**
     * The commands allowed in the UniverseRules along with their aliases.
     */
    private final Map<String, List<String>> commands;
    /**
     * The tracks of the group level.
     */
    private final Map<String, RoleTrack> tracks;
    /**
     * The initial role of the group level.
     */
    private final Role initial;
    /**
     * The founding role of the group level.
     */
    private final Role founder;

    /**
     * C'tor
     * 
     * There must be a better way... if final doesn't really matter then I will
     * switch from it.
     * 
     * @param id
     * @param name
     * @param rank
     * @param roles
     * @param plural
     * @param commands
     * @param tracks
     * @param initial
     * @param founder
     */
    public GroupLevel(final String id, final String name, final int rank, final Map<String, Role> roles, final String plural,
            final Map<String, List<String>> commands, final Map<String, RoleTrack> tracks, final Role initial, final Role founder) {
        this.id = id;
        this.name = name;
        this.rank = rank;
        this.roles = roles;
        this.plural = plural;
        this.commands = commands;
        this.tracks = tracks;
        this.initial = initial;
        this.founder = founder;
    }

    /**
     * Sets the allowed children to the given set.
     * 
     * @param set
     *            The set to use. This should be the only reference.
     */
    public void setAllowedChildren(final Set<GroupLevel> set) {
        allowedChildren = set;
    }

    /**
     * Gets the ID of this GroupLevel.
     * 
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the name of this GroupLevel.
     * 
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the rank of this GroupLevel.
     * 
     * @return
     */
    public int getRank() {
        return rank;
    }

    /**
     * Gets the plural form of this GroupLevel.
     * 
     * @return
     */
    public String getPlural() {
        return plural;
    }

    /**
     * Gets the set of allowed children of this GroupLevel.
     * 
     * @return
     */
    public Set<GroupLevel> getAllowedChildren() {
        return new HashSet<GroupLevel>(allowedChildren);
    }

    /**
     * Returns true if this level can have children of the given level.
     * 
     * @param level
     * @return
     */
    public boolean canBeChild(final GroupLevel level) {
        return allowedChildren.contains(level);
    }

    /**
     * Gets the roles of the GroupLevel, named.
     * 
     * @return
     */
    public Map<String, Role> getRoles() {
        return new HashMap<String, Role>(roles);
    }

    /**
     * Gets the aliases of the given command.
     * 
     * @param command
     * @return the ArrayList of aliases; an empty ArrayList if there are no
     *         aliases thus the command should not exist
     */
    public List<String> getAliases(final String command) {
        return new ArrayList<String>(commands.get(command.toLowerCase()));
    }

    /**
     * Gets a Role from its id.
     * 
     * @param roleId
     * @return
     */
    public Role getRole(final String roleId) {
        return roles.get(roleId.toLowerCase());
    }

    /**
     * Gets the track with the given id.
     * 
     * @param id
     * @return
     */
    public RoleTrack getTrack(final String id) {
        return tracks.get(id.toLowerCase());
    }

    /**
     * Gets the default track of the GroupLevel.
     * 
     * @return
     */
    public RoleTrack getDefaultTrack() {
        return getTrack(DEFAULT_TRACK);
    }

    /**
     * Gets the initial role of a member of the group.
     * 
     * @return
     */
    public Role getInitial() {
        return initial;
    }

    /**
     * Gets the role of a founder of the group.
     * 
     * @return
     */
    public Role getFounder() {
        return founder;
    }

    /**
     * Checks if this GroupLevel can be founded.
     * 
     * @return
     */
    public boolean canFound() {
        return founder != null;
    }

    /**
     * Saves this GroupLevel to the provided node.
     * 
     * @param node
     */
    public void save(final ConfigurationNode node) {
        node.getChild("name").setValue(name);
        node.getChild("rank").setValue(rank);
        final List<String> children = new ArrayList<String>();
        for (final GroupLevel child : getAllowedChildren()) {
            children.add(child.getId());
        }
        node.getChild("children").setValue(children);
        node.getChild("plural").setValue(plural);

        final ConfigurationNode rolesNode = node.getChild("roles");
        for (final Entry<String, Role> role : roles.entrySet()) {
            final String roleName = role.getKey();
            final Role value = role.getValue();
            final List<String> privNames = new ArrayList<String>();
            for (final Privilege priv : value.getPrivileges()) {
                privNames.add(priv.getName());
            }

            rolesNode.getChild(roleName).setValue(privNames);
        }

        // TODO track serialization

        node.getChild("initial").setValue(initial.getId());
        node.getChild("founder").setValue(founder.getId());
    }

    /**
     * Loads a GroupLevel.
     * 
     * @param id
     * @param node
     * @param levels
     *            The map that the level names are stored in.
     * @return
     */
    public static GroupLevel load(String id, final ConfigurationNode node, final Map<GroupLevel, List<String>> levels) {
        // Load name
        final String levelName = node.getNode("name").getString(id);

        // Make id lowercase
        id = id.toLowerCase();

        // Load rank
        final int rank = node.getNode("rank").getInt();

        // Load children
        final List<String> children = node.getNode("children").getStringList();

        // Load roles
        final Map<String, Role> rolesMap = new HashMap<String, Role>();
        for (final Entry<String, ConfigurationNode> roleEntry : node.getNode("roles").getChildren().entrySet()) {
            final String roleId = roleEntry.getKey();
            final Role role = Role.load(roleId, roleEntry.getValue());
            rolesMap.put(roleId, role);
        }

        // Load plural
        final String plural = node.getNode("plural").getString(levelName + "s");

        // Load allowed commands
        final Map<String, List<String>> commands = new HashMap<String, List<String>>();

        // Set for checking for alias overlaps.
        final Set<String> alreadyLoadedCommands = new HashSet<String>();

        // Command node
        final ConfigurationNode commandNode = node.getNode("commands");
        for (final Entry<String, ConfigurationNode> commandAliasEntry : commandNode.getChildren().entrySet()) {
            // Name of the command we want to alias
            final String commandName = commandAliasEntry.getKey().toLowerCase();

            // Get the list we're putting aliases in
            List<String> theAliases = commands.get(commandName);
            if (theAliases == null) {
                theAliases = new ArrayList<String>();
                commands.put(commandName, theAliases);
            }

            final ConfigurationNode aliasesNode = commandAliasEntry.getValue();

            // Check for list, if so add specified aliases. Does not
            // include the normal name unless explicitly specified.
            if (aliasesNode.getValue() instanceof List) {
                final List<String> aliases = aliasesNode.getStringList();
                for (String alias : aliases) {
                    alias = alias.toLowerCase();
                    if (alreadyLoadedCommands.contains(alias)) {
                        PoliticsPlugin.logger().log(Level.WARNING,
                                "Duplicate entry for command `" + alias + "'; not adding it to aliases for " + commandName + ".");
                        continue;
                    }
                    theAliases.add(alias);
                    alreadyLoadedCommands.add(alias);
                }

                // Else, we don't care, they specified it.
            } else {
                if (alreadyLoadedCommands.contains(commandName)) {
                    PoliticsPlugin.logger().log(Level.WARNING, "Duplicate entry for command `" + commandName + "'; not adding " + commandName + ".");
                    continue;
                }
                theAliases.add(commandName);
                alreadyLoadedCommands.add(commandName);
            }
        }

        // Our variables
        final Map<String, RoleTrack> tracks = new HashMap<String, RoleTrack>();
        Role initial;
        Role founder;

        if (rolesMap.isEmpty()) {
            initial = null;
            founder = null;
        } else {
            final ConfigurationNode tracksNode = node.getChild("tracks");
            for (final Entry<String, ConfigurationNode> trackEntry : tracksNode.getChildren().entrySet()) {
                final RoleTrack track = RoleTrack.load(trackEntry.getKey(), trackEntry.getValue(), rolesMap);
                tracks.put(track.getId(), track);
            }
            if (!tracks.containsKey(DEFAULT_TRACK)) {
                RoleTrack def;
                if (tracks.isEmpty()) {
                    final List<Role> rolesSorted = new LinkedList<Role>(rolesMap.values());
                    Collections.sort(rolesSorted);
                    def = new RoleTrack(DEFAULT_TRACK, rolesSorted);
                } else {
                    def = tracks.entrySet().iterator().next().getValue();
                }
                tracks.put(DEFAULT_TRACK, def);
            }

            final String initialName = node.getChild("initial").getString();
            if (initialName == null) {
                int lowest = Integer.MAX_VALUE;
                Role lowestRole = null;
                for (final Role role : rolesMap.values()) {
                    if (role.getRank() <= lowest) { // Incase of max value for
                        // rank
                        lowest = role.getRank();
                        lowestRole = role;
                    }
                }
                initial = lowestRole;
            } else {
                initial = rolesMap.get(initialName.toLowerCase());
                if (initial == null) {
                    throw new IllegalStateException("Invalid initial role '" + initialName + "'.");
                }
            }

            final String founderName = node.getChild("founder").getString();
            if (founderName == null) {
                int highest = 0;
                Role highestRole = null;
                for (final Role role : rolesMap.values()) {
                    if (role.getRank() > highest) {
                        highest = role.getRank();
                        highestRole = role;
                    }
                }
                founder = highestRole;
            } else {
                founder = rolesMap.get(founderName.toLowerCase());
                if (founder == null) {
                    throw new IllegalStateException("Invalid founder role '" + founderName + "'.");
                }
            }
        }

        final GroupLevel theLevel = new GroupLevel(id, levelName, rank, rolesMap, plural, commands, tracks, initial, founder);
        // Children so we can get our allowed children in the future
        levels.put(theLevel, children);
        return theLevel;
    }
}
