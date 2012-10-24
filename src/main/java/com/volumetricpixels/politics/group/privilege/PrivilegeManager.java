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

import java.util.Map;

/**
 * Manages the various different types of privileges.
 */
public class PrivilegeManager {
    /**
     * Contains all privileges mapped to their names.
     */
    private Map<String, Privilege> privileges;

    /**
     * C'tor
     */
    public PrivilegeManager() {
        loadDefaultPrivileges();
    }

    /**
     * Loads all default privileges.
     */
    private void loadDefaultPrivileges() {
        registerPrivileges(GroupPrivileges.ALL);
        registerPrivileges(GroupPlotPrivileges.ALL);
    }

    /**
     * Registers a privilege with this PrivilegeManager.
     *
     * @param privilege
     * @return True if a privilege was not displaced.
     */
    public boolean registerPrivilege(Privilege privilege) {
        return privileges.put(privilege.getName(), privilege) == null;
    }

    /**
     * Registers multiple privileges.
     *
     * @param privileges
     * @return
     */
    public boolean registerPrivileges(Privilege... privileges) {
        for (Privilege p : privileges) {
            if (!registerPrivilege(p)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets a privilege by its name.
     *
     * @param name
     * @return
     */
    public Privilege getPrivilege(String name) {
        return privileges.get(name.toUpperCase().replaceAll(" ", "_"));
    }
}
