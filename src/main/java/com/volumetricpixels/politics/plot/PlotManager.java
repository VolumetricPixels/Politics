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
package com.volumetricpixels.politics.plot;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.apache.commons.io.FileUtils;
import org.bson.BSONDecoder;
import org.bson.BSONEncoder;
import org.bson.BSONObject;
import org.bson.BasicBSONDecoder;
import org.bson.BasicBSONEncoder;
import org.spout.api.geo.World;

import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.PoliticsPlugin;

/**
 * Manages plots.
 */
public class PlotManager {
	/**
	 * World names mapped to GroupWorlds.
	 */
	private Map<String, PoliticsWorld> worlds;

	/**
	 * Directory containing the folders.
	 */
	private final File worldsDir;

	/**
	 * C'tor
	 */
	public PlotManager() {
		worldsDir = new File(Politics.getPlugin().getDataFolder(), "data/worlds/");
	}

	/**
	 * Loads all GroupsWorlds.
	 */
	public void loadAll() {
		BSONDecoder decoder = new BasicBSONDecoder();
		worlds = new HashMap<String, PoliticsWorld>();

		worldsDir.mkdirs();
		for (File file : worldsDir.listFiles()) {
			String fileName = file.getName();
			if (!fileName.endsWith(".cow") || fileName.length() <= 4) {
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

			BSONObject object = decoder.readObject(data);
			PoliticsWorld world = PoliticsWorld.fromBSONObject(worldName, object);
			worlds.put(world.getName(), world);
		}
	}

	/**
	 * Saves all GroupsWorlds.
	 */
	public void saveAll() {
		BSONEncoder encoder = new BasicBSONEncoder();
		worldsDir.mkdirs();

		for (PoliticsWorld world : worlds.values()) {
			String fileName = world.getName() + ".cow";
			File worldFile = new File(worldsDir, fileName);

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
	 * Gets a GroupWorld from its name.
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
	 * Gets a GroupWorld from its World.
	 * 
	 * @param world
	 * @return
	 */
	public PoliticsWorld getWorld(World world) {
		return getWorld(world.getName());
	}

	/**
	 * Creates a new GroupsWorld.
	 * 
	 * @param name
	 * @return
	 */
	private PoliticsWorld createWorld(String name) {
		PoliticsWorld world = new PoliticsWorld(name);
		worlds.put(name, world);
		return world;
	}
}
