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
package com.volumetricpixels.politics.group.level;

import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.group.privilege.Privilege;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.spout.api.util.config.ConfigurationNode;

import org.apache.commons.lang3.StringUtils;

import com.volumetricpixels.politics.PoliticsPlugin;
import java.util.HashSet;

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
    private Role(String id, String name, Set<Privilege> privileges, int rank) {
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
    public boolean hasPrivilege(Privilege privilege) {
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
    public static Role load(String id, ConfigurationNode node) {
        String name = node.getNode("name").getString(StringUtils.capitalize(id));
        List<String> privs = node.getNode("privileges").getStringList(new ArrayList<String>());
        Set<Privilege> privileges = new HashSet<Privilege>();
        for (String priv : privs) {
            Privilege p = Politics.getPrivilegeManager().getPrivilege(name);
            if (p == null) {
                continue;
            }
        }
        int rank = node.getNode("rank").getInt(1);
        return new Role(id, name, privileges, rank);
    }

    @Override
    public int compareTo(Role other) {
        if (rank == other.getRank()) {
            return id.compareToIgnoreCase(other.getId());
        }
        return rank - other.getRank();
    }
}
