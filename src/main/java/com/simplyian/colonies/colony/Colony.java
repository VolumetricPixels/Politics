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

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.TShortObjectMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import gnu.trove.map.hash.TShortObjectHashMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.simplyian.colonies.universe.Universe;

/**
 * Represents a colony of players.
 */
public final class Colony implements Comparable<Colony> {
	/**
	 * The universe this colony is part of.
	 */
	private final Universe universe;

	/**
	 * The level of the colony.
	 */
	private ColonyLevel level;

	/**
	 * Properties of this colony.
	 */
	private TShortObjectMap<Object> properties = new TShortObjectHashMap<Object>();

	/**
	 * The immediate players of this colony. The keys are the players, and the
	 * values are the player privileges.
	 */
	private TObjectIntMap<String> players = new TObjectIntHashMap<String>();

	/**
	 * C'tor
	 * 
	 * @param universe
	 */
	public Colony(Universe universe) {
		this.universe = universe;
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
}
