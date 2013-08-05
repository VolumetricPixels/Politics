/*
 * This file is part of Politics.
 * 
 * Copyright (c) 2012-2012, VolumetricPixels <http://volumetricpixels.com/>
 * Politics is licensed under the Affero General Public License Version 3.
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.volumetricpixels.politics.group.level;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import org.spout.cereal.config.ConfigurationNode;

import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.group.privilege.Privilege;

/**
 * A role
 */
public final class Role implements Comparable<Role> {

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
    private final Set<Privilege> privileges;
    /**
     * The rank of the role.
     */
    private final int rank;

    /**
     * C'tor
     * 
     * @param id
     * @param name
     * @param privileges
     * @param rank
     */
    private Role(final String id, final String name, final Set<Privilege> privileges, final int rank) {
        this.id = id;
        this.name = name;
        this.privileges = privileges;
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
     * Checks if the role has the given privilege.
     * 
     * @param privilege
     * @return
     */
    public boolean hasPrivilege(final Privilege privilege) {
        return privileges.contains(privilege);
    }

    /**
     * Gets the set of privileges of this Role.
     * 
     * @return
     */
    public Set<Privilege> getPrivileges() {
        return new HashSet<Privilege>(privileges);
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
     * Gets the name of the role.
     * 
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Loads a role from a string id and node.
     * 
     * @param id
     * @param node
     * @return
     */
    public static Role load(final String id, final ConfigurationNode node) {
        final String name = node.getNode("name").getString(StringUtils.capitalize(id));
        final List<String> privs = node.getNode("privileges").getStringList(new ArrayList<String>());
        final Set<Privilege> privileges = new HashSet<Privilege>();
        for (final String priv : privs) {
            final Privilege p = Politics.getPrivilegeManager().getPrivilege(priv);
            if (p == null) {
                continue;
            }
        }
        final int rank = node.getNode("rank").getInt(1);
        return new Role(id, name, privileges, rank);
    }

    @Override
    public int compareTo(final Role other) {
        if (rank == other.getRank()) {
            return id.compareToIgnoreCase(other.getId());
        }
        return rank - other.getRank();
    }
}
