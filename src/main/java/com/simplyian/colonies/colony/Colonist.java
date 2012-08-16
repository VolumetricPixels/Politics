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

import com.simplyian.colonies.universe.Universe;

/**
 * Represents a player.
 */
public class Colonist {
	private final Universe universe;
	private final Set<Colony> colonies;

	/**
	 * C'tor
	 * 
	 * @param universe
	 * @param colonies
	 *            The colonies of this colonist. This should be the only
	 *            reference.
	 */
	public Colonist(Set<Colony> colonies, Universe universe) {
		this.universe = universe;
		this.colonies = colonies;
	}
}
