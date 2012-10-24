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

import com.volumetricpixels.politics.Politics;

/**
 * Contains privileges pertaining to groups.
 */
public final class GroupPrivileges {
    /**
     * Allows the player to claim land.
     */
    public static final Privilege CLAIM = new Privilege("CLAIM", PrivilegeType.GROUP);

    /**
     * Allows the player to disband the group.
     */
    public static final Privilege DISBAND = new Privilege("DISBAND", PrivilegeType.GROUP);

    /**
     * Allows the player to view information about the group.
     */
    public static final Privilege INFO = new Privilege("INFO", PrivilegeType.GROUP);

    /**
     * Allows the player to leave the group.
     */
    public static final Privilege LEAVE = new Privilege("LEAVE", PrivilegeType.GROUP);

    /**
     * Allows the player to check who in the group is online.
     */
    public static final Privilege ONLINE = new Privilege("ONLINE", PrivilegeType.GROUP);

    /**
     * Allows the player to invite other players to the group.
     */
    public static final Privilege INVITE = new Privilege("INVITE", PrivilegeType.GROUP);

    /**
     * Allows the player to kick others from the group.
     */
    public static final Privilege KICK = new Privilege("KICK", PrivilegeType.GROUP);

    /**
     * Allows the player to alter the roles of other members.
     */
    public static final Privilege SET_ROLE = new Privilege("SET_ROLE", PrivilegeType.GROUP);

    /**
     * Allows the player to teleport to the spawn of the group.
     */
    public static final Privilege SPAWN = new Privilege("SPAWN", PrivilegeType.GROUP);

    /**
     * Allows the player to unclaim land.
     */
    public static final Privilege UNCLAIM = new Privilege("UNCLAIM", PrivilegeType.GROUP);

    /**
     * Allows the player to set the spawn of the group.
     */
    public static final Privilege SET_SPAWN = new Privilege("SET_SPAWN", PrivilegeType.GROUP);

    /**
     * Allows the player to spawn others to the group's spawn.
     */
    public static final Privilege SPAWN_OTHER = new Privilege("SPAWN_OTHER", PrivilegeType.GROUP);

    /**
     * Private C'tor
     */
    private GroupPrivileges() {
    }
}
