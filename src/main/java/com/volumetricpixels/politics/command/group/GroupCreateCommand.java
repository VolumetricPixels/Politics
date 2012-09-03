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
import com.volumetricpixels.politics.api.Politics;
import com.volumetricpixels.politics.event.PoliticsEventFactory;
import com.volumetricpixels.politics.group.Group;
import com.volumetricpixels.politics.group.GroupProperty;
import com.volumetricpixels.politics.group.level.GroupLevel;
import com.volumetricpixels.politics.universe.Universe;

import org.spout.api.command.Command;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;

public class GroupCreateCommand extends GroupCommand {
    /**
     * C'tor
     * 
     * @param level 
     */
    public GroupCreateCommand(GroupLevel level) {
        super(level, "create");
    }

    @Override
    public void processCommand(CommandSource source, Command cmd, CommandContext context) throws CommandException {
        // Get le founder
        String founderName = null;
        if (source instanceof Player) {
            founderName = source.getName();
        }
        if (source.hasPermission("politics.admin.group." + level.getId() + ".create")) {
            founderName = context.getFlagString('f', founderName);
        }
        // Check for a founder, this would only happen if he is not a player
        if (founderName == null) {
            throw new CommandException("The founder for the to-be-created " + level.getName() + " is unknown. A founder can be specified with the `-f' option.");
        }

        // Get le universe
        String universeName = context.getFlagString('u');
        Universe universe = null;
        if (universeName == null) {
            if (source instanceof Player) {
                universe = getUniverse((Player) source);
            } else {
                throw new CommandException("Please specify the universe you wish to use with the `-u' option.");
            }
        } else {
            universe = Politics.getUniverse(universeName);
            if (universe == null) {
                throw new CommandException("The universe you specified does not exist.");
            }
        }

        // Name
        StringBuilder nameBuilder = new StringBuilder();
        for (int i = 0; i < context.length(); i++) {
            nameBuilder.append(context.getString(i)).append(' ');
        }
        String name = nameBuilder.toString().trim();

        // Tag
        String tag = context.getFlagString('t', name.toLowerCase().replace(" ", "-"));

        // Create le group
        Group group = universe.createGroup(level);
        group.setRole(founderName, level.getFounder());
        group.setProperty(GroupProperty.NAME, name);
        group.setProperty(GroupProperty.TAG, tag);

        if (PoliticsEventFactory.callGroupCreateEvent(group, source).isCancelled()) {
            universe.destroyGroup(group);
            throw new CommandException(level.getName() + " creation denied.");
        }

        source.sendMessage(MsgStyle.SUCCESS, "Your " + level.getName() + " was created successfully.");
    }

    @Override
    public void setupCommand(Command cmd) {
        cmd.setArgBounds(1, -1);
        cmd.setHelp("Creates a new " + level.getName() + ".");
        cmd.setUsage("<name> [-f founder] [-u universe] [-t tag]");
        cmd.setPermissions(true, "politics.group." + level.getId() + ".create");
    }
}
