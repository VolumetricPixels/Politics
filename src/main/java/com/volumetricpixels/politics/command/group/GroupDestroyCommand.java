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
package com.volumetricpixels.politics.command.group;

import org.spout.api.command.Command;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.exception.CommandException;

import com.volumetricpixels.politics.universe.Universe;
import com.volumetricpixels.politics.util.MsgStyle;
import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.group.Group;
import com.volumetricpixels.politics.group.GroupProperty;
import com.volumetricpixels.politics.group.level.GroupLevel;
import com.volumetricpixels.politics.group.level.Privilege;

/**
 * Used to destroy (delete) a group
 */
public class GroupDestroyCommand extends GroupCommand {
    public GroupDestroyCommand(GroupLevel level) {
        super(level, "destroy");
    }

    @Override
    public void processCommand(CommandSource source, Command cmd, CommandContext context) throws CommandException {
        Universe universe = Politics.getUniverse(context.getFlagString('u'));
        if (universe == null) {
            source.sendMessage(MsgStyle.ERROR, "A universe with the name '" + context.getFlagString('u') + "' doesn't exist.");
            return;
        }

        Group group = universe.getFirstGroupByProperty(GroupProperty.NAME, context.getFlagString('g'));
        if (group == null) {
            source.sendMessage(MsgStyle.ERROR, "A " + level.getName() + " with the name '" + context.getFlagString('g') + " doesn't exist!");
        }

        if ((group.isMember(source.getName()) && group.getRole(source.getName()).hasPrivilege(Privilege.DISBAND))
                || source.hasPermission("politics.group.destroy")) {
            universe.destroyGroup(group);
        } else {
            source.sendMessage("You can't destroy the " + level.getName() + " '" + group.getProperty(GroupProperty.NAME) + "'!");
        }
    }

    @Override
    public String[] getPermissions() {
        /*
         * Permissions for this command must be handled manually as the command
         * will work even without the admin permission if the user is a high
         * enough rank within the group
         */
        return new String[0];
    }

    @Override
    public void setupCommand(Command cmd) {
        cmd.setArgBounds(1, -1);
        cmd.setHelp("Destroys your " + level.getName() + ".");
        cmd.setUsage("<template> [-f] [-n name]");
    }
}