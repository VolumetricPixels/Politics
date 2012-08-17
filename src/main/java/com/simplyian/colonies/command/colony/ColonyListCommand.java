/*
 * This file is part of Colonies.
 *
 * Colonies
 * Copyright (c) 2012-2012, THEDevTeam <http://thedevteam.org/>
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
package com.simplyian.colonies.command.colony;

import java.util.List;

import org.spout.api.command.Command;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandExecutor;
import org.spout.api.command.CommandSource;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;

import com.simplyian.colonies.Colonies;
import com.simplyian.colonies.MsgStyle;
import com.simplyian.colonies.colony.Colony;
import com.simplyian.colonies.colony.ColonyLevel;
import com.simplyian.colonies.universe.Universe;

/**
 * Colony list command
 */
public class ColonyListCommand extends ColonyCommand {
	public static final int PAGE_HEIGHT = 20;

	/**
	 * C'tor
	 * 
	 * @param level
	 */
	private ColonyListCommand(ColonyLevel level) {
		super(level);
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
			universe = Colonies.getUniverse(universeName);
			if (universe == null) {
				source.sendMessage(MsgStyle.error(), "A universe with the name `" + universeName + "' does not exist.");
				return;
			}
		} else {
			universe = Colonies.getUniverse(((Player) source).getWorld(), level);
			if (universe == null) {
				source.sendMessage(MsgStyle.error(), "You can't use this command right now.");
				return;
			}
		}

		source.sendMessage(MsgStyle.info(), "========= " + level.getPlural().toUpperCase() + " =========");

		List<Colony> colonies = universe.getColonies(level);
		if (colonies == null) {
			source.sendMessage(MsgStyle.error(), "There are no " + level.getPlural() + ".");
			return;
		}

		int page = 1;
		if (args.isInteger(0)) {
			page = args.getInteger(0);
		}

		int min = ((page - 1) * PAGE_HEIGHT) - 1; // Screen height
		int max = Math.min(colonies.size(), page * PAGE_HEIGHT) - 2;
		if (max <= min) {
			source.sendMessage(MsgStyle.error(), "There are no " + level.getPlural() + " on this page.");
			return;
		}

		List<Colony> pageColonies = colonies.subList(min, max);
		for (Colony colony : pageColonies) {
			source.sendMessage(colony.getName()); // TODO prettify list
		}
	}

	public static ColonyListCommand register(Command parent, ColonyLevel level) {
		ColonyListCommand executor = new ColonyListCommand(level);

		Command cmd = parent.addSubCommand(Colonies.getPlugin(), "list");
		cmd.setExecutor(executor);
		cmd.addAlias("ls");
		cmd.setArgBounds(0, -1);
		cmd.closeSubCommand();

		return executor;
	}
}
