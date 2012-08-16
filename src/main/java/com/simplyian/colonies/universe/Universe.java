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

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.simplyian.colonies.ColoniesPlugin;
import com.simplyian.colonies.colony.Colonist;
import com.simplyian.colonies.colony.Colony;

/**
 * Represents a headless group of all colonies within its scope.
 */
public class Universe {
	/**
	 * The instance of ColoniesPlugin.
	 */
	private final ColoniesPlugin plugin;

	/**
	 * The colonies in this universe manager.
	 */
	private Set<Colony> colonies;

	/**
	 * Contains the immediate children of each colony.
	 */
	private Map<Colony, Set<Colony>> children = new HashMap<Colony, Set<Colony>>();

	/**
	 * Cache containing colonists.
	 */
	private LoadingCache<String, Colonist> colonistCache;

	/**
	 * C'tor
	 */
	public Universe(ColoniesPlugin plugin) {
		this.plugin = plugin;

		buildColonistCache();
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
					if (colony.isMember(name)) {
						myColonies.add(colony);
					}
				}
				return new Colonist(name, myColonies, Universe.this);
			}
		});
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
