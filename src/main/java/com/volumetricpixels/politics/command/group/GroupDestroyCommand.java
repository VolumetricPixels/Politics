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
import java.util.Set;

import org.spout.api.command.Command;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;

import com.volumetricpixels.politics.MsgStyle;
import com.volumetricpixels.politics.api.Politics;
import com.volumetricpixels.politics.group.Group;
import com.volumetricpixels.politics.group.level.GroupLevel;
import com.volumetricpixels.politics.group.GroupProperty;

/**
 * Used to destroy (delete) a group
 */
public class GroupDestroyCommand extends GroupCommand {
    public GroupDestroyCommand(GroupLevel level) {
        super(level, "destroy");
    }

    @Override
    public void processCommand(CommandSource source, Command cmd, CommandContext context) throws CommandException {
    	if (!(source instanceof Player) || !Politics.getUniverse(((Player) source).getWorld(), level).getCitizenGroups(source.getName()).contains(Group.fromName(context.getFlagString('g')))) {
    		if (source.hasPermission("politics.admin.delgroup")) {
        		for (Group g : Politics.getUniverse(((Player) source).getWorld(), level).getGroups()) {
            		if (g.getStringProperty(GroupProperty.TAG).equalsIgnoreCase(context.getString(0))) {
                    	g.getUniverse().destroyGroup(g);
                    	source.sendMessage(MsgStyle.SUCCESS, "Deleted: " + context.getString(0) + "!");
                    	return;
                	}
            	}
            	throw new CommandException("No such " + level.getName() + "!");
        	}
    	}
        if (source instanceof Player) {
            Set<Group> groups = Politics.getUniverse(((Player) source).getWorld(), level).getCitizen(source.getName()).getGroups();
            for (Group g : groups) {
                if (g.getStringProperty(GroupProperty.TAG).equalsIgnoreCase(context.getString(0))) {
                    g.getUniverse().destroyGroup(g);
                    source.sendMessage("Deleted: " + context.getString(0) + "!");
                    return;
                }
                throw new CommandException("You can't do that!");
            }
        } else {
            List<Group> groups = Politics.getUniverse(((Player) source).getWorld(), level).getGroups();
            for (Group g : groups) {
                if (((String) g.getProperty(GroupProperty.TAG)).equalsIgnoreCase(context.getString(0))) {
                    g.getUniverse().destroyGroup(g);
                    source.sendMessage(MsgStyle.SUCCESS, "Deleted! " + context.getString(0));
                    return;
                }
            }
        }
    }

    @Override
    public void setupCommand(Command cmd) {
        cmd.setArgBounds(1, -1);
        cmd.setHelp("Destroys your " + level.getName() + ".");
        cmd.setUsage("<template> [-f] [-n name]");
        cmd.setPermissions(true, "politics.group." + level.getId() + ".destroy");
    }
}