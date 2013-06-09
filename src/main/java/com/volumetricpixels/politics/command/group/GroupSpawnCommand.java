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

import org.spout.api.command.Command;
import org.spout.api.command.CommandArguments;
import org.spout.api.command.CommandSource;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;
import org.spout.api.geo.discrete.Transform;

import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.group.Group;
import com.volumetricpixels.politics.group.GroupProperty;
import com.volumetricpixels.politics.group.level.GroupLevel;
import com.volumetricpixels.politics.group.privilege.GroupPrivileges;

/**
 * Teleports you to your group's spawn.
 */
public class GroupSpawnCommand extends GroupCommand {
    /**
     * C'tor
     * 
     * @param level
     *            The GroupLevel of this command
     */
    public GroupSpawnCommand(final GroupLevel level) {
        super(level, "spawn");
    }

    @Override
    public void execute(final CommandSource source, final Command cmd, final CommandArguments context) throws CommandException {
        final Group group = findGroup(source, cmd, context);

        if (!group.can(source, GroupPrivileges.SPAWN) && !hasAdmin(source)) {
            throw new CommandException("You don't have permissions to spawn to that " + level.getName() + ".");
        }

        final Transform spawn = group.getTransformProperty(GroupProperty.SPAWN);
        if (spawn == null) {
            throw new CommandException("The " + level.getName() + " doesn't have a spawn!");
        }

        Player player = null;
        final String playerName = context.getFlagString('p');
        if (playerName == null) {
            if (source instanceof Player) {
                player = (Player) source;
            }
        } else {
            if (!source.hasPermission("politics.admin.group." + level.getId() + ".spawnother")) {
                throw new CommandException("You aren't allowed to spawn other players!");
            }
            player = Politics.getPlugin().getEngine().getPlayer(playerName, false);
        }

        if (player == null) {
            throw new CommandException("The player wasn't specified!");
        }

        player.getScene().setTransform(spawn);

        if (playerName != null) {
            source.sendMessage(playerName + " was teleported to the " + level.getName() + " spawn!");
        }
        player.sendMessage("You have been teleported to the spawn of " + group.getStringProperty(GroupProperty.NAME) + "!");
    }

    @Override
    public void setupCommand(final Command cmd) {
        cmd.setArgumentBounds(1, -1);
        cmd.setHelp("Sets the spawn of your " + level.getName() + ".");
        cmd.setUsage("[-p player] [-g " + level.getName() + "] [-u universe]");
    }
}
