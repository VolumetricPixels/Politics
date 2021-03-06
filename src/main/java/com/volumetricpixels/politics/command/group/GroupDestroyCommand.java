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
import org.spout.api.command.CommandArguments;
import org.spout.api.command.CommandSource;
import org.spout.api.exception.CommandException;

import com.volumetricpixels.politics.group.Group;
import com.volumetricpixels.politics.group.GroupProperty;
import com.volumetricpixels.politics.group.level.GroupLevel;
import com.volumetricpixels.politics.group.privilege.GroupPrivileges;

/**
 * Used to destroy (delete) a group
 */
public class GroupDestroyCommand extends GroupCommand {
    /**
     * C'tor
     * 
     * @param level
     *            The GroupLevel of this command
     */
    public GroupDestroyCommand(final GroupLevel level) {
        super(level, "destroy");
    }

    @Override
    public void execute(final CommandSource source, final Command cmd, final CommandArguments context) throws CommandException {
        final Group group = findGroup(source, cmd, context);

        if (!group.can(source, GroupPrivileges.DISBAND) && !hasAdmin(source)) {
            throw new CommandException("You aren't allowed to disband this group!");
        }

        group.getUniverse().destroyGroup(group);
        source.sendMessage("The group " + group.getStringProperty(GroupProperty.NAME) + " has been disbanded!");
    }

    @Override
    public void setupCommand(final Command cmd) {
        cmd.setHelp("Destroys your " + level.getName() + ".");
        cmd.setUsage("<template> [-f] [-n name]");
    }
}