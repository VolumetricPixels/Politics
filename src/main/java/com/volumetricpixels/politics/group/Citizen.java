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
package com.volumetricpixels.politics.group;

import java.util.HashSet;
import java.util.Set;

import com.volumetricpixels.politics.universe.Universe;

/**
 * Represents a player.
 */
public class Citizen {
	/**
	 * The name of the citizen.
	 */
	private final String name;

	/**
	 * The groups this citizen is part of.
	 */
	private final Set<Group> groups;

	/**
	 * The universe this citizen is part of.
	 */
	private final Universe universe;

	/**
	 * C'tor
	 * 
	 * @param name
	 *            The name of the citizen.
	 * @param groups
	 *            The groups of this citizen. This should be the only
	 *            reference.
	 * @param universe
	 */
	public Citizen(String name, Set<Group> groups, Universe universe) {
		this.name = name;
		this.groups = groups;
		this.universe = universe;
	}

	/**
	 * Gets the name of this citizen.
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the groups this citizen is part of.
	 * 
	 * @return
	 */
	public Set<Group> getGroups() {
		return new HashSet<Group>(groups);
	}

	/**
	 * Gets the universe this Citizen is part of.
	 * 
	 * @return
	 */
	public Universe getUniverse() {
		return universe;
	}
}
