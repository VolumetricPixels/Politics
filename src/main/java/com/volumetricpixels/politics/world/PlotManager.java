/*
 * This file is part of Politics.
 * 
 * Copyright (c) 2012-2012, VolumetricPixels <http://volumetricpixels.com/>
 * Politics is licensed under the Affero General Public License Version 3.
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.volumetricpixels.politics.world;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.discrete.Point;

import org.apache.commons.io.FileUtils;

import org.bson.BSONEncoder;
import org.bson.BSONObject;
import org.bson.BasicBSONDecoder;
import org.bson.BasicBSONEncoder;

import org.spout.cereal.config.Configuration;
import org.spout.cereal.config.ConfigurationException;
import org.spout.cereal.config.yaml.YamlConfiguration;

import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.PoliticsPlugin;
import com.volumetricpixels.politics.util.DataUtils;

/**
 * Manages plots
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
     * Loads all World configurations.
     */
    public void loadWorldConfigs() {
        Politics.getFileSystem().getWorldConfigDir().mkdirs();
        configs = new HashMap<String, WorldConfig>();
        for (final File file : Politics.getFileSystem().getWorldConfigDir().listFiles()) {
            final String fileName = file.getName();
            if (!fileName.endsWith(".yml") || fileName.length() <= 4) {
                continue;
            }
            final String name = fileName.substring(0, fileName.length() - 4);

            final Configuration config = new YamlConfiguration(file);
            final WorldConfig wc = WorldConfig.load(name, config);
            configs.put(name, wc);
        }
    }

    /**
     * Loads all GroupsWorlds.
     */
    public void loadWorlds() {
        final BasicBSONDecoder decoder = new BasicBSONDecoder();
        worlds = new HashMap<String, PoliticsWorld>();

        Politics.getFileSystem().getWorldsDir().mkdirs();
        for (final File file : Politics.getFileSystem().getWorldsDir().listFiles()) {
            final String fileName = file.getName();
            if (!fileName.endsWith(".ptw") || fileName.length() <= 4) {
                continue;
            }
            final String worldName = fileName.substring(0, fileName.length() - 4);

            byte[] data;
            try {
                data = FileUtils.readFileToByteArray(file);
            } catch (final IOException ex) {
                PoliticsPlugin.logger().log(Level.SEVERE, "Could not read world file `" + fileName + "'!", ex);
                continue;
            }

            final WorldConfig config = getWorldConfig(worldName);
            final BSONObject object = decoder.readObject(data);
            final PoliticsWorld world = new PoliticsWorld(worldName, config, DataUtils.toBasicBSONObject(object));
            worlds.put(world.getName(), world);
        }
    }

    /**
     * Saves all GroupsWorlds.
     */
    public void saveWorlds() {
        final BSONEncoder encoder = new BasicBSONEncoder();
        Politics.getFileSystem().getWorldsDir().mkdirs();

        for (final PoliticsWorld world : worlds.values()) {
            if (!world.canStore()) {
                continue;
            }
            final String fileName = world.getName() + ".ptw";
            final File worldFile = new File(Politics.getFileSystem().getWorldsDir(), fileName);

            final byte[] data = encoder.encode(world.toBSONObject());
            try {
                FileUtils.writeByteArrayToFile(worldFile, data);
            } catch (final IOException ex) {
                PoliticsPlugin.logger().log(Level.SEVERE, "Could not save universe file `" + fileName + "' due to error!", ex);
                continue;
            }
        }
    }

    /**
     * Gets the WorldConfig of the given world name.
     * 
     * @param name
     * @return
     */
    public WorldConfig getWorldConfig(final String name) {
        WorldConfig conf = configs.get(name);
        if (conf == null) {
            conf = new WorldConfig(name);
            Politics.getFileSystem().getWorldConfigDir().mkdirs();
            final File toSave = new File(Politics.getFileSystem().getWorldConfigDir(), name + ".yml");
            final Configuration tc = new YamlConfiguration(toSave);
            conf.save(tc);
            try {
                tc.save();
            } catch (final ConfigurationException e) {
                PoliticsPlugin.logger().log(Level.SEVERE, "Could not write a world config file!", e);
            }
            configs.put(name, conf);
        }
        return conf;
    }

    /**
     * Gets a PoliticsWorld from its name.
     * 
     * @param name
     * @return
     */
    public PoliticsWorld getWorld(final String name) {
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
    public PoliticsWorld getWorld(final World world) {
        return getWorld(world.getName());
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
    public Plot getPlotAtPosition(final String world, final int x, final int y, final int z) {
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
    public Plot getPlotAtPosition(final World world, final int x, final int y, final int z) {
        return getWorld(world).getPlotAtChunkPosition(x, y, z);
    }

    /**
     * Gets the plot corresponding with the given Chunk.
     * 
     * @param chunk
     * @return
     */
    public ChunkPlot getPlotAtChunk(final Chunk chunk) {
        return getWorld(chunk.getWorld()).getPlotAtChunkPosition(chunk.getX(), chunk.getY(), chunk.getZ());
    }

    /**
     * Gets the plot at the given position.
     * 
     * @param position
     * @return
     */
    public Plot getPlotAt(final Point position) {
        return getPlotAtPosition(position.getWorld(), position.getChunkX(), position.getChunkY(), position.getChunkZ());
    }

    /**
     * Creates a new GroupsWorld.
     * 
     * @param name
     * @return
     */
    private PoliticsWorld createWorld(final String name) {
        final PoliticsWorld world = new PoliticsWorld(name, getWorldConfig(name));
        worlds.put(name, world);
        return world;
    }
}
