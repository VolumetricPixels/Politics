/*
 * This file is part of Colonies.
 *
 * Copyright (c) 2012-2012, THEDevTeam <http://thedevteam.org/>
 * Colonies is licensed under the Apache License Version 2.
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

import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TLongObjectHashMap;

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
import org.spout.api.exception.ConfigurationException;
import org.spout.api.geo.World;
import org.spout.api.util.config.yaml.YamlConfiguration;

import com.simplyian.colonies.Colonies;
import com.simplyian.colonies.ColoniesPlugin;
import com.simplyian.colonies.colony.Colony;
import com.simplyian.colonies.colony.ColonyLevel;
import com.simplyian.colonies.plot.ColoniesWorld;

/**
 * Contains all universes.
 */
public class UniverseManager {
	/**
	 * Plugin instance
	 */
	private final ColoniesPlugin plugin;

	/**
	 * Rules directory;
	 */
	private final File rulesDir;

	/**
	 * Universe directory
	 */
	private final File universeDir;

	/**
	 * The rules of the universe.
	 */
	private Map<String, UniverseRules> rules;

	/**
	 * Universes mapped to their names.
	 */
	private Map<String, Universe> universes;

	/**
	 * Stores colonies.
	 */
	private TLongObjectMap<Colony> colonies;

	/**
	 * Worlds mapped to their levels.
	 */
	private Map<ColoniesWorld, Map<ColonyLevel, Universe>> worldLevels;

	/**
	 * C'tor
	 * 
	 * @param plugin
	 */
	public UniverseManager(ColoniesPlugin plugin) {
		this.plugin = plugin;

		rulesDir = new File(plugin.getDataFolder(), "rules/");
		universeDir = new File(plugin.getDataFolder(), "data/universe/");
	}

	/**
	 * Loads the rules into memory.
	 */
	public void loadRules() {
		rulesDir.mkdirs();
		rules = new HashMap<String, UniverseRules>();
		for (File file : rulesDir.listFiles()) {
			String fileName = file.getName();
			if (!fileName.endsWith(".yml") || fileName.length() <= 4) {
				continue;
			}

			YamlConfiguration configFile = new YamlConfiguration(file);
			try {
				configFile.load();
			} catch (ConfigurationException ex) {
				plugin.getLogger().log(Level.SEVERE, "Invalid universe YAML file `" + fileName + "'!", ex);
			}

			UniverseRules thisRules = UniverseRules.load(configFile);
			String ruleName = thisRules.getName();
			rules.put(ruleName.toLowerCase(), thisRules);
		}
	}

	/**
	 * Loads all universes into memory from files.
	 */
	public void loadUniverses() {
		BSONDecoder decoder = new BasicBSONDecoder();
		universes = new HashMap<String, Universe>();
		colonies = new TLongObjectHashMap<Colony>();
		universeDir.mkdirs();
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

			for (Colony colony : universe.getColonies()) {
				if (colonies.put(colony.getUid(), colony) != null) {
					ColoniesPlugin.logger().log(Level.WARNING, "Duplicate colony id " + colony.getUid() + "!");
				}
			}
		}

		// Populate World levels
		worldLevels = new HashMap<ColoniesWorld, Map<ColonyLevel, Universe>>();
		for (Universe universe : universes.values()) {
			for (ColonyLevel level : universe.getRules().getColonyLevels()) {
				for (ColoniesWorld world : universe.getWorlds()) {
					Map<ColonyLevel, Universe> levelMap = worldLevels.get(world);
					if (levelMap == null) {
						levelMap = new HashMap<ColonyLevel, Universe>();
						worldLevels.put(world, levelMap);
					}
					Universe prev = levelMap.put(level, universe);
					if (prev != null) {
						plugin.getLogger().log(Level.WARNING, "Multiple universes are conflicting on the same world! Universe name: " + universe.getName() + "; Rules name: " + universe.getRules().getName());
					}
				}
			}
		}
	}

	/**
	 * Saves all universes in memory to files.
	 */
	public void saveUniverses() {
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
	 * Gets the rules with the corresponding name.
	 * 
	 * @param rulesName
	 * @return
	 */
	public UniverseRules getRules(String rulesName) {
		return rules.get(rulesName);
	}

	/**
	 * Gets a universe from its world and colony level.
	 * 
	 * @param world
	 * @param level
	 * @return
	 */
	public Universe getUniverse(World world, ColonyLevel level) {
		ColoniesWorld cw = Colonies.getWorld(world);
		if (cw == null) {
			return null;
		}
		return getUniverse(cw, level);
	}

	/**
	 * Gets a colony by its id.
	 * 
	 * @param id
	 * @return
	 */
	public Colony getColonyById(long id) {
		return colonies.get(id);
	}

	/**
	 * Gets the universe of the given CommandSource.
	 * 
	 * @param world
	 * @param level
	 * @return
	 */
	public Universe getUniverse(ColoniesWorld world, ColonyLevel level) {
		Map<ColonyLevel, Universe> levelUniverses = worldLevels.get(world);
		if (levelUniverses == null) {
			return null;
		}
		return levelUniverses.get(world);
	}
}
