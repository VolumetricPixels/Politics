package com.volumetricpixels.politics.group.level;

import com.volumetricpixels.politics.PoliticsPlugin;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import org.apache.commons.lang3.StringUtils;
import org.spout.api.util.config.ConfigurationNode;

/**
 * A role.
 */
public class Role implements Comparable<Role> {
	/**
	 * The string id of the role. All lowercase.
	 */
	private final String id;

	/**
	 * The name of the role.
	 */
	private final String name;

	/**
	 * The bitset of the role.
	 */
	private final int bitset;

	/**
	 * The rank of the role.
	 */
	private final int rank;

	/**
	 * C'tor
	 *
	 * @param id
	 * @param name
	 * @param bitset
	 * @param rank
	 */
	private Role(String id, String name, int bitset, int rank) {
		this.id = id;
		this.name = name;
		this.bitset = bitset;
		this.rank = rank;
	}

	/**
	 * Gets the ID of this Role.
	 *
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * Gets the Bitset of this Role.
	 *
	 * @return
	 */
	public int getBitset() {
		return bitset;
	}

	/**
	 * Gets the set of privileges of this Role.
	 *
	 * @return
	 */
	public Set<Privilege> getPrivileges() {
		return Privilege.getPrivileges(bitset);
	}

	/**
	 * Gets the rank of the role.
	 *
	 * @return
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * Loads a role from a string id and node.
	 *
	 * @param id
	 * @param node
	 * @return
	 */
	public static Role load(String id, ConfigurationNode node) {
		String name = node.getNode("name").getString(StringUtils.capitalize(id));
		List<String> privs = node.getNode("privileges").getStringList(new ArrayList<String>());
		int mask = 0x0;
		for (String priv : privs) {
			try {
				Privilege p = Privilege.valueOf(priv);
				mask &= p.getMask();
			} catch (IllegalArgumentException ex) {
				PoliticsPlugin.logger().log(Level.WARNING, "Unknown privilege '" + priv + "'. Not adding.");
			}
		}
		int rank = node.getNode("rank").getInt(1);
		return new Role(id, name, mask, rank);
	}

	@Override
	public int compareTo(Role other) {
		if (rank == other.getRank()) {
			return id.compareToIgnoreCase(other.getId());
		}
		return rank - other.getRank();
	}
}
