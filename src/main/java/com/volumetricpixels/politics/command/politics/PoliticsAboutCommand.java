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
package com.volumetricpixels.politics.command.politics;

import java.util.Calendar;

import org.spout.api.command.Command;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.exception.CommandException;

import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.util.MsgStyle;

/**
 * Politics About Command.
 */
public class PoliticsAboutCommand extends PoliticsCommand {
    /**
     * C'tor
     */
    public PoliticsAboutCommand() {
        super("about");
    }

    @Override
    public void processCommand(CommandSource source, Command command, CommandContext args) throws CommandException {
        source.sendMessage(MsgStyle.INFO, "Politics ", Politics.getPlugin().getVersion());
        source.sendMessage(MsgStyle.INFO, "Copyright (c) 2012-" + Calendar.getInstance().get(Calendar.YEAR) + " Volumetric Pixels");
        source.sendMessage(MsgStyle.INFO,
                "Politics is a self-serve community management system, designed as the final replacement for Towny, Factions and other similar plugins.");
    }
}