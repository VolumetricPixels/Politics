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

import org.spout.api.Spout;
import org.spout.api.command.Command;

import com.simplyian.colonies.Colonies;
import com.simplyian.colonies.colony.ColonyLevel;

/**
 * Colony commands.
 */
public class ColonyCommands {
	public static void register(ColonyLevel level) {
		Command cmd = Spout.getEngine().getRootCommand().addSubCommand(Colonies.getPlugin(), level.getName().toLowerCase());

		ColonyListCommand.register(cmd, level);
	}
}
