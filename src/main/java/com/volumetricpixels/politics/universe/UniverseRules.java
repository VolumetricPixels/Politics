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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.spout.api.util.config.Configuration;
import org.spout.api.util.config.ConfigurationNode;

import com.volumetricpixels.politics.group.level.GroupLevel;

/**
 * Represents the sum of the rules of a given universe. In essence, it is a
 * configuration object.
 *
 * <p> "...the conclusion is not so much that the Universe is fine-tuned for fun
 * gameplay; rather it is fine-tuned for the building blocks and environments
 * that fun gameplay requires". - adapted from Paul Davies </p>
 */
public class UniverseRules {
	/**
	 * The name of these UniverseRules.
	 */
	private final String name;

	/**
	 * The description of these UniverseRules.
	 */
	private final String description;

	/**
	 * The GroupLevels apparent in this Universe.
	 */
	private final Map<String, GroupLevel> groupLevels;

	/**
	 * C'tor
	 */
	private UniverseRules(String name, String description, Map<String, GroupLevel> groupLevels) {
		this.name = name;
		this.description = description;
		this.groupLevels = groupLevels;
	}

	/**
	 * Gets the name of these UniverseRules.
	 *
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the description of these UniverseRules.
	 *
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Gets the group levels of these UniverseRules.
	 *
	 * @return
	 */
	public List<GroupLevel> getGroupLevels() {
		return new ArrayList<GroupLevel>(groupLevels.values());
	}

	/**
	 * Gets the GroupLevel with the given name.
	 *
	 * @param name
	 * @return
	 */
	public GroupLevel getGroupLevel(String name) {
		return groupLevels.get(name.toLowerCase());
	}

	/**
	 * Saves the UniverseRules to the given config.
	 *
	 * @param config
	 */
	public void save(Configuration config) {
		for (GroupLevel level : groupLevels.values()) {
			ConfigurationNode node = config.getNode("levels." + level.getId());
			level.save(node);
		}
	}

	/**
	 * Loads a UniverseRules from the given config.
	 *
	 * @param config
	 * @return
	 */
	public static UniverseRules load(String name, Configuration config) {
		String description = config.getChild("description").getString("No description given.");

		Map<String, GroupLevel> levelMap = new HashMap<String, GroupLevel>();
		{ // Load levels

			// Get the levels turned into objects
			Map<GroupLevel, List<String>> levels = new HashMap<GroupLevel, List<String>>();

			ConfigurationNode levelsNode = config.getNode("levels");
			for (Entry<String, ConfigurationNode> entry : levelsNode.getChildren().entrySet()) {
				GroupLevel level = GroupLevel.load(entry.getKey(), entry.getValue(), levels);
				levelMap.put(level.getId(), level);
			}

			// Turn these levels into only objects
			for (Entry<GroupLevel, List<String>> levelEntry : levels.entrySet()) {
				Set<GroupLevel> allowed = new HashSet<GroupLevel>();
				for (String ln : levelEntry.getValue()) {
					GroupLevel level = levelMap.get(ln.toLowerCase());
					allowed.add(level);
				}
				levelEntry.getKey().setAllowedChildren(allowed);
			}
		}

		return new UniverseRules(name, description, levelMap);
	}
}
