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
package com.volumetricpixels.politics.command.universe;

import java.util.HashSet;
import java.util.Set;

import org.spout.api.Spout;
import org.spout.api.command.Command;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandExecutor;
import org.spout.api.command.CommandSource;
import org.spout.api.exception.CommandException;
import org.spout.api.geo.World;

import com.volumetricpixels.politics.MsgStyle;
import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.plot.PoliticsWorld;
import com.volumetricpixels.politics.universe.Universe;
import com.volumetricpixels.politics.universe.UniverseRules;

/**
 * Used to create universes.
 */
public class UniverseCreateCommand implements CommandExecutor {

	@Override
	public void processCommand(CommandSource source, Command command, CommandContext args) throws CommandException {
		String name = args.getString(0).toLowerCase();
		String rules = args.getString(1).toLowerCase();
		UniverseRules theRules = Politics.getUniverseManager().getRules(rules);
		if (theRules == null) {
			source.sendMessage(MsgStyle.error(), "There is no set of rules named '" + rules + "'. To see the available rules, use `universe rules`.");
			return;
		}

		Universe universe = Politics.getUniverseManager().createUniverse(name, theRules);

		int worlds = 0;
		if (args.length() > 2) {
			for (int i = 2; i < args.length(); i++) {
				String worldName = args.getString(i);
				World spoutWorld = Spout.getEngine().getWorld(worldName);
				if (spoutWorld == null) {
					continue;
				}
				PoliticsWorld pw = Politics.getWorld(spoutWorld);
				if (universe.addWorld(pw)) {
					worlds++;
				}
			}
		}

		if (worlds <= 0) {
			source.sendMessage(MsgStyle.error(), "There were no valid worlds specified.");
		}
		// TODO destroy universe
		source.sendMessage(MsgStyle.success(), "A new universe has been created named '" + name + "' with the rules '" + rules + "'.");
	}

	public static Command register(Command parent) {
		UniverseCreateCommand exec = new UniverseCreateCommand();

		Command cmd = parent.addSubCommand(Politics.getPlugin(), "create");
		cmd.addAlias("new", "c", "n");
		cmd.setArgBounds(2, -1);
		cmd.setExecutor(exec);
		cmd.setHelp("Creates a new Universe with the given rules.");
		cmd.setUsage("create <name> <rules> [worlds...]");
		cmd.setPermissions(true, "politics.admin.universe.create");
		cmd.closeSubCommand();

		return cmd;
	}
}
