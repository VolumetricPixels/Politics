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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.spout.api.exception.ConfigurationException;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.discrete.Point;
import org.spout.api.util.config.Configuration;
import org.spout.api.util.config.yaml.YamlConfiguration;

import org.apache.commons.io.FileUtils;

import org.bson.BSONDecoder;
import org.bson.BSONEncoder;
import org.bson.BSONObject;
import org.bson.BasicBSONDecoder;
import org.bson.BasicBSONEncoder;

import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.PoliticsPlugin;

/**
 * Manages plots.
 */
public class PlotManager {
    /**
     * Configs of worlds.
     */
    private Map<String, WorldConfig> configs;

    /**
     * World names mapped to GroupWorlds.
     */
    private Map<String, PoliticsWorld> worlds;

    /**
     * C'tor
     */
    public PlotManager() {
    }

    /**
     * Gets the plot at the given position.
     * 
     * @param position
     * @return
     */
    public Plot getPlotAt(Point position) {
        return getPlotAtPosition(position.getWorld(), position.getChunkX(), position.getChunkY(), position.getChunkZ());
    }

    /**
     * Gets the plot corresponding with the given Chunk.
     * 
     * @param chunk
     * @return
     */
    public Plot getPlotAtChunk(Chunk chunk) {
        return getWorld(chunk.getWorld()).getPlotAtChunkPosition(chunk.getX(), chunk.getY(), chunk.getZ());
    }

    /**
     * Gets the plot at the given chunk position.
     * 
     * @param world
     * @param x
     * @param y
     * @param z
     * @return
     */
    public Plot getPlotAtPosition(String world, int x, int y, int z) {
        return getWorld(world).getPlotAtChunkPosition(x, y, z);
    }

    /**
     * Gets the plot at the given chunk position.
     * 
     * @param world
     * @param x
     * @param y
     * @param z
     * @return
     */
    public Plot getPlotAtPosition(World world, int x, int y, int z) {
        return getWorld(world).getPlotAtChunkPosition(x, y, z);
    }

    /**
     * Gets a PoliticsWorld from its name.
     * 
     * @param name
     * @return
     */
    public PoliticsWorld getWorld(String name) {
        PoliticsWorld world = worlds.get(name);
        if (world == null) {
            world = createWorld(name);
        }
        return world;
    }

    /**
     * Gets a PoliticsWorld from its World.
     * 
     * @param world
     * @return
     */
    public PoliticsWorld getWorld(World world) {
        return getWorld(world.getName());
    }

    /**
     * Gets the WorldConfig of the given world name.
     * 
     * @param name
     * @return
     */
    public WorldConfig getWorldConfig(String name) {
        WorldConfig conf = configs.get(name);
        if (conf == null) {
            conf = new WorldConfig(name);
            Politics.getFileSystem().getWorldConfigDir().mkdirs();
            File toSave = new File(Politics.getFileSystem().getWorldConfigDir(), name + ".yml");
            Configuration tc = new YamlConfiguration(toSave);
            conf.save(tc);
            try {
                tc.save();
            } catch (ConfigurationException e) {
                PoliticsPlugin.logger().log(Level.SEVERE, "Could not write a world config file!", e);
            }
            configs.put(name, conf);
        }
        return conf;
    }

    /**
     * Loads all World configurations.
     */
    public void loadWorldConfigs() {
        Politics.getFileSystem().getWorldConfigDir().mkdirs();
        configs = new HashMap<String, WorldConfig>();
        for (File file : Politics.getFileSystem().getWorldConfigDir().listFiles()) {
            String fileName = file.getName();
            if (!fileName.endsWith(".yml") || fileName.length() <= 4) {
                continue;
            }
            String name = fileName.substring(0, fileName.length() - 4);

            Configuration config = new YamlConfiguration(file);
            WorldConfig wc = WorldConfig.load(name, config);
            configs.put(name, wc);
        }
    }

    /**
     * Loads all GroupsWorlds.
     */
    public void loadWorlds() {
        BSONDecoder decoder = new BasicBSONDecoder();
        worlds = new HashMap<String, PoliticsWorld>();

        Politics.getFileSystem().getWorldsDir().mkdirs();
        for (File file : Politics.getFileSystem().getWorldsDir().listFiles()) {
            String fileName = file.getName();
            if (!fileName.endsWith(".ptw") || fileName.length() <= 4) {
                continue;
            }
            String worldName = fileName.substring(0, fileName.length() - 4);

            byte[] data;
            try {
                data = FileUtils.readFileToByteArray(file);
            } catch (IOException ex) {
                PoliticsPlugin.logger().log(Level.SEVERE, "Could not read world file `" + fileName + "'!", ex);
                continue;
            }

            WorldConfig config = getWorldConfig(worldName);
            BSONObject object = decoder.readObject(data);
            PoliticsWorld world = PoliticsWorld.fromBSONObject(worldName, config, object);
            worlds.put(world.getName(), world);
        }
    }

    /**
     * Saves all GroupsWorlds.
     */
    public void saveWorlds() {
        BSONEncoder encoder = new BasicBSONEncoder();
        Politics.getFileSystem().getWorldsDir().mkdirs();

        for (PoliticsWorld world : worlds.values()) {
            String fileName = world.getName() + ".cow";
            File worldFile = new File(Politics.getFileSystem().getWorldsDir(), fileName);

            byte[] data = encoder.encode(world.toBSONObject());
            try {
                FileUtils.writeByteArrayToFile(worldFile, data);
            } catch (IOException ex) {
                PoliticsPlugin.logger().log(Level.SEVERE, "Could not save universe file `" + fileName + "' due to error!", ex);
                continue;
            }
        }
    }

    /**
     * Creates a new GroupsWorld.
     * 
     * @param name
     * @return
     */
    private PoliticsWorld createWorld(String name) {
        PoliticsWorld world = new PoliticsWorld(name, getWorldConfig(name));
        worlds.put(name, world);
        return world;
    }
}
