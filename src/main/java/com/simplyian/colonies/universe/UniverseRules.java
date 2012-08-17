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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;

import org.spout.api.util.config.Configuration;
import org.spout.api.util.config.ConfigurationNode;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.simplyian.colonies.ColoniesPlugin;
import com.simplyian.colonies.colony.ColonyLevel;
import com.simplyian.colonies.colony.Privilege;

/**
 * Represents the sum of the rules of a given universe. In essence, it is a
 * configuration object.
 * 
 * <p>
 * "...the conclusion is not so much that the Universe is fine-tuned for fun
 * gameplay; rather it is fine-tuned for the building blocks and environments
 * that fun gameplay requires". - adapted from Paul Davies
 * </p>
 */
public class UniverseRules {
	/**
	 * The ColonyLevels apparent in this Universe.
	 */
	private final Map<String, ColonyLevel> colonyLevels;

	/**
	 * C'tor
	 */
	private UniverseRules(Map<String, ColonyLevel> colonyLevels) {
		this.colonyLevels = colonyLevels;
	}

	/**
	 * Gets the ColonyLevel with the given name.
	 * 
	 * @param name
	 * @return
	 */
	public ColonyLevel getColonyLevel(String name) {
		return colonyLevels.get(name.toLowerCase());
	}

	/**
	 * Loads a UniverseRules from the given config.
	 * 
	 * @param config
	 * @return
	 */
	public static UniverseRules load(Configuration config) {
		Map<String, ColonyLevel> levelMap = new HashMap<String, ColonyLevel>();
		{ // Load levels

			// Get the levels turned into objects
			Map<ColonyLevel, List<String>> levels = new HashMap<ColonyLevel, List<String>>();

			ConfigurationNode levelNode = config.getNode("levels");
			for (Entry<String, ConfigurationNode> entry : levelNode.getChildren().entrySet()) {
				String levelName = entry.getKey();

				int rank = entry.getValue().getNode("rank").getInt();
				List<String> children = entry.getValue().getNode("children").getStringList();

				Map<Integer, String> rolesMap = new HashMap<Integer, String>();
				for (Entry<String, ConfigurationNode> roleEntry : entry.getValue().getChildren().entrySet()) {
					String roleName = roleEntry.getKey();
					List<String> privs = roleEntry.getValue().getStringList();
					int mask = 0x0;
					for (String priv : privs) {
						try {
							Privilege p = Privilege.valueOf(priv);
							mask &= p.getId();
						} catch (IllegalArgumentException ex) {
							ColoniesPlugin.logger().log(Level.WARNING, "Unknown privilege '" + priv + "'. Not adding.");
						}
					}
					rolesMap.put(mask, roleName);
				}
				BiMap<Integer, String> realRolesMap = ImmutableBiMap.<Integer, String> builder().putAll(rolesMap).build();
				ColonyLevel level = new ColonyLevel(levelName, rank, realRolesMap);
				levelMap.put(levelName.toLowerCase(), level);
				levels.put(level, children);
			}

			// Turn these levels into only objects
			for (Entry<ColonyLevel, List<String>> levelEntry : levels.entrySet()) {
				Set<ColonyLevel> allowed = new HashSet<ColonyLevel>();
				for (String ln : levelEntry.getValue()) {
					ColonyLevel level = levelMap.get(ln.toLowerCase());
					allowed.add(level);
				}
				levelEntry.getKey().setAllowedChildren(allowed);
			}
		}

		return new UniverseRules(levelMap);
	}
}
