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

import com.volumetricpixels.politics.event.PoliticsEventFactory;
import com.volumetricpixels.politics.group.Group;
import com.volumetricpixels.politics.group.GroupProperty;
import com.volumetricpixels.politics.group.level.GroupLevel;
import com.volumetricpixels.politics.universe.Universe;
import com.volumetricpixels.politics.util.MessageStyle;

public class GroupCreateCommand extends GroupCommand {
    /**
     * C'tor
     * 
     * @param level
     *            The GroupLevel of this command
     */
    public GroupCreateCommand(final GroupLevel level) {
        super(level, "create");
    }

    @Override
    public void processCommand(final CommandSource source, final Command cmd, final CommandContext context) throws CommandException {
        // Get the founder
        String founderName = null;
        if (source instanceof Player) {
            founderName = source.getName();
        }
        if (hasAdmin(source)) {
            founderName = context.getFlagString('f', founderName);
        }
        // Check for a founder, this would only happen if he is not a player
        if (founderName == null) {
            throw new CommandException("The founder for the to-be-created " + level.getName()
                    + " is unknown. A founder can be specified with the `-f' option.");
        }

        // Get the universe
        final Universe universe = findUniverse(source, cmd, context);

        // Name
        final StringBuilder nameBuilder = new StringBuilder();
        for (int i = 0; i < context.length(); i++) {
            nameBuilder.append(context.getString(i)).append(' ');
        }
        final String name = nameBuilder.toString().trim();

        // Tag
        final String tag = context.getFlagString('t', name.toLowerCase().replace(" ", "-"));

        // Create le group
        final Group group = universe.createGroup(level);
        group.setRole(founderName, level.getFounder());
        group.setProperty(GroupProperty.NAME, name);
        group.setProperty(GroupProperty.TAG, tag);

        if (PoliticsEventFactory.callGroupCreateEvent(group, source).isCancelled()) {
            universe.destroyGroup(group);
            throw new CommandException(level.getName() + " creation denied!");
        }

        source.sendMessage(MessageStyle.SUCCESS, "Your " + level.getName() + " was created successfully.");
    }

    @Override
    public void setupCommand(final Command cmd) {
        cmd.setArgBounds(1, -1);
        cmd.setHelp("Creates a new " + level.getName() + ".");
        cmd.setUsage("<name> [-f founder] [-u universe] [-t tag]");
    }
}
