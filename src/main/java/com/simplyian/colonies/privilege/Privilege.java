package com.simplyian.colonies.privilege;

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
	 * Allows the player to disband the colony.
	 */
	DISBAND(0x4);

	/**
	 * The bit that represents the privilege.
	 */
	private int id;

	/**
	 * Private C'tor
	 */
	private Privilege(int id) {
		this.id = id;
	}

	/**
	 * Gets the ID of the privilege.
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns true if the given bitset has this privilege.
	 * 
	 * @param bitset
	 * @return
	 */
	public boolean hasPrivilege(int bitset) {
		return (id & bitset) == id;
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
