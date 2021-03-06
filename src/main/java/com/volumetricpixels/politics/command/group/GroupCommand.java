/*
 * This file is part of Politics.
 * 
 * Copyright (c) 2012-2012, VolumetricPixels <http://volumetricpixels.com/>
 * Politics is licensed under the Affero General Public License Version 3.
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.volumetricpixels.politics.command.group;

import java.util.List;

import org.spout.api.command.Command;
import org.spout.api.command.CommandArguments;
import org.spout.api.command.CommandSource;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;

import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.command.PCommand;
import com.volumetricpixels.politics.group.Citizen;
import com.volumetricpixels.politics.group.Group;
import com.volumetricpixels.politics.group.GroupProperty;
import com.volumetricpixels.politics.group.level.GroupLevel;
import com.volumetricpixels.politics.universe.Universe;

/**
 * A group-related command.
 */
public abstract class GroupCommand extends PCommand {
    /**
     * Level of this GroupCommand.
     */
    protected final GroupLevel level;
    /**
     * The original primary name of the command.
     */
    private String truePrimary;
    /**
     * Aliases of this GroupCommand.
     */
    private List<String> aliases;

    /**
     * C'tor
     * 
     * @param level
     *            The GroupLevel of this command
     * @param primary
     *            The primary name of this command
     */
    public GroupCommand(final GroupLevel level, final String primary) {
        super(primary.toLowerCase());
        this.level = level;
        truePrimary = this.primary;

        // Load the primary and aliases
        aliases = level.getAliases(this.primary);
        if (aliases.size() <= 0) {
            this.primary = null;
            return;
        }

        final int index = aliases.indexOf(this.primary);
        if (index != -1) {
            aliases.remove(index);
        } else {
            this.primary = aliases.get(0);
            aliases.remove(0);
        }
    }

    @Override
    protected String[] getAliases() {
        return aliases.toArray(new String[0]);
    }

    @Override
    protected String getPermission() {
        return "politics.group." + level.getId() + "." + truePrimary;
    }

    /**
     * Gets this command's GroupLevel
     * 
     * @return The GroupLevel of this command
     */
    public GroupLevel getLevel() {
        return level;
    }

    /**
     * Gets the citizen corresponding with the given player
     * 
     * @param player
     *            The Player to get the Citizen for
     * @return The Citizen corresponding with the given Player
     */
    public Citizen getCitizen(final Player player) {
        final Universe universe = getUniverse(player);
        if (universe != null) {
            return universe.getCitizen(player.getName());
        }
        return null;
    }

    /**
     * Gets the universe of the given player in relation to this command.
     * 
     * @param player
     *            The Player to get the Universe of
     * @return The Universe the given Player is in
     */
    public Universe getUniverse(final Player player) {
        return Politics.getUniverse(player.getWorld(), level);
    }

    /**
     * Finds the universe that is wanted from the arguments. Uses the 'u' flag.
     * 
     * @param source
     *            The source of the command
     * @param cmd
     *            The Command executed
     * @param context
     *            The arguments of the command
     * @return The universe
     */
    public Universe findUniverse(final CommandSource source, final Command cmd, final CommandArguments context) throws CommandException {
        Universe universe = null;
        final String universeName = context.getString("u");

        if (universeName != null) {
            universe = Politics.getUniverse(universeName);
            if (universe == null) {
                throw new CommandException("There isn't a universe with the name of '" + universeName + "'!");
            }
        } else {
            if (source instanceof Player) {
                universe = getUniverse((Player) source);
                if (universe == null) {
                    throw new CommandException("You aren't currently in a world containing " + level.getPlural() + "!");
                }
            } else {
                throw new CommandException("There was no universe specified!");
            }
        }
        return universe;
    }

    /**
     * Finds the group that is wanted from the arguments. Uses the `g' and `u'
     * flags.
     * 
     * @param source
     *            The source of the command
     * @param cmd
     *            The command executed
     * @param context
     *            The arguments of the command
     * @return The group
     */
    public Group findGroup(final CommandSource source, final Command cmd, final CommandArguments context) throws CommandException {
        final Universe universe = findUniverse(source, cmd, context);

        Group group = null;
        final String groupName = context.getString("g");
        if (groupName != null) {
            group = universe.getFirstGroupByProperty(level, GroupProperty.TAG, groupName.toLowerCase());
        } else {
            if (source instanceof Player) {
                group = getCitizen((Player) source).getGroup(level);
            } else {
                throw new CommandException("No " + level.getName() + " was specified.");
            }
        }
        return group;
    }

    /**
     * Returns true if the given source has admin privileges for this command.
     * 
     * @param source
     *            The source of the command
     * @return True if the given source has admin privileges for this command.
     */
    public boolean hasAdmin(final CommandSource source) {
        return source.hasPermission("politics.admin.group." + level.getId() + "." + primary);
    }
}
