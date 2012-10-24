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
package com.volumetricpixels.politics.world;

import org.spout.api.math.Vector3;
import org.spout.api.util.config.Configuration;

/**
 * Configuration of a world.
 */
public class WorldConfig {
    /**
     * The name of the WorldConfig.
     */
    private final String name;

    /**
     * X size in chunks of a plot.
     */
    private int plotSizeX = 1;

    /**
     * Y size in chunks of a plot.
     */
    private int plotSizeY = 1;

    /**
     * Z size in chunks of a plot.
     */
    private int plotSizeZ = 1;

    /**
     * C'tor
     */
    public WorldConfig(String name) {
        this.name = name;
    }

    /**
     * Gets the name of the WorldConfig.
     * 
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the plot size as a vector.
     * 
     * @return
     */
    public Vector3 getPlotSizeVector() {
        return new Vector3(plotSizeX, plotSizeY, plotSizeZ);
    }

    /**
     * @return the plotSizeX
     */
    public int getPlotSizeX() {
        return plotSizeX;
    }

    /**
     * @return the plotSizeY
     */
    public int getPlotSizeY() {
        return plotSizeY;
    }

    /**
     * @return the plotSizeZ
     */
    public int getPlotSizeZ() {
        return plotSizeZ;
    }

    /**
     * Saves a WorldConfig.
     * 
     * @param config
     */
    public void save(Configuration config) {
        config.getNode("plotsize.x").setValue(plotSizeX);
        config.getNode("plotsize.y").setValue(plotSizeY);
        config.getNode("plotsize.z").setValue(plotSizeZ);
    }

    /**
     * @param plotSizeX
     *            the plotSizeX to set
     */
    public void setPlotSizeX(int plotSizeX) {
        this.plotSizeX = plotSizeX;
    }

    /**
     * @param plotSizeY
     *            the plotSizeY to set
     */
    public void setPlotSizeY(int plotSizeY) {
        this.plotSizeY = plotSizeY;
    }

    /**
     * @param plotSizeZ
     *            the plotSizeZ to set
     */
    public void setPlotSizeZ(int plotSizeZ) {
        this.plotSizeZ = plotSizeZ;
    }

    /**
     * Loads a WorldConfig.
     * 
     * @param name
     * @param config
     * @return the new WorldConfig
     */
    public static WorldConfig load(String name, Configuration config) {
        WorldConfig wc = new WorldConfig(name);
        wc.plotSizeX = config.getNode("plotsize.x").getInt(1);
        wc.plotSizeY = config.getNode("plotsize.y").getInt(8);
        wc.plotSizeZ = config.getNode("plotsize.z").getInt(1);
        return wc;
    }
}