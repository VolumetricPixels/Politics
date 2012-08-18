/*
 * This file is part of Colonies.
 *
 * Colonies
 * Copyright (c) 2012-2012, THEDevTeam <http://thedevteam.org/>
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
package com.simplyian.colonies.colony;

import gnu.trove.iterator.TObjectIntIterator;
import gnu.trove.iterator.TShortObjectIterator;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.TShortObjectMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import gnu.trove.map.hash.TShortObjectHashMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;

import com.simplyian.colonies.data.Storable;
import com.simplyian.colonies.universe.Universe;

/**
 * Represents a colony of players.
 */
public final class Colony implements Comparable<Colony>, Storable {
	/**
	 * The universe this colony is part of.
	 */
	private final Universe universe;

	/**
	 * The level of the colony.
	 */
	private final ColonyLevel level;

	/**
	 * Properties of this colony.
	 */
	private final TShortObjectMap<Object> properties;

	/**
	 * The immediate players of this colony. The keys are the players, and the
	 * values are the player privileges.
	 */
	private final TObjectIntMap<String> players;

	/**
	 * C'tor
	 * 
	 * @param universe
	 * @param level
	 */
	public Colony(Universe universe, ColonyLevel level) {
		this(universe, level, new TShortObjectHashMap<Object>(), new TObjectIntHashMap<String>());
	}

	/**
	 * C'tor
	 * 
	 * @param universe
	 * @param level
	 * @param properties
	 * @param players
	 */
	private Colony(Universe universe, ColonyLevel level, TShortObjectMap<Object> properties, TObjectIntMap<String> players) {
		this.universe = universe;
		this.level = level;
		this.properties = properties;
		this.players = players;
	}

	/**
	 * Gets the name of the colony.
	 * 
	 * @return
	 */
	public String getName() {
		return getProperty(ColonyProperty.NAME).toString();
	}

	/**
	 * Sets the name of the colony.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		setProperty(ColonyProperty.NAME, name);
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
	 * Gets the colonies composing this colony.
	 * 
	 * @return
	 */
	public Set<Colony> getColonies() {
		return universe.getChildColonies(this);
	}

	/**
	 * Adds the given colony as a child of this colony.
	 * 
	 * @param colony
	 * @return True if the given colony was able to be a child of the colony.
	 */
	public boolean addChildColony(Colony colony) {
		return universe.addChildColony(this, colony);
	}

	/**
	 * Removes the given colony from this colony's children.
	 * 
	 * @param colony
	 * @return
	 * 
	 * @see Universe#removeChildColony(Colony, Colony)
	 */
	public boolean removeChildColony(Colony colony) {
		return universe.removeChildColony(this, colony);
	}

	/**
	 * Gets the ColonyLevel of this Colony.
	 * 
	 * @return
	 */
	public ColonyLevel getLevel() {
		return level;
	}

	/**
	 * Gets the value of a property.
	 * 
	 * @param property
	 * @return
	 */
	public Object getProperty(short property) {
		return properties.get(property);
	}

	/**
	 * Sets the value of a property.
	 * 
	 * @param property
	 * @param value
	 */
	public void setProperty(short property, Serializable value) {
		properties.put(property, value);
	}

	/**
	 * Gets the immediate players part of this colony.
	 * 
	 * @return
	 */
	public Set<String> getImmediatePlayers() {
		return players.keySet();
	}

	/**
	 * Gets all players part of this colony.
	 * 
	 * @return
	 */
	public List<String> getPlayers() {
		List<String> players = new ArrayList<String>();
		for (Colony colony : getColonies()) {
			players.addAll(colony.getPlayers());
		}
		players.addAll(this.players.keySet());
		return players;
	}

	/**
	 * Returns true if the given player is an immediate member of this colony.
	 * 
	 * @param player
	 * @return
	 */
	public boolean isImmediateMember(String player) {
		return players.containsKey(player);
	}

	/**
	 * Checks if the given player is a member of this colony or child colonies.
	 * 
	 * @param player
	 * @return
	 */
	public boolean isMember(String player) {
		if (isImmediateMember(player)) {
			return true;
		}

		for (Colony colony : getColonies()) {
			if (colony.isMember(player)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int compareTo(Colony o) {
		return getName().compareTo(o.getName());
	}

	public BasicBSONObject toBSONObject() {
		BasicBSONObject object = new BasicBSONObject();

		object.put("level", level.getName());

		final BasicBSONObject propertiesBson = new BasicBSONObject();
		TShortObjectIterator<Object> pit = properties.iterator();
		while (pit.hasNext()) {
			pit.advance();
			propertiesBson.put(Integer.toHexString(pit.key()), pit.value());
		}
		object.put("properties", propertiesBson);

		final BasicBSONObject playersBson = new BasicBSONObject();
		TObjectIntIterator<String> lit = players.iterator();
		while (lit.hasNext()) {
			lit.advance();
			playersBson.put(lit.key(), lit.value());
		}
		object.put("players", lit);

		return object;
	}

	/**
	 * Gets the Colony from the given BSONObject.
	 * 
	 * @param universe
	 * @param object
	 * @return
	 */
	public static Colony fromBSONObject(Universe universe, BSONObject object) {
		if (!(object instanceof BasicBSONObject)) {
			throw new IllegalStateException("object is not a BasicBsonObject! ERROR ERROR ERROR!");
		}

		BasicBSONObject bobject = (BasicBSONObject) object;
		String levelName = bobject.getString("level");
		ColonyLevel level = universe.getRules().getColonyLevel(levelName);
		if (level == null) {
			throw new IllegalStateException("Unknown level type '" + level + "'! (Did the universe rules change?)");
		}

		// Properties
		Object propertiesObj = bobject.get("properties");
		if (!(propertiesObj instanceof BasicBSONObject)) {
			throw new IllegalStateException("WTF you screwed up the properties! CORRUPT!");
		}
		BasicBSONObject propertiesBson = (BasicBSONObject) propertiesObj;
		TShortObjectMap<Object> properties = new TShortObjectHashMap<Object>();
		for (Entry<String, Object> entry : propertiesBson.entrySet()) {
			int realKey = Integer.valueOf(entry.getKey(), 16);
			short realKeyShort = (short) realKey;
			Object value = entry.getValue();
			properties.put(realKeyShort, value);
		}

		// Players
		Object playersObj = bobject.get("players");
		if (!(playersObj instanceof BasicBSONObject)) {
			throw new IllegalStateException("Stupid server admin... don't mess with the data!");
		}
		BasicBSONObject playersBson = (BasicBSONObject) playersObj;
		TObjectIntMap<String> players = new TObjectIntHashMap<String>();
		for (Entry<String, Object> entry : playersBson.entrySet()) {
			int mask = ((Integer) entry.getValue()).intValue();
			players.put(entry.getKey(), mask);
		}

		return new Colony(universe, level, properties, players);
	}
}
