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
import gnu.trove.map.hash.TObjectIntHashMap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.simplyian.colonies.Universe;

/**
 * Represents a colony of players.
 */
public abstract class Colony {
	/**
	 * The universe this colony is part of.
	 */
	private final Universe universe;

	/**
	 * The immediate child colonies of this colony.
	 */
	private Set<Colony> colonies;

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
	public Set<Colony> getGroups() {
		return new HashSet<Colony>(colonies);
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
		for (Colony colony : colonies) {
			players.addAll(colony.getPlayers());
		}
		players.addAll(this.players.keySet());
		return players;
	}

	/**
	 * Checks if the given player is a member of this colony.
	 * 
	 * @param player
	 * @return
	 */
	public boolean isMember(String player) {
		if (players.containsKey(player)) {
			return true;
		}

		for (Colony colony : colonies) {
			if (colony.isMember(player)) {
				return true;
			}
		}
		return false;
	}
}
