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

import org.spout.api.Spout;
import org.spout.api.command.Command;

import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.group.GroupLevel;

/**
 * Group commands.
 */
public class GroupCommands {
	public static void register(GroupLevel level) {
		Command cmd = Spout.getEngine().getRootCommand().addSubCommand(Politics.getPlugin(), level.getName().toLowerCase());

		GroupListCommand.register(cmd, level);

		cmd.closeSubCommand();
	}
}
