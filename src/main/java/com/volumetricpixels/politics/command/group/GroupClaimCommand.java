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
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;
import org.spout.api.geo.discrete.Point;

import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.group.Group;
import com.volumetricpixels.politics.group.GroupProperty;
import com.volumetricpixels.politics.group.level.GroupLevel;
import com.volumetricpixels.politics.group.privilege.GroupPrivileges;
import com.volumetricpixels.politics.util.MessageStyle;
import com.volumetricpixels.politics.world.Plot;

/**
 * Claims the plot you are in.
 */
public class GroupClaimCommand extends GroupCommand {
    /**
     * C'tor
     *
     * @param level The GroupLevel of this command
     */
    public GroupClaimCommand(GroupLevel level) {
        super(level, "claim");
    }

    @Override
    public void processCommand(CommandSource source, Command cmd, CommandContext context) throws CommandException {
        Group group = findGroup(source, cmd, context);

        if (!group.can(source, GroupPrivileges.CLAIM) && !hasAdmin(source)) {
            throw new CommandException("You don't have permissions to claim land in this " + level.getName() + ".");
        }

        // TODO add a way to get the world,x,y,z from the command line (should
        // be in GroupCommand)
        Point position = ((Player) source).getTransform().getPosition();
        if (!group.getUniverse().getWorlds().contains(Politics.getWorld(position.getWorld()))) {
            throw new CommandException("You can't create a plot for that group in this world.");
        }

        Plot plot = Politics.getPlotAt(position);
        if (plot.isOwner(group)) {
            throw new CommandException(group.getStringProperty(GroupProperty.NAME) + " already owns this plot.");
        }

        Group owner = plot.getOwner(group.getUniverse());
        if (owner != null) {
            throw new CommandException("Sorry, this plot is already owned by " + owner.getStringProperty(GroupProperty.NAME) + ".");
        }

        if (!plot.addOwner(group)) {
            throw new CommandException("You cannot claim this plot!");
        }

        source.sendMessage(MessageStyle.SUCCESS, "The plot was claimed successfully.");
    }

    @Override
    public void setupCommand(Command cmd) {
        cmd.setArgBounds(1, -1);
        cmd.setHelp("Claims land for your " + level.getName() + ".");
        cmd.setUsage("[-g " + level.getName() + "] [-u universe]");
    }
}
