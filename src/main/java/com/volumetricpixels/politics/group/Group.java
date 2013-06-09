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
package com.volumetricpixels.politics.group;

import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import org.spout.api.Server;
import org.spout.api.command.CommandSource;
import org.spout.api.entity.Player;
import org.spout.api.geo.discrete.Point;
import org.spout.api.geo.discrete.Transform;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;

import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.PoliticsPlugin;
import com.volumetricpixels.politics.data.Storable;
import com.volumetricpixels.politics.event.PoliticsEventFactory;
import com.volumetricpixels.politics.exception.PropertyDeserializationException;
import com.volumetricpixels.politics.exception.PropertySerializationException;
import com.volumetricpixels.politics.group.level.GroupLevel;
import com.volumetricpixels.politics.group.level.Role;
import com.volumetricpixels.politics.group.privilege.Privilege;
import com.volumetricpixels.politics.universe.Universe;
import com.volumetricpixels.politics.universe.UniverseRules;
import com.volumetricpixels.politics.util.PropertySerializer;

/**
 * Represents a group of players
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
     * The universe this group is part of
     */
    private Universe universe;

    /**
     * C'tor
     * 
     * @param universe
     * @param level
     */
    public Group(final int uid, final GroupLevel level) {
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
    private Group(final int uid, final GroupLevel level, final TIntObjectMap<Object> properties, final Map<String, Role> players) {
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
    public void initialize(final Universe universe) {
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
    public boolean addChildGroup(final Group group) {
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
    public boolean removeChildGroup(final Group group) {
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
    public Object getProperty(final int property) {
        return properties.get(property);
    }

    /**
     * Gets a property as a String.
     * 
     * @param property
     * @return
     */
    public String getStringProperty(final int property) {
        return getStringProperty(property, null);
    }

    /**
     * Gets a property as a String.
     * 
     * @param property
     *            The property to get
     * @param def
     *            Default value
     * @return The value of the given property, or <code>def</code> if not
     *         exists
     */
    public String getStringProperty(final int property, final String def) {
        final Object p = getProperty(property);
        if (p != null) {
            return p.toString();
        }
        return def;
    }

    /**
     * Gets a property as an integer.
     * 
     * @param property
     *            The property to get
     * @return The int value of the given property
     */
    public int getIntProperty(final int property) {
        return getIntProperty(property, -1);
    }

    /**
     * Gets a property as an integer.
     * 
     * @param property
     *            The property to get
     * @param def
     *            The default value
     * @return The value of the property, or <code>def</code> if not exists
     */
    public int getIntProperty(final int property, final int def) {
        final Object p = getProperty(property);
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
    public Transform getTransformProperty(final int property) {
        return getTransformProperty(property, null);
    }

    /**
     * Gets a property as a transform.
     * 
     * @param property
     * @param def
     * @return
     */
    public Transform getTransformProperty(final int property, final Transform def) {
        final String s = getStringProperty(property);
        if (s == null) {
            return def;
        }
        try {
            return (Transform) PropertySerializer.deserialize(s);
        } catch (final PropertyDeserializationException ex) {
            PoliticsPlugin.logger().log(Level.WARNING, "Property '" + Integer.toHexString(property) + "' is not a transform!", ex);
            return def;
        }
    }

    /**
     * Gets a property as a point.
     * 
     * @param property
     * @return
     */
    public Point getPointProperty(final int property) {
        return getPointProperty(property, null);
    }

    /**
     * Gets a property as a point.
     * 
     * @param property
     * @param def
     * @return
     */
    public Point getPointProperty(final int property, final Point def) {
        final String s = getStringProperty(property);
        if (s == null) {
            return def;
        }
        try {
            return (Point) PropertySerializer.deserialize(s);
        } catch (final PropertyDeserializationException ex) {
            PoliticsPlugin.logger().log(Level.WARNING, "Property '" + Integer.toHexString(property) + "' is not a point!", ex);
            return def;
        }
    }

    /**
     * Sets the value of a transform property.
     * 
     * @param property
     * @param value
     */
    public void setProperty(final int property, final Transform value) {
        try {
            setProperty(property, PropertySerializer.serialize(value));
        } catch (final PropertySerializationException e) {
            Politics.getLogger().log(Level.SEVERE, "Error serializing property!", e);
        }
    }

    /**
     * Sets the value of a point or block property.
     * 
     * @param property
     * @param value
     * @param block
     *            True if you wish to only store integer coordinates
     */
    public void setProperty(final int property, final Point value) {
        try {
            setProperty(property, PropertySerializer.serialize(value));
        } catch (final PropertySerializationException e) {
            Politics.getLogger().log(Level.SEVERE, "Error serializing property!", e);
        }
    }

    /**
     * Sets the value of a property.
     * 
     * @param property
     * @param value
     */
    public void setProperty(final int property, final Object value) {
        PoliticsEventFactory.callGroupPropertySetEvent(this, property, value);
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
     * Gets the immediate online players part of this group
     * 
     * @return
     */
    public List<Player> getImmediateOnlinePlayers() {
        final List<Player> players = new ArrayList<Player>();
        for (final String pn : getImmediatePlayers()) {
            final Player player = ((Server) Politics.getPlugin().getEngine()).getPlayer(pn, true);
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
        final List<String> players = new ArrayList<String>();
        for (final Group group : getGroups()) {
            players.addAll(group.getPlayers());
        }
        players.addAll(this.players.keySet());
        return players;
    }

    /**
     * Returns true if the given player is an immediate member of this group
     * 
     * @param player
     * @return
     */
    public boolean isImmediateMember(final String player) {
        return players.containsKey(player);
    }

    /**
     * Checks if the given player is a member of this group or child groups.
     * 
     * @param player
     * @return
     */
    public boolean isMember(final String player) {
        if (isImmediateMember(player)) {
            return true;
        }

        for (final Group group : getGroups()) {
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
    public Role getRole(final String player) {
        return players.get(player);
    }

    /**
     * Sets the role of the given player to the given role.
     * 
     * @param player
     * @param role
     */
    public void setRole(final String player, final Role role) {
        players.put(player, role);
    }

    /**
     * Removes the role of the given player from this group
=======
     * Removes the role of the given player from this group.
>>>>>>> parent of 3f40c1b... Protection work & remove unused methods from Base64Coder
     * 
     * @param player
     */
    public void removeRole(final String player) {
        players.remove(player);
    }

    /**
     * Checks if the given CommandSource has a certain privilege.
     * 
     * @param source
     * @param privilege
     * @return True if the source has the privilege
     */
    public boolean can(final CommandSource source, final Privilege privilege) {
        if (source instanceof Player) {
            final Role role = getRole(source.getName());
            return role == null ? false : role.hasPrivilege(privilege);
        }
        return true;
    }

    /**
     * Gets the parent of this group.
     * 
     * @return
     */
    public Group getParent() {
        for (final Group group : universe.getGroups()) {
            if (group.getGroups().contains(group)) {
                return group;
            }
        }
        return null;
    }

    @Override
    public int compareTo(final Group o) {
        return getProperty(GroupProperty.TAG).toString().compareTo(o.getProperty(GroupProperty.TAG).toString());
    }

    @Override
    public BasicBSONObject toBSONObject() {
        final BasicBSONObject object = new BasicBSONObject();

        object.put("uid", uid);
        object.put("level", level.getId());

        final BasicBSONObject propertiesBson = new BasicBSONObject();
        final TIntObjectIterator<Object> pit = properties.iterator();
        while (pit.hasNext()) {
            pit.advance();
            propertiesBson.put(Integer.toHexString(pit.key()), pit.value());
        }
        object.put("properties", propertiesBson);

        final BasicBSONObject playersBson = new BasicBSONObject();
        for (final Entry<String, Role> roleEntry : players.entrySet()) {
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
    public static Group fromBSONObject(final UniverseRules rules, final BSONObject object) {
        if (!(object instanceof BasicBSONObject)) {
            throw new IllegalStateException("object is not a BasicBsonObject! ERROR ERROR ERROR!");
        }

        final BasicBSONObject bobject = (BasicBSONObject) object;

        final int uid = bobject.getInt("uid");

        final String levelName = bobject.getString("level");
        final GroupLevel level = rules.getGroupLevel(levelName);
        if (level == null) {
            throw new IllegalStateException("Unknown level type '" + level + "'! (Did the universe rules change?)");
        }

        // Properties
        final Object propertiesObj = bobject.get("properties");
        if (!(propertiesObj instanceof BasicBSONObject)) {
            throw new IllegalStateException("WTF you screwed up the properties! CORRUPT!");
        }
        final BasicBSONObject propertiesBson = (BasicBSONObject) propertiesObj;
        final TIntObjectMap<Object> properties = new TIntObjectHashMap<Object>();
        for (final Entry<String, Object> entry : propertiesBson.entrySet()) {
            final int realKey = Integer.valueOf(entry.getKey(), 16);
            final Object value = entry.getValue();
            properties.put(realKey, value);
        }

        // Players
        final Object playersObj = bobject.get("players");
        if (!(playersObj instanceof BasicBSONObject)) {
            throw new IllegalStateException("Stupid server admin... don't mess with the data!");
        }
        final BasicBSONObject playersBson = (BasicBSONObject) playersObj;
        final Map<String, Role> players = new HashMap<String, Role>();
        for (final Entry<String, Object> entry : playersBson.entrySet()) {
            final String roleId = entry.getValue().toString();
            final Role role = level.getRole(roleId);
            players.put(entry.getKey(), role);
        }

        return new Group(uid, level, properties, players);
    }

    @Override
    public boolean canStore() {
        return true;
    }
}
