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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.spout.cereal.config.ConfigurationNode;

/**
 * Represents a promotion track of roles
 */
public class RoleTrack implements Iterable<Role> {

    /**
     * The id of the track.
     */
    private final String id;
    /**
     * The roles of the track.
     */
    private final List<Role> roles;

    /**
     * C'tor
     * 
     * @param id
     * @param roles
     */
    RoleTrack(final String id, final List<Role> roles) {
        this.id = id;
        this.roles = roles;
    }

    /**
     * Gets the id of the track.
     * 
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the roles of the track.
     * 
     * @return
     */
    public List<Role> getRoles() {
        return new LinkedList<Role>(roles);
    }

    /**
     * Gets the role before the given role.
     * 
     * @param role
     * @return
     */
    public Role getPreviousRole(final Role role) {
        final int index = roles.indexOf(role);
        if (index < 0 || index <= 0) {
            return null;
        }
        return roles.get(index - 1);
    }

    /**
     * Gets the role after the given role.
     * 
     * @param role
     * @return
     */
    public Role getNextRole(final Role role) {
        final int index = roles.indexOf(role);
        if (index < 0 || index + 2 > roles.size()) {
            return null;
        }
        return roles.get(index + 1);
    }

    @Override
    public Iterator<Role> iterator() {
        return roles.listIterator();
    }

    /**
     * Loads a Track.
     * 
     * @param id
     * @param node
     * @param roles
     * @return
     */
    public static RoleTrack load(final String id, final ConfigurationNode node, final Map<String, Role> roles) {
        final List<String> rolesNames = node.getStringList(new LinkedList<String>());
        final List<Role> rolesList = new LinkedList<Role>();
        for (final String roleName : rolesNames) {
            final Role role = roles.get(roleName.toLowerCase());
            if (role == null) {
                throw new IllegalStateException("The role '" + roleName + "' does not exist.");
            }
            rolesList.add(role);
        }
        return new RoleTrack(id, rolesList);
    }
}
