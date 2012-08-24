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
	 * The universe this citizen is part of.
	 */
	private final Universe universe;

	/**
	 * C'tor
	 * 
	 * @param name
	 *            The name of the citizen.
	 * @param groups
	 *            The groups of this citizen. This should be the only reference.
	 * @param universe
	 */
	public Citizen(String name, Universe universe) {
		this.name = name;
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
		return universe.getCitizenGroups(name);
	}

	/**
	 * Gets the groups of the given level this citizen is part of.
	 * 
	 * @param level
	 * @return
	 */
	public Set<Group> getGroups(GroupLevel level) {
		Set<Group> groups = new HashSet<Group>();
		for (Group group : getGroups()) {
			if (group.getLevel().equals(level)) {
				groups.add(group);
			}
		}
		return groups;
	}

	/**
	 * Gets the universe this Citizen is part of.
	 * 
	 * @return
	 */
	public Universe getUniverse() {
		return universe;
	}

	/**
	 * Invalidates this Citizen's groups. This should happen when this Citizen
	 * is added to or removed from a group.
	 */
	public void invalidateGroups() {
		universe.invalidateCitizenGroups(name);
	}
}
