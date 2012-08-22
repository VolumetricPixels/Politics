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
import java.util.logging.Level;

import org.spout.api.util.config.Configuration;
import org.spout.api.util.config.ConfigurationNode;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.volumetricpixels.politics.PoliticsPlugin;
import com.volumetricpixels.politics.group.GroupLevel;
import com.volumetricpixels.politics.group.Privilege;

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
	 * The name of these UniverseRules
	 */
	private final String name;

	/**
	 * The GroupLevels apparent in this Universe.
	 */
	private final Map<String, GroupLevel> groupLevels;

	/**
	 * C'tor
	 */
	private UniverseRules(String name, Map<String, GroupLevel> groupLevels) {
		this.name = name;
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
			String id = level.getId();
			config.getNode("levels." + id + ".name").setValue(level.getName());
			config.getNode("levels." + id + ".rank").setValue(level.getRank());
			List<String> children = new ArrayList<String>();
			for (GroupLevel child : level.getAllowedChildren()) {
				children.add(child.getId());
			}
			config.getNode("levels." + id + ".children").setValue(children);
			config.getNode("levels." + id + ".plural").setValue(level.getPlural());

			for (Entry<String, Integer> role : level.getRoles().entrySet()) {
				String roleName = role.getKey();
				int value = role.getValue().intValue();
				Set<Privilege> privs = Privilege.getPrivileges(value);

				List<String> privNames = new ArrayList<String>();
				for (Privilege priv : privs) {
					privNames.add(priv.name());
				}

				config.getNode("levels." + id + ".roles." + roleName).setValue(privNames);
			}
		}
	}

	/**
	 * Loads a UniverseRules from the given config.
	 * 
	 * @param config
	 * @return
	 */
	public static UniverseRules load(String name, Configuration config) {
		Map<String, GroupLevel> levelMap = new HashMap<String, GroupLevel>();
		{ // Load levels

			// Get the levels turned into objects
			Map<GroupLevel, List<String>> levels = new HashMap<GroupLevel, List<String>>();

			ConfigurationNode levelNode = config.getNode("levels");
			for (Entry<String, ConfigurationNode> entry : levelNode.getChildren().entrySet()) {
				// Load id
				String id = entry.getKey();

				// Load name
				String levelName = entry.getValue().getNode("name").getString(id);

				// Load rank
				int rank = entry.getValue().getNode("rank").getInt();

				// Load children
				List<String> children = entry.getValue().getNode("children").getStringList();

				// Load roles
				Map<Integer, String> rolesMap = new HashMap<Integer, String>();
				for (Entry<String, ConfigurationNode> roleEntry : entry.getValue().getNode("roles").getChildren().entrySet()) {
					String roleName = roleEntry.getKey();
					List<String> privs = roleEntry.getValue().getStringList();
					int mask = 0x0;
					for (String priv : privs) {
						try {
							Privilege p = Privilege.valueOf(priv);
							mask &= p.getMask();
						} catch (IllegalArgumentException ex) {
							PoliticsPlugin.logger().log(Level.WARNING, "Unknown privilege '" + priv + "'. Not adding.");
						}
					}
					rolesMap.put(mask, roleName);
				}
				BiMap<Integer, String> realRolesMap = ImmutableBiMap.<Integer, String> builder().putAll(rolesMap).build();

				// Load plural
				String plural = entry.getValue().getNode("plural").getString(levelName + "s");

				// Create the level
				GroupLevel level = new GroupLevel(id, levelName, rank, realRolesMap, plural);
				levelMap.put(level.getId().toLowerCase(), level);
				levels.put(level, children);
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

		return new UniverseRules(name, levelMap);
	}
}
