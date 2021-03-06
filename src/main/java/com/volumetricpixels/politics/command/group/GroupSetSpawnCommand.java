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
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;
import org.spout.api.geo.discrete.Transform;

import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.group.Group;
import com.volumetricpixels.politics.group.GroupProperty;
import com.volumetricpixels.politics.group.level.GroupLevel;
import com.volumetricpixels.politics.group.privilege.GroupPrivileges;
import com.volumetricpixels.politics.world.Plot;

/**
 * Claims the plot you are in
 */
public class GroupSetSpawnCommand extends GroupCommand {
    /**
     * C'tor
     * 
     * @param level
     *            The GroupLevel of this command
     */
    public GroupSetSpawnCommand(final GroupLevel level) {
        super(level, "setspawn");
    }

    @Override
    public void execute(final CommandSource source, final Command cmd, final CommandArguments context) throws CommandException {
        final Group group = findGroup(source, cmd, context);

        if (!group.can(source, GroupPrivileges.SET_SPAWN) && !hasAdmin(source)) {
            throw new CommandException("You don't have permissions to set the spawn of your " + level.getName() + "!");
        }

        final Transform transform = ((Player) source).getPhysics().getTransform();

        final Plot plot = Politics.getPlotAt(transform.getPosition());
        if (plot == null) {
            throw new CommandException("There is no plot here!");
        }
        if (!plot.isOwner(group)) {
            throw new CommandException("Sorry, the plot you are in must be owned by " + group.getStringProperty(GroupProperty.NAME)
                    + " to set your spawn in it!");
        }

        group.setProperty(GroupProperty.SPAWN, transform);
        source.sendMessage("The spawn of your " + level.getName() + " was set successfully!");
    }

    @Override
    public void setupCommand(final Command cmd) {
        cmd.setHelp("Sets the spawn of your " + level.getName() + ".");
        cmd.setUsage("[-p player] [-g " + level.getName() + "] [-u universe]");
    }
}
