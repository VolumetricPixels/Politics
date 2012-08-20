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
package com.simplyian.colonies.plot;

import java.util.HashMap;
import java.util.Map;

import org.spout.api.geo.World;

/**
 * Manages plots.
 */
public class PlotManager {
	/**
	 * World names mapped to ColonyWorlds.
	 */
	private Map<String, ColoniesWorld> worlds = new HashMap<String, ColoniesWorld>();

	/**
	 * Gets a ColonyWorld from its name.
	 * 
	 * @param name
	 * @return
	 */
	public ColoniesWorld getWorld(String name) {
		return worlds.get(name);
	}

	/**
	 * Gets a ColonyWorld from its World.
	 * 
	 * @param world
	 * @return
	 */
	public ColoniesWorld getWorld(World world) {
		return getWorld(world.getName());
	}

}
