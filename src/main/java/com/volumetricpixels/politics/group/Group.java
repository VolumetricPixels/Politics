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
package com.volumetricpixels.politics.group;

import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Map;
import java.util.Set;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;

import org.spout.api.Server;
import org.spout.api.Spout;
import org.spout.api.entity.Player;

import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.PoliticsPlugin;
import com.volumetricpixels.politics.data.Storable;
import com.volumetricpixels.politics.group.level.GroupLevel;
import com.volumetricpixels.politics.group.level.Role;
import com.volumetricpixels.politics.universe.Universe;
import com.volumetricpixels.politics.universe.UniverseRules;
import com.volumetricpixels.politics.util.PropertyDeserializationException;
import com.volumetricpixels.politics.util.PropertySerializer;
import java.util.logging.Level;
import org.spout.api.geo.discrete.Point;
import org.spout.api.geo.discrete.Transform;

/**
 * Represents a group of players.
 */
public final class Group implements Comparable<Group>, Storable {
    /**
     * The unique identifier of this group. This is unique for the entire
     * plugin.
     */
    private final int uid;

    /**
     * The level of the group.
     */
    private final GroupLevel level;

    /**
     * Properties of this group.
     */
    private final TIntObjectMap<Object> properties;

    /**
     * The immediate players of this group. The keys are the players, and the
     * values are the player privileges.
     */
    private final Map<String, Role> players;

    /**
     * The universe this group is part of.
     */
    private Universe universe;

    /**
     * C'tor
     *
     * @param universe
     * @param level
     */
    public Group(int uid, GroupLevel level) {
        this(uid, level, new TIntObjectHashMap<Object>(), new HashMap<String, Role>());
    }

    /**
     * C'tor
     *
     * @param universe
     * @param level
     * @param properties
     * @param players
     */
    private Group(int uid, GroupLevel level, TIntObjectMap<Object> properties, Map<String, Role> players) {
        this.uid = uid;
        this.level = level;
        this.properties = properties;
        this.players = players;
    }

    /**
     * Initializes the universe.
     *
     * @param universe
     */
    public void initialize(Universe universe) {
        if (universe == null || this.universe != null) {
            throw new IllegalStateException("Someone is trying to screw with the plugin!");
        }
        this.universe = universe;
    }

    /**
     * Gets the universe of this Group.
     *
     * @return
     */
    public Universe getUniverse() {
        return universe;
    }

    /**
     * Gets the UID of this Group.
     *
     * @return
     */
    public int getUid() {
        return uid;
    }

    /**
     * Gets the groups composing this group.
     *
     * @return
     */
    public Set<Group> getGroups() {
        return universe.getChildGroups(this);
    }

    /**
     * Adds the given group as a child of this group.
     *
     * @param group
     * @return True if the given group was able to be a child of the group.
     */
    public boolean addChildGroup(Group group) {
        return universe.addChildGroup(this, group);
    }

    /**
     * Removes the given group from this group's children.
     *
     * @param group
     * @return
     *
     * @see Universe#removeChildGroup(Group, Group)
     */
    public boolean removeChildGroup(Group group) {
        return universe.removeChildGroup(this, group);
    }

    /**
     * Gets the GroupLevel of this Group.
     *
     * @return
     */
    public GroupLevel getLevel() {
        return level;
    }

    /**
     * Gets the value of a property.
     *
     * @param property
     * @return
     */
    public Object getProperty(int property) {
        return properties.get(property);
    }

    /**
     * Gets a property as a String.
     *
     * @param property
     * @return
     */
    public String getStringProperty(int property) {
        return getStringProperty(property, null);
    }

    /**
     * Gets a property as a String.
     *
     * @param property
     * @param def Default value
     * @return
     */
    public String getStringProperty(int property, String def) {
        Object p = getProperty(property);
        if (p != null) {
            return p.toString();
        }
        return def;
    }

    /**
     * Gets a property as an integer.
     *
     * @param property
     * @return
     */
    public int getIntProperty(int property) {
        return getIntProperty(property, -1);
    }

