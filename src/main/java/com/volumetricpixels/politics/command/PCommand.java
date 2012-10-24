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

import org.spout.api.command.Command;
import org.spout.api.command.CommandExecutor;

import com.volumetricpixels.politics.Politics;

/**
 * Represents a command that can be run in Politics. Helps out with command
 * registration.
 */
public abstract class PCommand implements CommandExecutor {
    /**
     * The primary name of this command.
     */
    protected String primary = null;

    /**
     * C'tor
     * 
     * @param primary
     *            The primary name of the command
     */
    protected PCommand(String primary) {
        this.primary = primary;
    }

    /**
     * Registers this command with the given parent.
     * 
     * @param parent
     *            The command to set this command as a subcommand of
     * @return A Command object representing this command
     */
    public final Command register(Command parent) {
        if (primary == null) {
            return null; // No registration
        }

        Command cmd = parent.addSubCommand(Politics.getPlugin(), primary);
        cmd.setExecutor(this);
        cmd.addAlias(getAliases());
        cmd.setPermissions(true, getPermissions());
        setupCommand(cmd);
        cmd.closeSubCommand();
        return cmd;
    }

    /**
     * Sets up the command created.
     * 
     * @param cmd
     *            The command to set up
     */
    public void setupCommand(Command cmd) {
        cmd.setArgBounds(0, -1);
    }

    /**
     * Gets the aliases for this command.
     * 
     * @return A String[] of aliases for this command
     */
    protected String[] getAliases() {
        return new String[0];
    }

    /**
     * Gets the permissions required to execute this command.
     * 
     * @return A String[] of permissions for this command
     */
    protected String[] getPermissions() {
        return new String[0];
    }
}
