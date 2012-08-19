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
package com.simplyian.colonies;

import org.spout.api.geo.World;

import com.simplyian.colonies.colony.ColonyLevel;
import com.simplyian.colonies.plot.ColoniesWorld;
import com.simplyian.colonies.plot.PlotManager;
import com.simplyian.colonies.universe.Universe;
import com.simplyian.colonies.universe.UniverseManager;

/**
 * Static API access
 */
public final class Colonies {
	/**
	 * C'tor
	 */
	private Colonies() {
	}

	/**
	 * Gets the ColoniesPlugin instance.
	 * 
	 * @return
	 */
	public static ColoniesPlugin getPlugin() {
		return ColoniesPlugin.getInstance();
	}

	/**
	 * Gets the PlotManager of the plugin.
	 * 
	 * @return
	 */
	public static PlotManager getPlotManager() {
		return getPlugin().getPlotManager();
	}

	/**
	 * Gets the UniverseManager of the plugin.
	 * 
	 * @return
	 */
	public static UniverseManager getUniverseManager() {
		return getPlugin().getUniverseManager();
	}

	/**
	 * Gets a ColoniesWorld by a world.
	 * 
	 * @param world
	 * @return
	 */
	public static ColoniesWorld getWorld(World world) {
		return getPlotManager().getWorld(world);
	}

	/**
	 * Gets a Universe by its name.
	 * 
	 * @param name
	 * @return
	 */
	public static Universe getUniverse(String name) {
		return getUniverseManager().getUniverse(name);
	}

	/**
	 * Gets the universe corresponding with the given world and colony level.
	 * 
	 * @param world
	 * @param level
	 * @return
	 */
	public static Universe getUniverse(World world, ColonyLevel level) {
		return getUniverseManager().getUniverse(world, level);
	}
}
