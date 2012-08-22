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

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents things one is allowed to do.
 */
public enum Privilege {
	/**
	 * Allows the player to build.
	 */
	BUILD(0x1),

	/**
	 * Allows the player to claim land.
	 */
	CLAIM(0x2),

	/**
	 * Allows the player to disband the group.
	 */
	DISBAND(0x4),

	/**
	 * Allows the player to view information about the group.
	 */
	INFO(0x8),

	/**
	 * Allows the player to leave the group.
	 */
	LEAVE(0x10),

	/**
	 * Allows the player to check who in the group is online.
	 */
	ONLINE(0x20),

	/**
	 * Allows the player to add other players to the group.
	 */
	ADD(0x40),

	/**
	 * Allows the player to kick others from the group.
	 */
	KICK(0x80),

	/**
	 * Allows the player to alter the roles of other members.
	 */
	SET_ROLE(0x100),

	/**
	 * Allows the player to teleport to the spawn of the group.
	 */
	SPAWN(0x200),

	/**
	 * Allows the player to unclaim land.
	 */
	UNCLAIM(0x400),

	;

	/**
	 * The bit that represents the privilege.
	 */
	private int mask;

	/**
	 * Private C'tor
	 */
	private Privilege(int mask) {
		this.mask = mask;
	}

	/**
	 * Gets the ID of the privilege.
	 * 
	 * @return
	 */
	public int getMask() {
		return mask;
	}

	/**
	 * Returns true if the given bitset has this privilege.
	 * 
	 * @param bitset
	 * @return
	 */
	public boolean hasPrivilege(int bitset) {
		return (mask & bitset) == mask;
	}

	/**
	 * Map of all privileges.
	 */
	private static TIntObjectMap<Privilege> privs = new TIntObjectHashMap<Privilege>();

	/**
	 * Gets a privilege by its id.
	 * 
	 * @param id
	 * @return
	 */
	public static Privilege getPrivilege(int id) {
		return privs.get(id);
	}

	/**
	 * Gets a set of all privileges the bitset contains.
	 * 
	 * @param bitset
	 * @return
	 */
	public static Set<Privilege> getPrivileges(int bitset) {
		Set<Privilege> privs = new HashSet<Privilege>();
		for (Privilege priv : values()) {
			if (priv.hasPrivilege(bitset)) {
				privs.add(priv);
			}
		}
		return privs;
	}
}
