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
import org.spout.api.command.CommandExecutor;

import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.group.GroupLevel;

/**
 * A group-related command.
 */
public abstract class GroupCommand implements CommandExecutor {
	protected final GroupLevel level;
	protected final String primary;

	/**
	 * C'tor
	 * 
	 * @param level
	 * @param primary
	 */
	public GroupCommand(GroupLevel level, String primary) {
		this.level = level;
		this.primary = primary.toLowerCase();
	}

	/**
	 * Registers the command with the given parent.
	 * 
	 * @param parent
	 * @return
	 */
	public Command register(Command parent) {
		List<String> commands = level.getAliases("list");

		if (commands.size() <= 0) {
			return null;
		}

		String primary;
		int index = commands.indexOf("list");
		if (index != -1) {
			primary = "list";
			commands.remove(index);
		} else {
			primary = commands.get(0);
			commands.remove(0);
		}

		Command cmd = parent.addSubCommand(Politics.getPlugin(), primary);
		cmd.setExecutor(this);
		if (commands.size() > 0) {
			cmd.addAlias(commands.toArray(new String[0]));
		}
		setupCommand(cmd);
		cmd.closeSubCommand();

		return cmd;
	}

	/**
	 * Sets up the command created.
	 * 
	 * @param cmd
	 */
	public void setupCommand(Command cmd) {
		cmd.setArgBounds(0, -1);
	}
}
