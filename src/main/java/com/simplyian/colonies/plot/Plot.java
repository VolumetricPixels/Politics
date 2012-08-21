/*
 * This file is part of Colonies.
 *
 * Copyright (c) 2012-2012, THEDevTeam <http://thedevteam.org/>
 * Colonies is licensed under the Apache License Version 2.
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
package com.simplyian.colonies.plot;

import gnu.trove.list.TLongList;

import java.util.List;

import com.simplyian.colonies.colony.Colony;

/**
 * ColoniesWorld wrapper.
 */
public class Plot {
	/**
	 * World of the plot.
	 */
	private final ColoniesWorld world;

	/**
	 * X of the plot.
	 */
	private final int x;

	/**
	 * Y of the plot.
	 */
	private final int y;

	/**
	 * Z of the plot.
	 */
	private final int z;

	/**
	 * C'tor
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 */
	Plot(ColoniesWorld world, int x, int y, int z) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * @return the world
	 */
	public ColoniesWorld getWorld() {
		return world;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @return the z
	 */
	public int getZ() {
		return z;
	}

	/**
	 * Gets the IDs of the owners of the plot.
	 * 
	 * @return
	 */
	public TLongList getOwnerIds() {
		return world.getOwnerIds(x, y, z);
	}

	/**
	 * Gets the list of the owners of the plot.
	 * 
	 * @return
	 */
	public List<Colony> getOwners() {
		return world.getOwners(x, y, z);
	}
}
