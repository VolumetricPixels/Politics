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
package com.volumetricpixels.politics.data;

import java.io.File;

import com.volumetricpixels.politics.Politics;

/**
 * Manages all Files used in Politics
 */
public class PoliticsFileSystem {
    /**
     * Rules directory;
     */
    private final File rulesDir;

    /**
     * Universe directory
     */
    private final File universesDir;

    /**
     * World Configuration Directory
     */
    private final File worldConfigDir;

    /**
     * Directory containing the folders.
     */
    private final File worldsDir;

    /**
     * C'tor
     */
    public PoliticsFileSystem() {
        File base = Politics.getPlugin().getDataFolder();

        rulesDir = new File(base, "rules/");
        worldConfigDir = new File(base, "worlds/");
        worldsDir = new File(base, "data/worlds/");
        universesDir = new File(base, "data/universes/");

        if (!rulesDir.exists())
            rulesDir.mkdirs();
        if (!worldConfigDir.exists())
            worldConfigDir.mkdirs();
        if (!worldsDir.exists())
            worldsDir.mkdirs();
        if (!universesDir.exists())
            universesDir.mkdirs();
    }

    /**
     * Gets the rules directory.
     * 
     * @return the rules directory
     */
    public File getRulesDir() {
        return rulesDir;
    }

    /**
     * Gets the world config directory.
     * 
     * @return the world config directory
     */
    public File getWorldConfigDir() {
        return worldConfigDir;
    }

    /**
     * Gets the universes directory.
     * 
     * @return the universes directory
     */
    public File getUniversesDir() {
        return universesDir;
    }

    /**
     * Gets the worlds directory.
     * 
     * @return the worlds directory
     */
    public File getWorldsDir() {
        return worldsDir;
    }
}
