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
package com.simplyian.colonies.colony;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a level of organization of a colony.
 */
public final class ColonyLevel {
	/**
	 * The name of the ColonyLevel.
	 */
	private final String name;

	/**
	 * The rank of this ColonyLevel. Smaller means lower. For example, in Towny,
	 * a Nation would have a higher rank than a Town.
	 */
	private final int rank;

	/**
	 * Contains the allowed children for this ColonyLevel.
	 */
	private final Set<ColonyLevel> allowedChildren = new HashSet<ColonyLevel>();

	/**
	 * C'tor
	 * 
	 * @param name
	 * @param rank
	 */
	public ColonyLevel(String name, int rank) {
		this.name = name;
		this.rank = rank;
	}

	/**
	 * Gets the name of this ColonyLevel.
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the rank of this ColonyLevel.
	 * 
	 * @return
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * Adds the given levels as allowed children.
	 * 
	 * @param levels
	 *            The levels to add.
	 */
	public void addAllowedChildren(ColonyLevel... levels) {
		allowedChildren.addAll(Arrays.asList(levels));
	}

	/**
	 * Returns true if this level can have children of the given level.
	 * 
	 * @param level
	 * @return
	 */
	public boolean canBeChild(ColonyLevel level) {
		return allowedChildren.contains(level);
	}
}
