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
package com.simplyian.colonies.universe;

import java.util.HashMap;
import java.util.Map;

import org.spout.api.geo.World;

import com.simplyian.colonies.ColoniesPlugin;
import com.simplyian.colonies.colony.ColonyLevel;

/**
 * Contains all universes.
 */
public class UniverseManager {
	/**
	 * Plugin instance
	 */
	private final ColoniesPlugin plugin;

	/**
	 * Universes mapped to their names.
	 */
	private Map<String, Universe> universes;

	/**
	 * World names mapped to ColonyWorlds.
	 */
	private Map<String, ColonyWorld> worlds = new HashMap<String, ColonyWorld>();

	/**
	 * C'tor
	 * 
	 * @param plugin
	 */
	public UniverseManager(ColoniesPlugin plugin) {
		this.plugin = plugin;
	}

	/**
	 * Loads all universes into memory from files.
	 */
	public void loadUniverses() {

	}

	/**
	 * Gets a universe by its name.
	 * 
	 * @param name
	 * @return
	 */
	public Universe getUniverse(String name) {
		return universes.get(name.toLowerCase());
	}

	/**
	 * Gets the universe of the given CommandSource.
	 * 
	 * @param world
	 * @param level
	 * @return
	 */
	public Universe getUniverse(World world, ColonyLevel level) {
		ColonyWorld cw = getWorld(world);
		if (cw == null) {
			return null;
		}
		return cw.getUniverse(level);
	}

	/**
	 * Gets a ColonyWorld from its name.
	 * 
	 * @param name
	 * @return
	 */
	public ColonyWorld getWorld(String name) {
		return worlds.get(name);
	}

	/**
	 * Gets a ColonyWorld from its World.
	 * 
	 * @param world
	 * @return
	 */
	public ColonyWorld getWorld(World world) {
		return getWorld(world.getName());
	}
}
