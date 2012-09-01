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

import org.spout.api.entity.Player;

import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.command.PCommand;
import com.volumetricpixels.politics.group.Citizen;
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
}
