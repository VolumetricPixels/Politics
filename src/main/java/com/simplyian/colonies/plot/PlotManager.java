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

import com.simplyian.colonies.Colonies;
import com.simplyian.colonies.ColoniesPlugin;

/**
 * Manages plots.
 */
public class PlotManager {
	/**
	 * World names mapped to ColonyWorlds.
	 */
	private Map<String, ColoniesWorld> worlds;

	/**
	 * Directory containing the folders.
	 */
	private final File worldsDir;

	/**
	 * C'tor
	 */
	public PlotManager() {
		worldsDir = new File(Colonies.getPlugin().getDataFolder(), "data/worlds/");
	}

	/**
	 * Loads all ColoniesWorlds.
	 */
	public void loadAll() {
		BSONDecoder decoder = new BasicBSONDecoder();
		worlds = new HashMap<String, ColoniesWorld>();

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
				ColoniesPlugin.logger().log(Level.SEVERE, "Could not read world file `" + fileName + "'!", ex);
				continue;
			}

			BSONObject object = decoder.readObject(data);
			ColoniesWorld world = ColoniesWorld.fromBSONObject(worldName, object);
			worlds.put(world.getName(), world);
		}
	}

	/**
	 * Saves all ColoniesWorlds.
	 */
	public void saveAll() {
		BSONEncoder encoder = new BasicBSONEncoder();
		worldsDir.mkdirs();

		for (ColoniesWorld world : worlds.values()) {
			String fileName = world.getName() + ".cow";
			File universeFile = new File(worldsDir, fileName);

			byte[] data = encoder.encode(world.toBSONObject());
			try {
				FileUtils.writeByteArrayToFile(universeFile, data);
			} catch (IOException ex) {
				ColoniesPlugin.logger().log(Level.SEVERE, "Could not save universe file `" + fileName + "' due to error!", ex);
				continue;
			}
		}
	}

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
