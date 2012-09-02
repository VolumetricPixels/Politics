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

import com.volumetricpixels.politics.MsgStyle;
import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.event.PoliticsEventFactory;
import com.volumetricpixels.politics.event.plot.PlotOwnerChangeEvent;
import com.volumetricpixels.politics.group.Group;
import com.volumetricpixels.politics.group.GroupProperty;
import com.volumetricpixels.politics.group.level.GroupLevel;
import com.volumetricpixels.politics.group.level.Privilege;
import com.volumetricpixels.politics.plot.Plot;

import org.spout.api.command.Command;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;
import org.spout.api.geo.discrete.Point;

/**
 * Claims the plot you are in.
 */
public class GroupClaimCommand extends GroupCommand {
    /**
     * C'tor
     *
     * @param level
     */
    public GroupClaimCommand(GroupLevel level) {
        super(level, "claim");
    }

    @Override
    public void processCommand(CommandSource source, Command cmd, CommandContext context) throws CommandException {
        Group group = findGroup(source, cmd, context);
        if (group == null) {
            return;
        }

        if (!group.getRole(source.getName()).hasPrivilege(Privilege.CLAIM) && !source.hasPermission("politics.admin.group." + level.getId() + ".claim")) {
            source.sendMessage(MsgStyle.ERROR, "You don't have permissions to claim land in this " + level.getName() + ".");
            return;
        }

        // TODO add a way to get the world,x,y,z from the command line (should be in GroupCommand)
        Point position = ((Player) source).getPosition();

        Plot plot = Politics.getPlotAt(position);
        Group owner = plot.getOwner(level);
        if (owner != null) {
            source.sendMessage(MsgStyle.ERROR, "Sorry, this plot is already owned by " + owner.getStringProperty(GroupProperty.NAME) + ".");
            return;
        }

        if (!plot.addOwner(group)) {
            source.sendMessage(MsgStyle.ERROR, "The plot could not be claimed.");
            return;
        }
        source.sendMessage(MsgStyle.SUCCESS, "The plot was claimed successfully.");
    }

    @Override
    public void setupCommand(Command cmd) {
        cmd.setArgBounds(1, -1);
        cmd.setHelp("Claims land for your " + level.getName() + ".");
        cmd.setUsage("[-g " + level.getName() + "] [-u universe]");
        cmd.setPermissions(true, "politics.group." + level.getId() + ".claim");
    }
}
