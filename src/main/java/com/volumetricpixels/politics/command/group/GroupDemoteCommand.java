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

import org.spout.api.Server;
import org.spout.api.command.Command;
import org.spout.api.command.CommandArguments;
import org.spout.api.command.CommandSource;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;

import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.group.Group;
import com.volumetricpixels.politics.group.level.GroupLevel;
import com.volumetricpixels.politics.group.level.Role;
import com.volumetricpixels.politics.group.level.RoleTrack;

/**
 * Group promote command.
 */
public class GroupDemoteCommand extends GroupCommand {
    /**
     * C'tor
     * 
     * @param level
     *            The GroupLevel of this command
     */
    public GroupDemoteCommand(final GroupLevel level) {
        super(level, "demote");
    }

    @Override
    public void execute(final CommandSource source, final Command cmd, final CommandArguments args) throws CommandException {
        final Group group = findGroup(source, cmd, args);

        final Player player = ((Server) Politics.getPlugin().getEngine()).getPlayer(args.get().get(0), false);
        if (player == null) {
            throw new CommandException("That player is not online.");
        }
        if (!group.isImmediateMember(player.getName())) {
            throw new CommandException("That player is not a member of the group!");
        }

        final String trackName = args.getString("t");
        RoleTrack track;
        if (trackName == null) {
            track = group.getLevel().getDefaultTrack();
        } else {
            track = group.getLevel().getTrack(trackName.toLowerCase());
        }
        if (track == null) {
            throw new CommandException("There isn't a track named '" + trackName + "'!");
        }
        final Role role = group.getRole(player.getName());
        final Role next = track.getPreviousRole(role);
        if (next == null) {
            throw new CommandException("There is no role to demote to!");
        }

        if (!hasAdmin(source)) {
            final Role myRole = group.getRole(source.getName());
            if (myRole.getRank() - role.getRank() <= 0) {
                throw new CommandException("You can't demote someone with a rank higher than yours!");
            }
        }

        group.setRole(player.getName(), next);
        source.sendMessage(player.getName() + " was demoted to " + next.getName() + " in the group!");
    }

    @Override
    public void setupCommand(final Command cmd) {
        cmd.setHelp("Demotes a player in this " + level.getName() + ".");
        cmd.setUsage("<player> [-t track] [-g group] [-u universe]");
    }
}
