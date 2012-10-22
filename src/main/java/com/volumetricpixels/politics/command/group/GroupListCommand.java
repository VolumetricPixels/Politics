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

import java.util.List;

import org.spout.api.command.Command;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.exception.CommandException;

import com.volumetricpixels.politics.group.Group;
import com.volumetricpixels.politics.group.GroupProperty;
import com.volumetricpixels.politics.group.level.GroupLevel;
import com.volumetricpixels.politics.universe.Universe;
import com.volumetricpixels.politics.util.MessageStyle;

/**
 * Group list command
 */
public class GroupListCommand extends GroupCommand {
    public static final int PAGE_HEIGHT = 20;

    /**
     * C'tor
     * 
     * @param level
     *            The GroupLevel of this command
     */
    public GroupListCommand(GroupLevel level) {
        super(level, "list");
    }

    @Override
    public void processCommand(CommandSource source, Command cmd, CommandContext args) throws CommandException {
        Universe universe = findUniverse(source, cmd, args);

        source.sendMessage(MessageStyle.INFO, "========= " + level.getPlural().toUpperCase() + " =========");

        List<Group> groups = universe.getGroups(level);
        if (groups == null) {
            throw new CommandException("There are no " + level.getPlural() + ".");
        }

        int page = 1;
        if (args.isInteger(0)) {
            page = args.getInteger(0);
        }

        int min = ((page - 1) * PAGE_HEIGHT) - 1; // Screen height
        int max = Math.min(groups.size(), page * PAGE_HEIGHT) - 2;
        if (max <= min) {
            throw new CommandException("There are no " + level.getPlural() + " on this page.");
        }

        List<Group> pageGroups = groups.subList(min, max);
        for (Group group : pageGroups) {
            source.sendMessage(group.getProperty(GroupProperty.TAG));
            // TODO prettify list
        }
    }
}
