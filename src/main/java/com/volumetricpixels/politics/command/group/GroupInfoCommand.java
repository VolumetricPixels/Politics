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

import java.util.Set;

import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.Command;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;

import com.volumetricpixels.politics.MsgStyle;
import com.volumetricpixels.politics.group.Citizen;
import com.volumetricpixels.politics.group.Group;
import com.volumetricpixels.politics.group.level.GroupLevel;
import com.volumetricpixels.politics.group.GroupProperty;

public class GroupInfoCommand extends GroupCommand {

	public GroupInfoCommand(GroupLevel level) {
		super(level, "info");
	}

	@Override
	public void processCommand(CommandSource source, Command cmd, CommandContext context) throws CommandException {
		if (!(source instanceof Player)) {
			source.sendMessage("Consoles are part of a group");
			return;
		}

		Player p = (Player) source;
		Citizen citizen = getCitizen(p);
		if (citizen == null) {
			source.sendMessage(MsgStyle.error(), "You can't use this command in this world.");
			return;
		}

		Set<Group> groups = citizen.getGroups(level);
		if (groups.size() <= 0) {
			source.sendMessage(MsgStyle.error(), "You aren't in a " + level.getName() + ".");
		}

		p.sendMessage(ChatStyle.BLUE, "============= INFO =============");
		p.sendMessage(ChatStyle.DARK_GREEN, "Current Group: " + groups.iterator().next().getStringProperty(GroupProperty.NAME));
		p.sendMessage(ChatStyle.BLUE, "================================");
	}

	@Override
	public void setupCommand(Command cmd) {
		cmd.setHelp("Gets information about the current group you are in.");
	}
}
