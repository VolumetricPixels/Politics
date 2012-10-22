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

import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.Command;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;

import com.volumetricpixels.politics.group.Citizen;
import com.volumetricpixels.politics.group.Group;
import com.volumetricpixels.politics.group.GroupProperty;
import com.volumetricpixels.politics.group.level.GroupLevel;

/**
 * Used to get info about a group
 */
public class GroupInfoCommand extends GroupCommand {
    /**
     * C'tor
     * 
     * @param level
     *            The GroupLevel of this command
     */
    public GroupInfoCommand(GroupLevel level) {
        super(level, "info");
    }

    @Override
    public void processCommand(CommandSource source, Command cmd, CommandContext context) throws CommandException {
        if (!(source instanceof Player)) {
            throw new CommandException("Consoles aren't part of a group");
        }

        Player p = (Player) source;
        Citizen citizen = getCitizen(p);
        if (citizen == null) {
            throw new CommandException("You can't use this command in this world.");
        }

        Group group = citizen.getGroup(level);
        if (group == null) {
            throw new CommandException("You aren't in a " + level.getName() + ".");
        }

        p.sendMessage(ChatStyle.BLUE, "============= INFO =============");
        p.sendMessage(ChatStyle.DARK_GREEN, "Current Group: " + group.getStringProperty(GroupProperty.NAME));
        p.sendMessage(ChatStyle.BLUE, "================================");
    }

    @Override
    public void setupCommand(Command cmd) {
        cmd.setHelp("Gets information about the current group you are in.");
    }
}
