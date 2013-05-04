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
package com.volumetricpixels.politics.group.privilege;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents things one is allowed to do
 */
public class Privilege {
    /**
     * The name of the privilege.
     */
    private final String name;
    /**
     * The types of the privilege.
     */
    private final Set<PrivilegeType> types;

    /**
     * C'tor
     * 
     * @param name
     *            The name of the privilege
     * @param types
     *            The types of the privilege
     */
    public Privilege(final String name, final PrivilegeType... types) {
        name.replaceAll(" ", "_");

        if (name.matches(":")) {
            throw new IllegalStateException("Colons not allowed in privilege names!");
        }
        this.name = name.toUpperCase();
        this.types = EnumSet.of(types[0], types);
    }

    /**
     * Gets the name of this privilege. This must be unique.
     * 
     * @return The name of this privilege
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the types of this privilege.
     * 
     * @return The types of this privilege
     */
    public Set<PrivilegeType> getTypes() {
        return EnumSet.copyOf(types);
    }

    /**
     * Merges two sets of privileges together.
     * 
     * @param sets
     *            The sets of privileges to merge
     * @return A Set containing all privileges in the given sets
     */
    @SuppressWarnings("unchecked")
    public static Set<Privilege> all(final Set<Privilege>... sets) {
        final Set<Privilege> result = new HashSet<Privilege>();
        for (final Set<Privilege> set : sets) {
            result.addAll(set);
        }
        return result;
    }

    /**
     * Gets the privileges found in all given sets.
     * 
     * @param sets
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Set<Privilege> common(final Set<Privilege>... sets) {
        boolean first = true;
        final Set<Privilege> result = new HashSet<Privilege>();
        for (final Set<Privilege> set : sets) {
            if (first) {
                result.addAll(set);
                first = false;
                continue;
            }

            final Set<Privilege> copy = new HashSet<Privilege>(result);
            for (final Privilege privilege : copy) {
                if (!set.contains(privilege)) {
                    result.remove(privilege);
                }
            }
        }
        return result;
    }

    /**
     * Filters the given set by the given privilege types.
     * 
     * @param set
     * @param types
     * @return
     */
    public static Set<Privilege> filter(final Set<Privilege> set, final PrivilegeType... types) {
        final Set<Privilege> result = new HashSet<Privilege>();
        final Set<PrivilegeType> typesSet = EnumSet.of(types[0], types);
        for (final Privilege priv : set) {
            if (priv.getTypes().containsAll(typesSet)) {
                result.add(priv);
            }
        }
        return result;
    }
}
