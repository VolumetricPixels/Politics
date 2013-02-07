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

import java.util.logging.Logger;

import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;

import com.volumetricpixels.politics.data.PoliticsFileSystem;
import com.volumetricpixels.politics.group.level.GroupLevel;
import com.volumetricpixels.politics.group.privilege.PrivilegeManager;
import com.volumetricpixels.politics.universe.Universe;
import com.volumetricpixels.politics.universe.UniverseManager;
import com.volumetricpixels.politics.world.Plot;
import com.volumetricpixels.politics.world.PlotManager;
import com.volumetricpixels.politics.world.PoliticsWorld;

/**
 * Static class for accessing the managers of Politics, along with some utility methods
 */
public final class Politics {

    /**
     * The URL to download Politics from
     */
    public static final String CI_URL = "";

    /**
     * Gets the PoliticsPlugin instance.
     *
     * @return The running PoliticsPlugin instance
     */
    public static PoliticsPlugin getPlugin() {
        return PoliticsPlugin.getInstance();
    }

    /**
     * Gets the PoliticsFileSystem of the plugin.
     *
     * @return The PoliticsFileSystem used to keep track of files
     */
    public static PoliticsFileSystem getFileSystem() {
        return getPlugin().getFileSystem();
    }

    /**
     * Gets the PlotManager of the plugin.
     *
     * @return The PlotManager used to keep track of Plots
     */
    public static PlotManager getPlotManager() {
        return getPlugin().getPlotManager();
    }

    /**
     * Gets the PrivilegeManager of the plugin.
     *
     * @return
     */
    public static PrivilegeManager getPrivilegeManager() {
        return getPlugin().getPrivilegeManager();
    }

    /**
     * Gets the UniverseManager of the plugin.
     *
     * @return The UniverseManager used to keep track of Universes
     */
    public static UniverseManager getUniverseManager() {
        return getPlugin().getUniverseManager();
    }

    /**
     * Gets the plot at the given position.
     *
     * @param position The Point the ChunkPlot is located at
     * @return The ChunkPlot the given Point is inside
     */
    public static Plot getPlotAt(final Point position) {
        return getPlotManager().getPlotAt(position);
    }

    /**
     * Gets a PoliticsWorld by a world.
     *
     * @param world The Spout World to get the PoliticsWorld from
     * @return The PoliticsWorld representing the given World
     */
    public static PoliticsWorld getWorld(final World world) {
        return getPlotManager().getWorld(world);
    }

    /**
     * Gets a Universe by its name.
     *
     * @param name The name of the requested Universe
     * @return The Universe that goes by the given name
     */
    public static Universe getUniverse(final String name) {
        return getUniverseManager().getUniverse(name);
    }

    /**
     * Gets the universe corresponding with the given world and group level.
     *
     * @param world The World the universe is in
     * @param level The GroupLevel the universe corresponds with
     * @return The Universe for the given World and GroupLevel
     */
    public static Universe getUniverse(final World world, final GroupLevel level) {
        return getUniverseManager().getUniverse(world, level);
    }

    /**
     * Gets the Logger Politics uses to print messages to the console
     *
     * @return Politics' unique Logger
     */
    public static Logger getLogger() {
        return PoliticsPlugin.logger();
    }

    /**
     * Gets the running version of Politics
     *
     * @return The version of Politics on the server
     */
    public static String getVersion() {
        return getPlugin().getVersion();
    }

    /**
     * C'tor
     */
    private Politics() {
    }
}
