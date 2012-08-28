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
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;

import com.volumetricpixels.politics.MsgStyle;
import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.group.Group;
import com.volumetricpixels.politics.group.level.GroupLevel;
import com.volumetricpixels.politics.group.GroupProperty;
import com.volumetricpixels.politics.universe.Universe;

/**
 * Group list command
 */
public class GroupListCommand extends GroupCommand {
	public static final int PAGE_HEIGHT = 20;

	/**
	 * C'tor
	 * 
	 * @param level
	 */
	public GroupListCommand(GroupLevel level) {
		super(level, "list");
	}

	@Override
	public void processCommand(CommandSource source, Command command, CommandContext args) throws CommandException {
		Universe universe = null;

		// Get le universe
		if (!(source instanceof Player)) {
			if (!args.hasFlag('u')) {
				source.sendMessage(MsgStyle.error(), "You need to specify a universe. Add `-u universename' to your command to make it so.");
				return;
			}
			String universeName = args.getFlagString('u');
			universe = Politics.getUniverse(universeName);
			if (universe == null) {
				source.sendMessage(MsgStyle.error(), "A universe with the name `" + universeName + "' does not exist.");
				return;
			}
		} else {
			universe = Politics.getUniverse(((Player) source).getWorld(), level);
			if (universe == null) {
				source.sendMessage(MsgStyle.error(), "You can't use this command right now.");
				return;
			}
		}

		source.sendMessage(MsgStyle.info(), "========= " + level.getPlural().toUpperCase() + " =========");

		List<Group> groups = universe.getGroups(level);
		if (groups == null) {
			source.sendMessage(MsgStyle.error(), "There are no " + level.getPlural() + ".");
			return;
		}

		int page = 1;
		if (args.isInteger(0)) {
			page = args.getInteger(0);
		}

		int min = ((page - 1) * PAGE_HEIGHT) - 1; // Screen height
		int max = Math.min(groups.size(), page * PAGE_HEIGHT) - 2;
		if (max <= min) {
			source.sendMessage(MsgStyle.error(), "There are no " + level.getPlural() + " on this page.");
			return;
		}

		List<Group> pageGroups = groups.subList(min, max);
		for (Group group : pageGroups) {
			source.sendMessage(group.getProperty(GroupProperty.TAG));
			// TODO prettify list
		}
	}
}