    /**
     * Gets a property as an integer.
     *
     * @param property
     * @param def
     * @return
     */
    public int getIntProperty(int property, int def) {
        Object p = getProperty(property);
        if (p != null) {
            if (p instanceof Integer) {
                return ((Integer) p).intValue();
            } else {
                return def;
            }
        }
        return def;
    }

    /**
     * Gets a property as a transform.
     *
     * @param property
     * @return
     */
    public Transform getTransformProperty(int property) {
        return getTransformProperty(property, null);
    }

    /**
     * Gets a property as a transform.
     *
     * @param property
     * @param def
     * @return
     */
    public Transform getTransformProperty(int property, Transform def) {
        String s = getStringProperty(property);
        if (s == null) {
            return def;
        }
        Transform t;
        try {
            t = PropertySerializer.deserializeTransform(s);
        } catch (PropertyDeserializationException ex) {
            PoliticsPlugin.logger().log(Level.WARNING, "Property '" + Integer.toHexString(property) + "' is not a transform!", ex);
            return def;
        }
        return t;
    }

    /**
     * Gets a property as a point.
     *
     * @param property
     * @return
     */
    public Point getPointProperty(int property) {
        return getPointProperty(property, null);
    }

    /**
     * Gets a property as a point.
     *
     * @param property
     * @param def
     * @return
     */
    public Point getPointProperty(int property, Point def) {
        String s = getStringProperty(property);
        if (s == null) {
            return def;
        }
        Point p;
        try {
            p = PropertySerializer.deserializePoint(s);
        } catch (PropertyDeserializationException ex) {
            PoliticsPlugin.logger().log(Level.WARNING, "Property '" + Integer.toHexString(property) + "' is not a point!", ex);
            return def;
        }
        return p;
    }

    /**
     * Gets a property as a block.
     *
     * @param property
     * @return
     */
    public Point getBlockProperty(int property) {
        return getBlockProperty(property, null);
    }

    /**
     * Gets a property as a block.
     *
     * @param property
     * @param def
     * @return
     */
    public Point getBlockProperty(int property, Point def) {
        String s = getStringProperty(property);
        if (s == null) {
            return def;
        }
        Point p;
        try {
            p = PropertySerializer.deserializeBlock(s);
        } catch (PropertyDeserializationException ex) {
            PoliticsPlugin.logger().log(Level.WARNING, "Property '" + Integer.toHexString(property) + "' is not a block!", ex);
            return def;
        }
        return p;
    }

    /**
     * Sets the value of a transform property.
     *
     * @param property
     * @param value
     */
    public void setProperty(int property, Transform value) {
        setProperty(property, PropertySerializer.serializeTransform(value));
    }

    /**
     * Sets the value of a point property.
     *
     * @param property
     * @param value
     */
    public void setProperty(int property, Point value) {
        setProperty(property, value, false);
    }

    /**
     * Sets the value of a point or block property.
     *
     * @param property
     * @param value
     * @param block True if you wish to only store integer coordinates
     */
    public void setProperty(int property, Point value, boolean block) {
        setProperty(property, (block ? PropertySerializer.serializeBlock(value) : PropertySerializer.serializePoint(value)));
    }

    /**
     * Sets the value of a property.
     *
     * @param property
     * @param value
     */
    public void setProperty(int property, Object value) {
        properties.put(property, value);
    }

    /**
     * Gets the immediate players part of this group.
     *
     * @return
     */
    public List<String> getImmediatePlayers() {
        return new ArrayList<String>(players.keySet());
    }

    /**
     * Gets the immediate online players part of this group.
     *
     * @return
     */
    public List<Player> getImmediateOnlinePlayers() {
        List<Player> players = new ArrayList<Player>();
        for (String pn : getImmediatePlayers()) {
            Player player = ((Server) Spout.getEngine()).getPlayer(pn, true);
            if (player != null) {
                players.add(player);
            }
        }
        return players;
    }

