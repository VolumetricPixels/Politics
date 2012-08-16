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

import com.simplyian.colonies.colony.Colony;

/**
 * Represents a headless group of all colonies within its scope.
 */
public class Universe {
	/**
	 * The colonies in this universe manager.
	 */
	private Set<Colony> colonies;

	/**
	 * Contains the immediate children of each colony.
	 */
	private Map<Colony, Set<Colony>> children = new HashMap<Colony, Set<Colony>>();

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
}
