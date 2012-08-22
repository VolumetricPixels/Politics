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
package com.volumetricpixels.politics.universe;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.PoliticsPlugin;
import com.volumetricpixels.politics.group.Group;
import com.volumetricpixels.politics.group.GroupLevel;
import com.volumetricpixels.politics.plot.PoliticsWorld;

/**
 * Contains all universes.
 */
public class UniverseManager {
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
	 * Stores groups.
	 */
	private TIntObjectMap<Group> groups;

	/**
	 * Worlds mapped to their levels.
	 */
	private Map<PoliticsWorld, Map<GroupLevel, Universe>> worldLevels;

	/**
	 * The next id to assign a Group.
	 */
	private int nextId = 0xffffffff;

	/**
	 * C'tor
	 * 
	 * @param plugin
	 */
	public UniverseManager() {
		rulesDir = new File(Politics.getPlugin().getDataFolder(), "rules/");
		universeDir = new File(Politics.getPlugin().getDataFolder(), "data/universe/");
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
			String name = fileName.substring(0, fileName.length() - 4);

			YamlConfiguration configFile = new YamlConfiguration(file);
			try {
				configFile.load();
			} catch (ConfigurationException ex) {
				PoliticsPlugin.logger().log(Level.SEVERE, "Invalid universe YAML file `" + fileName + "'!", ex);
			}

			UniverseRules thisRules = UniverseRules.load(name, configFile);
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
		groups = new TIntObjectHashMap<Group>();
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
				PoliticsPlugin.logger().log(Level.SEVERE, "Could not read universe file `" + fileName + "'!", ex);
				continue;
			}

			BSONObject object = decoder.readObject(data);

			Universe universe = Universe.fromBSONObject(object);
			universes.put(universe.getName(), universe);

			for (Group group : universe.getGroups()) {
				if (groups.put(group.getUid(), group) != null) {
					PoliticsPlugin.logger().log(Level.WARNING, "Duplicate group id " + group.getUid() + "!");
				}
				if (group.getUid() > nextId) {
					nextId = group.getUid();
				}
			}
		}

		// Populate World levels
		worldLevels = new HashMap<PoliticsWorld, Map<GroupLevel, Universe>>();
		for (Universe universe : universes.values()) {
			for (GroupLevel level : universe.getRules().getGroupLevels()) {
				for (PoliticsWorld world : universe.getWorlds()) {
					Map<GroupLevel, Universe> levelMap = worldLevels.get(world);
					if (levelMap == null) {
						levelMap = new HashMap<GroupLevel, Universe>();
						worldLevels.put(world, levelMap);
					}
					Universe prev = levelMap.put(level, universe);
					if (prev != null) {
						PoliticsPlugin.logger().log(Level.WARNING, "Multiple universes are conflicting on the same world! Universe name: " + universe.getName() + "; Rules name: " + universe.getRules().getName());
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
				PoliticsPlugin.logger().log(Level.SEVERE, "Could not save universe file `" + fileName + "' due to error!", ex);
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
	 * Gets a universe from its world and group level.
	 * 
	 * @param world
	 * @param level
	 * @return
	 */
	public Universe getUniverse(World world, GroupLevel level) {
		PoliticsWorld cw = Politics.getWorld(world);
		if (cw == null) {
			return null;
		}
		return getUniverse(cw, level);
	}

	/**
	 * Gets a list of all GroupLevels.
	 * 
	 * @return
	 */
	public List<GroupLevel> getGroupLevels() {
		List<GroupLevel> ret = new ArrayList<GroupLevel>();
		for (UniverseRules rules : this.rules.values()) {
			ret.addAll(rules.getGroupLevels());
		}
		return ret;
	}

	/**
	 * Gets a group by its id.
	 * 
	 * @param id
	 * @return
	 */
	public Group getGroupById(int id) {
		return groups.get(id);
	}

	/**
	 * Gets the universe of the given CommandSource.
	 * 
	 * @param world
	 * @param level
	 * @return
	 */
	public Universe getUniverse(PoliticsWorld world, GroupLevel level) {
		Map<GroupLevel, Universe> levelUniverses = worldLevels.get(world);
		if (levelUniverses == null) {
			return null;
		}
		return levelUniverses.get(world);
	}

	/**
	 * Gets the group levels in the given world.
	 * 
	 * @param world
	 * @return
	 */
	public List<GroupLevel> getLevelsOfWorld(PoliticsWorld world) {
		Map<GroupLevel, Universe> levelUniverses = worldLevels.get(world);
		if (levelUniverses == null) {
			return new ArrayList<GroupLevel>();
		}
		return new ArrayList<GroupLevel>(levelUniverses.keySet());
	}

	/**
	 * Creates a new universe with the given name.
	 * 
	 * @param name
	 * @param theRules
	 * @return the created universe
	 */
	public Universe createUniverse(String name, UniverseRules theRules) {
		Universe universe = new Universe(name, theRules);
		universes.put(name, universe);
		return universe;
	}

	/**
	 * Destroys the given universe.
	 * 
	 * @param universe
	 */
	public void destroyUniverse(Universe universe) {
		universes.remove(universe.getName());
		for (Group group : universe.getGroups()) {
			universe.destroyGroup(group);
		}
	}

	/**
	 * Gets the next ID to use for a group.
	 * 
	 * @return
	 */
	public int nextId() {
		while (getGroupById(nextId) != null) {
			nextId++;
		}
		return nextId;
	}
}
