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
