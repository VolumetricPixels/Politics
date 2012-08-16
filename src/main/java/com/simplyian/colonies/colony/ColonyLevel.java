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

import java.util.Set;

import com.google.common.collect.BiMap;

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
	private Set<ColonyLevel> allowedChildren;

	/**
	 * Contains the names of roles corresponding to the given privilege set.
	 */
	private final BiMap<Integer, String> roleNames;

	/**
	 * C'tor
	 * 
	 * @param name
	 * @param rank
	 */
	public ColonyLevel(String name, int rank, BiMap<Integer, String> roleNames) {
		this.name = name;
		this.rank = rank;
		this.roleNames = roleNames;
	}

	/**
	 * Sets the allowed children to the given set.
	 * 
	 * @param set
	 *            The set to use. This should be the only reference.
	 */
	public void setAllowedChildren(Set<ColonyLevel> set) {
		this.allowedChildren = set;
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
	 * Returns true if this level can have children of the given level.
	 * 
	 * @param level
	 * @return
	 */
	public boolean canBeChild(ColonyLevel level) {
		return allowedChildren.contains(level);
	}

	/**
	 * Gets the role name of the given privilege set.
	 * 
	 * @param privileges
	 * @return
	 */
	public String getRoleName(int privileges) {
		return roleNames.get(Integer.valueOf(privileges));
	}

	/**
	 * Gets the privileges of the given role name.
	 * 
	 * @param roleName
	 * @return
	 */
	public int getPrivileges(String roleName) {
		return roleNames.inverse().get(roleName).intValue();
	}
}
