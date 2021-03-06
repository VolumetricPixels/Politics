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
package com.volumetricpixels.politics.command.universe;

import org.spout.api.command.Command;
import org.spout.api.command.CommandArguments;
import org.spout.api.command.CommandSource;
import org.spout.api.exception.CommandException;

import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.event.PoliticsEventFactory;
import com.volumetricpixels.politics.universe.Universe;

/**
 * Destroys a universe and all of its groups.
 */
public class UniverseDestroyCommand extends UniverseCommand {
    /**
     * C'tor
     */
    public UniverseDestroyCommand() {
        super("destroy");
    }

    @Override
    public void execute(final CommandSource source, final Command command, final CommandArguments args) throws CommandException {
        final Universe universe = Politics.getUniverse(args.get().get(0));
        if (universe == null) {
            source.sendMessage("A universe with the name '" + args.get().get(0) + "' doesn't exist.");
            return;
        }

        Politics.getUniverseManager().destroyUniverse(universe);
        PoliticsEventFactory.callUniverseDestroyEvent(universe);
        source.sendMessage("Universe destroyed, sir.");
    }

    @Override
    protected String[] getAliases() {
        return new String[] { "delete", "d" };
    }

    @Override
    public void setupCommand(final Command cmd) {
        cmd.setHelp("Destroys the given universe.");
        cmd.setUsage("<name>");
    }
}
