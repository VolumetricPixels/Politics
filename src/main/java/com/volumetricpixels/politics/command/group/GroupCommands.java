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
package com.volumetricpixels.politics.command.group;

import org.spout.api.command.Command;

import com.volumetricpixels.politics.command.Commands;
import com.volumetricpixels.politics.group.level.GroupLevel;

/**
 * Group commands.
 */
public class GroupCommands extends Commands {
    /**
     * The level of these GroupCommands.
     */
    private final GroupLevel level;

    /**
     * C'tor
     * 
     * @param level
     *            The GroupLevel of this command
     */
    public GroupCommands(final GroupLevel level) {
        super(level.getName().toLowerCase());
        this.level = level;
    }

    @Override
    public void setup(final Command cmd) {
        new GroupClaimCommand(level).register(cmd);
        new GroupCreateCommand(level).register(cmd);
        new GroupDemoteCommand(level).register(cmd);
        new GroupDestroyCommand(level).register(cmd);
        new GroupInfoCommand(level).register(cmd);
        new GroupListCommand(level).register(cmd);
        new GroupPromoteCommand(level).register(cmd);
        new GroupSetRoleCommand(level).register(cmd);
        new GroupSetSpawnCommand(level).register(cmd);
        new GroupSpawnCommand(level).register(cmd);
        new GroupUnclaimCommand(level).register(cmd);
    }
}
