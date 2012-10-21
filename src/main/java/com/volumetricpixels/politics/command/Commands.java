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
package com.volumetricpixels.politics.command;

import org.spout.api.Spout;
import org.spout.api.command.Command;

import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.command.group.GroupCommands;
import com.volumetricpixels.politics.command.universe.UniverseCommands;
import com.volumetricpixels.politics.group.level.GroupLevel;

/**
 * Registration for all commands
 */
public abstract class Commands {
    /**
     * The name of the command.
     */
    private final String name;

    /**
     * C'tor
     * 
     * @param name
     */
    public Commands(String name) {
        this.name = name.toLowerCase();
    }

    /**
     * Registers this command with Spout.
     * 
     * @return The registered command.
     */
    public final Command register() {
        Command command = Spout.getEngine().getRootCommand().addSubCommand(Politics.getPlugin(), name);
        setup(command);
        command.closeSubCommand();
        return command;
    }

    /**
     * Sets up the command by adding subcommands etc
     * 
     * @param cmd
     *            The Command to add this command to as a subcommand
     */
    public abstract void setup(Command cmd);

    /**
     * Registers all of the commands in Politics
     */
    public static void registerAll() {
        // First register all Universe commands
        (new UniverseCommands()).register();

        // Now register all Group commands
        for (GroupLevel level : Politics.getUniverseManager().getGroupLevels()) {
            (new GroupCommands(level)).register();
        }
    }
}
