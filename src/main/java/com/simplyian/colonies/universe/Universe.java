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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.spout.api.Server;
import org.spout.api.Spout;
import org.spout.api.command.Command;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.simplyian.colonies.ColoniesPlugin;
import com.simplyian.colonies.colony.Colonist;
import com.simplyian.colonies.colony.Colony;
import com.simplyian.colonies.colony.ColonyLevel;

/**
 * Represents a headless group of all colonies within its scope.
 */
public class Universe {
	/**
	 * The instance of ColoniesPlugin.
	 */
	private final ColoniesPlugin plugin;

	/**
	 * Contains the rules of this universe.
	 */
	private final UniverseRules rules;

	/**
	 * The colonies in this universe manager.
	 */
	private Set<Colony> colonies;

	/**
	 * Contains the immediate children of each colony.
	 */
	private Map<Colony, Set<Colony>> children;

	/**
	 * Colonies in the given levels.
	 */
	private Map<ColonyLevel, Set<Colony>> levels;

	/**
	 * Cache containing colonists.
	 */
	private LoadingCache<String, Colonist> colonistCache;

	/**
	 * C'tor
	 */
	public Universe(ColoniesPlugin plugin, UniverseRules properties) {
		this.plugin = plugin;
		this.rules = properties;

		buildColonistCache();
	}

	public void load() {
		// TODO major
		colonies = new HashSet<Colony>();
		children = new HashMap<Colony, Set<Colony>>();

		levels = new HashMap<ColonyLevel, Set<Colony>>();
		for (Colony colony : colonies) {
			Set<Colony> level = levels.get(colony.getLevel());
			if (level == null) {
				level = new HashSet<Colony>();
				levels.put(colony.getLevel(), level);
			}
			level.add(colony);
		}
	}

	/**
	 * Builds the colonist cache.
	 */
	private void buildColonistCache() {
		// Build cache
		CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder();

		builder.maximumSize(((Server) Spout.getEngine()).getMaxPlayers());
		builder.expireAfterAccess(10L, TimeUnit.MINUTES);

		colonistCache = builder.build(new CacheLoader<String, Colonist>() {
			@Override
			public Colonist load(String name) throws Exception {
				Set<Colony> myColonies = new HashSet<Colony>();
				for (Colony colony : colonies) {
					if (colony.isImmediateMember(name)) {
						myColonies.add(colony);
					}
				}
				return new Colonist(name, myColonies, Universe.this);
			}
		});
	}

	/**
	 * Gets the rules of this universe.
	 * 
	 * @return
	 */
	public UniverseRules getRules() {
		return rules;
	}

	/**
	 * Gets a list of all colonies in the universe.
	 * 
	 * @return
	 */
	public List<Colony> getColonies() {
		return new ArrayList<Colony>(colonies);
	}

	/**
	 * Gets a list of all colonies with the given level in this universe.
	 * 
	 * @param level
	 * @return
	 */
	public List<Colony> getColonies(ColonyLevel level) {
		return new ArrayList<Colony>(levels.get(level));
	}

	/**
	 * Gets the child colonies of the given colony.
	 * 
	 * @param colony
	 * @return
	 */
	public Set<Colony> getChildColonies(Colony colony) {
		Set<Colony> childs = children.get(colony);
		if (childs == null) {
			return new HashSet<Colony>();
		}
		return childs;
	}

	/**
	 * Adds the given child as a child for the given colony.
	 * 
	 * @param colony
	 * @param child
	 * @return True if the colony could be made a child
	 */
	public boolean addChildColony(Colony colony, Colony child) {
		if (!colony.getLevel().canBeChild(child.getLevel())) {
			return false;
		}

		Set<Colony> childs = children.get(colony);
		if (childs == null) {
			childs = new HashSet<Colony>();
		}
		childs.add(child);
		return true;
	}

	/**
	 * Removes the given child colony from the children of the given colony.
	 * 
	 * @param colony
	 * @param child
	 * @return True if the child was removed, false if the child was not a child
	 *         in the first place
	 */
	public boolean removeChildColony(Colony colony, Colony child) {
		Set<Colony> childs = children.get(colony);
		if (childs == null) {
			return false;
		}
		return childs.remove(child);
	}

	/**
	 * Gets the colonist corresponding with the given player name.
	 * 
	 * @param player
	 * @return
	 */
	public Colonist getColonist(String player) {
		try {
			return colonistCache.get(player);
		} catch (ExecutionException ex) {
			plugin.getLogger().log(Level.SEVERE, "Could not load a colonist! This is a PROBLEM!", ex);
			return null;
		}
	}
}
