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
package com.volumetricpixels.politics;

import org.spout.api.geo.World;

import com.volumetricpixels.politics.group.GroupLevel;
import com.volumetricpixels.politics.plot.PlotManager;
import com.volumetricpixels.politics.plot.PoliticsWorld;
import com.volumetricpixels.politics.universe.Universe;
import com.volumetricpixels.politics.universe.UniverseManager;

/**
 * Static API access
 */
public final class Politics {
	/**
	 * C'tor
	 */
	private Politics() {
	}

	/**
	 * Gets the GroupsPlugin instance.
	 * 
	 * @return
	 */
	public static PoliticsPlugin getPlugin() {
		return PoliticsPlugin.getInstance();
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
	 * Gets a GroupsWorld by a world.
	 * 
	 * @param world
	 * @return
	 */
	public static PoliticsWorld getWorld(World world) {
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
	 * Gets the universe corresponding with the given world and group level.
	 * 
	 * @param world
	 * @param level
	 * @return
	 */
	public static Universe getUniverse(World world, GroupLevel level) {
		return getUniverseManager().getUniverse(world, level);
	}
}