    /**
     * Gets all players part of this group.
     *
     * @return
     */
    public List<String> getPlayers() {
        List<String> players = new ArrayList<String>();
        for (Group group : getGroups()) {
            players.addAll(group.getPlayers());
        }
        players.addAll(this.players.keySet());
        return players;
    }

    /**
     * Returns true if the given player is an immediate member of this group.
     *
     * @param player
     * @return
     */
    public boolean isImmediateMember(String player) {
        return players.containsKey(player);
    }

    /**
     * Checks if the given player is a member of this group or child groups.
     *
     * @param player
     * @return
     */
    public boolean isMember(String player) {
        if (isImmediateMember(player)) {
            return true;
        }

        for (Group group : getGroups()) {
            if (group.isMember(player)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the role of the given player.
     *
     * @param player
     * @return
     */
    public Role getRole(String player) {
        return players.get(player);
    }

    /**
     * Sets the role of the given player to the given role.
     *
     * @param player
     * @param role
     */
    public void setRole(String player, Role role) {
        players.put(player, role);
    }

    /**
     * Removes the role of the given player from this group.
     *
     * @param player
     */
    public void removeRole(String player) {
        players.remove(player);
    }

    @Override
    public int compareTo(Group o) {
        return getProperty(GroupProperty.TAG).toString().compareTo(o.getProperty(GroupProperty.TAG).toString());
    }

    @Override
    public BasicBSONObject toBSONObject() {
        BasicBSONObject object = new BasicBSONObject();

        object.put("uid", uid);
        object.put("level", level.getId());

        final BasicBSONObject propertiesBson = new BasicBSONObject();
        TIntObjectIterator<Object> pit = properties.iterator();
        while (pit.hasNext()) {
            pit.advance();
            propertiesBson.put(Integer.toHexString(pit.key()), pit.value());
        }
        object.put("properties", propertiesBson);

        final BasicBSONObject playersBson = new BasicBSONObject();
        for (Entry<String, Role> roleEntry : players.entrySet()) {
            playersBson.put(roleEntry.getKey(), roleEntry.getValue().getId());
        }
        object.put("players", playersBson);

        return object;
    }

    /**
     * Gets the Group from the given BSONObject.
     *
     * @param rules
     * @param object
     * @return
     */
    public static Group fromBSONObject(UniverseRules rules, BSONObject object) {
        if (!(object instanceof BasicBSONObject)) {
            throw new IllegalStateException("object is not a BasicBsonObject! ERROR ERROR ERROR!");
        }

        BasicBSONObject bobject = (BasicBSONObject) object;

        int uid = bobject.getInt("uid");

        String levelName = bobject.getString("level");
        GroupLevel level = rules.getGroupLevel(levelName);
        if (level == null) {
            throw new IllegalStateException("Unknown level type '" + level + "'! (Did the universe rules change?)");
        }

        // Properties
        Object propertiesObj = bobject.get("properties");
        if (!(propertiesObj instanceof BasicBSONObject)) {
            throw new IllegalStateException("WTF you screwed up the properties! CORRUPT!");
        }
        BasicBSONObject propertiesBson = (BasicBSONObject) propertiesObj;
        TIntObjectMap<Object> properties = new TIntObjectHashMap<Object>();
        for (Entry<String, Object> entry : propertiesBson.entrySet()) {
            int realKey = Integer.valueOf(entry.getKey(), 16);
            Object value = entry.getValue();
            properties.put(realKey, value);
        }

        // Players
        Object playersObj = bobject.get("players");
        if (!(playersObj instanceof BasicBSONObject)) {
            throw new IllegalStateException("Stupid server admin... don't mess with the data!");
        }
        BasicBSONObject playersBson = (BasicBSONObject) playersObj;
        Map<String, Role> players = new HashMap<String, Role>();
        for (Entry<String, Object> entry : playersBson.entrySet()) {
            String roleId = entry.getValue().toString();
            Role role = level.getRole(roleId);
            players.put(entry.getKey(), role);
        }

        return new Group(uid, level, properties, players);
    }
}
