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
package com.simplyian.colonies.universe;

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

import com.simplyian.colonies.ColoniesPlugin;
import com.simplyian.colonies.colony.ColonyLevel;
import com.simplyian.colonies.plot.ColonyWorld;

/**
 * Contains all universes.
 */
public class UniverseManager {
	/**
	 * Plugin instance
	 */
	private final ColoniesPlugin plugin;

	/**
	 * Universe directory
	 */
	private final File universeDir;

	/**
	 * Universes mapped to their names.
	 */
	private Map<String, Universe> universes;

	/**
	 * World names mapped to ColonyWorlds.
	 */
	private Map<String, ColonyWorld> worlds = new HashMap<String, ColonyWorld>();

	/**
	 * C'tor
	 * 
	 * @param plugin
	 */
	public UniverseManager(ColoniesPlugin plugin) {
		this.plugin = plugin;

		universeDir = new File(plugin.getDataFolder(), "data/universe/");
		universeDir.mkdirs();
	}

	/**
	 * Loads all universes into memory from files.
	 */
	public void loadAll() {
		BSONDecoder decoder = new BasicBSONDecoder();
		for (File file : universeDir.listFiles()) {
			String fileName = file.getName();
			if (!fileName.endsWith(".cou") || fileName.length() <= 4) {
				continue;
			}

			byte[] data;
			try {
				data = FileUtils.readFileToByteArray(file);
			} catch (IOException ex) {
				plugin.getLogger().log(Level.SEVERE, "Could not read universe file `" + fileName + "'!", ex);
				continue;
			}

			BSONObject object = decoder.readObject(data);

			Universe universe = Universe.fromBSONObject(object);
			universes.put(universe.getName(), universe);
		}
	}

	/**
	 * Saves all universes in memory to files.
	 */
	public void saveAll() {
		BSONEncoder encoder = new BasicBSONEncoder();
		universeDir.mkdirs();
		for (Universe universe : universes.values()) {
			String fileName = universe.getName() + ".cou";
			File universeFile = new File(universeDir, fileName);

			byte[] data = encoder.encode(universe.toBSONObject());
			try {
				FileUtils.writeByteArrayToFile(universeFile, data);
			} catch (IOException ex) {
				plugin.getLogger().log(Level.SEVERE, "Could not save universe file `" + fileName + "' due to error!", ex);
				continue;
			}
			// TODO make backups
		}
	}

	/**
	 * Gets a universe by its name.
	 * 
	 * @param name
	 * @return
	 */
	public Universe getUniverse(String name) {
		return universes.get(name.toLowerCase());
	}

	/**
	 * Gets the universe of the given CommandSource.
	 * 
	 * @param world
	 * @param level
	 * @return
	 */
	public Universe getUniverse(World world, ColonyLevel level) {
		ColonyWorld cw = getWorld(world);
		if (cw == null) {
			return null;
		}
		return cw.getUniverse(level);
	}

	/**
	 * Gets a ColonyWorld from its name.
	 * 
	 * @param name
	 * @return
	 */
	public ColonyWorld getWorld(String name) {
		return worlds.get(name);
	}

	/**
	 * Gets a ColonyWorld from its World.
	 * 
	 * @param world
	 * @return
	 */
	public ColonyWorld getWorld(World world) {
		return getWorld(world.getName());
	}

	public UniverseRules getRules(String rulesName) {
		// TODO Auto-generated method stub
		return null;
	}
}
