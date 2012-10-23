package com.volumetricpixels.politics.group.privilege;

/**
 * Contains privileges pertaining to groups and plots.
 */
public class GroupPlotPrivileges {
    /**
     * Allows the player to build.
     */
    public static final Privilege BUILD = new Privilege("BUILD", PrivilegeType.GROUP, PrivilegeType.PLOT);

    /**
     * Allows the player to interact.
     */
    public static final Privilege INTERACT = new Privilege("INTERACT", PrivilegeType.GROUP, PrivilegeType.PLOT);

}
