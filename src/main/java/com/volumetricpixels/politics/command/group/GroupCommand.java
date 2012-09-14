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

import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.util.MsgStyle;
import com.volumetricpixels.politics.command.PCommand;
import com.volumetricpixels.politics.group.Citizen;
import com.volumetricpixels.politics.group.Group;
import com.volumetricpixels.politics.group.GroupProperty;
import com.volumetricpixels.politics.group.level.GroupLevel;
import com.volumetricpixels.politics.universe.Universe;

import org.spout.api.entity.Player;
import org.spout.api.command.Command;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;

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
     * @param primary
     */
    public GroupCommand(GroupLevel level, String primary) {
        super(primary.toLowerCase());
        this.level = level;
        this.truePrimary = this.primary;

        // Load the primary and aliases
        aliases = level.getAliases(this.primary);
        if (aliases.size() <= 0) {
            this.primary = null;
            return;
        }

        int index = aliases.indexOf(this.primary);
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
    protected String[] getPermissions() {
        return new String[]{"politics.group." + level.getId() + "." + truePrimary};
    }

    /**
     * Gets the GroupLevel
     *
     * @return
     */
    public GroupLevel getLevel() {
        return level;
    }

    /**
     * Gets the citizen corresponding with the given player.
     *
     * @param player
     * @return
     */
    public Citizen getCitizen(Player player) {
        Universe universe = getUniverse(player);
        if (universe != null) {
            return universe.getCitizen(player.getName());
        }
        return null;
    }

    /**
     * Gets the universe of the given player in relation to this command.
     *
     * @param player
     * @return
     */
    public Universe getUniverse(Player player) {
        return Politics.getUniverse(player.getWorld(), level);
    }

    /**
     * Finds the group that is wanted from the arguments.
     *
     * @param source
     * @param cmd
     * @param context
     * @return The group
     */
    public Group findGroup(CommandSource source, Command cmd, CommandContext context) {
        Universe universe = null;
        String universeName = context.getFlagString('u');

        if (universeName != null) {
            universe = Politics.getUniverse(universeName);
            if (universe == null) {
                source.sendMessage(MsgStyle.ERROR, "There isn't a universe with the name of '" + universeName + "'.");
                return null;
            }
        } else {
            if (source instanceof Player) {
                universe = getUniverse((Player) source);
                if (universe == null) {
                    source.sendMessage(MsgStyle.ERROR, "You aren't currently in a world containing " + level.getPlural() + ".");
                    return null;
                }
            } else {
                source.sendMessage(MsgStyle.ERROR, "There was no universe specified.");
                return null;
            }
        }

        Group group = null;
        String groupName = context.getFlagString('g');
        if (groupName != null) {
            group = universe.getFirstGroupByProperty(level, GroupProperty.TAG, groupName.toLowerCase());
        } else {
            if (source instanceof Player) {
                group = getCitizen((Player) source).getGroup(level);
            } else {
                source.sendMessage(MsgStyle.ERROR, "No " + level.getName() + " was specified.");
                return null;
            }
        }
        return group;
    }
}
