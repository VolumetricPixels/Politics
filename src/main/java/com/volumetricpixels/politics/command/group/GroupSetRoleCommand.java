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
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;

import com.volumetricpixels.politics.group.Group;
import com.volumetricpixels.politics.group.level.GroupLevel;
import com.volumetricpixels.politics.group.level.Role;
import com.volumetricpixels.politics.util.MessageStyle;

/**
 * Group set role command
 */
public class GroupSetRoleCommand extends GroupCommand {

    /**
     * C'tor
     *
     * @param level The GroupLevel of this command
     */
    public GroupSetRoleCommand(final GroupLevel level) {
        super(level, "setrole");
    }

    @Override
    public void processCommand(final CommandSource source, final Command cmd, final CommandContext args) throws CommandException {
        final Group group = findGroup(source, cmd, args);

        final Player player = Spout.getEngine().getPlayer(args.getString(0), false);
        if (player == null) {
            throw new CommandException("That player is not online!");
        }
        if (!group.isImmediateMember(player.getName())) {
            throw new CommandException("That player is not a member of the group!");
        }

        final String rn = args.getString(1);
        final Role role = group.getLevel().getRole(rn);
        if (role == null) {
            throw new CommandException("There isn't a role named `" + rn + "'!");
        }

        if (!hasAdmin(source)) {
            final Role myRole = group.getRole(source.getName());
            if (myRole.getRank() - role.getRank() <= 1) {
                throw new CommandException("You can't set someone to a role equal to or higher than your own!");
            }
        }

        group.setRole(player.getName(), role);
        source.sendMessage(MessageStyle.SUCCESS, player.getName() + " is now part of the " + level.getName() + "!");
    }

    @Override
    public void setupCommand(final Command cmd) {
        cmd.setArgBounds(2, -1);
        cmd.setHelp("Sets the role of a player in this " + level.getName() + ".");
        cmd.setUsage("<player> <role> [-g group] [-u universe]");
    }
}
