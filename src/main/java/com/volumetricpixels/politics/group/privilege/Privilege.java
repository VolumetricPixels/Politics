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
package com.volumetricpixels.politics.group.privilege;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents things one is allowed to do.
 */
public class Privilege {
    /**
     * The name of the privilege.
     */
    private final String name;

    /**
     * The bit that represents the privilege.
     */
    private final Set<PrivilegeType> types;

    /**
     * Private C'tor
     */
    public Privilege(String name, PrivilegeType... types) {
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
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the types of this privilege.
     * 
     * @return
     */
    public Set<PrivilegeType> getTypes() {
        return EnumSet.copyOf(types);
    }

    /**
     * Merges two sets of privileges together.
     * 
     * @param sets
     * @return
     */
    public static Set<Privilege> all(Set<Privilege>... sets) {
        Set<Privilege> result = new HashSet<Privilege>();
        for (Set<Privilege> set : sets) {
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
    public static Set<Privilege> common(Set<Privilege>... sets) {
        boolean first = true;
        Set<Privilege> result = new HashSet<Privilege>();
        for (Set<Privilege> set : sets) {
            if (first) {
                result.addAll(set);
                first = false;
                continue;
            }

            Set<Privilege> copy = new HashSet<Privilege>(result);
            for (Privilege privilege : copy) {
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
    public static Set<Privilege> filter(Set<Privilege> set, PrivilegeType... types) {
        Set<Privilege> result = new HashSet<Privilege>();
        Set<PrivilegeType> typesSet = EnumSet.of(types[0], types);
        for (Privilege priv : set) {
            if (priv.getTypes().containsAll(typesSet)) {
                result.add(priv);
            }
        }
        return result;
    }
}
