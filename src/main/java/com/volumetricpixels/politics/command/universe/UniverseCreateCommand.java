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
package com.volumetricpixels.politics.command.universe;

import java.util.ArrayList;
import java.util.List;

import org.spout.api.Spout;
import org.spout.api.command.Command;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.exception.CommandException;
import org.spout.api.geo.World;

import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.event.PoliticsEventFactory;
import com.volumetricpixels.politics.universe.Universe;
import com.volumetricpixels.politics.universe.UniverseRules;
import com.volumetricpixels.politics.util.MessageStyle;
import com.volumetricpixels.politics.world.PoliticsWorld;

/**
 * Used to create universes.
 */
public class UniverseCreateCommand extends UniverseCommand {
    /**
     * C'tor
     */
    public UniverseCreateCommand() {
        super("create");
    }

    @Override
    public void processCommand(CommandSource source, Command command, CommandContext args) throws CommandException {
        String name = args.getString(0).toLowerCase();

        if (name.contains(" ")) {
            source.sendMessage(MessageStyle.ERROR, "Spaces are not allowed in universe names.");
            return;
        }

        if (name.contains("/") || name.contains("\\")) {
            source.sendMessage(MessageStyle.ERROR, "Slashes are not allowed in universe names.");
            return;
        }

        // boolean force = args.hasFlag('f');
        Universe existing = Politics.getUniverse(name);
        if (existing != null) {
            source.sendMessage(MessageStyle.ERROR, "A universe named '" + name + "' already exists. Please destroy that universe via the `",
                    MessageStyle.ERROR_HIGHLIGHT, "universe destroy", MessageStyle.ERROR, "' command if you wish to overwrite that universe.");
            return;
        }

        String rules = args.getString(1).toLowerCase();
        UniverseRules theRules = Politics.getUniverseManager().getRules(rules);
        if (theRules == null) {
            source.sendMessage(MessageStyle.ERROR, "There is no set of rules named ", MessageStyle.ERROR_HIGHLIGHT, rules, MessageStyle.ERROR,
                    ". To see the available rules, use ", MessageStyle.ERROR_HIGHLIGHT, "universe rules", MessageStyle.ERROR, ".");
            return;
        }

        String worldsStr = args.getString(2);
        List<PoliticsWorld> worlds = new ArrayList<PoliticsWorld>();
        if (worldsStr == null) {
            for (World world : Spout.getEngine().getWorlds()) {
                worlds.add(Politics.getWorld(world));
            }
        } else {
            String[] worldNames = worldsStr.split(",");
            for (String worldName : worldNames) {
                String trimmed = worldName.trim();
                World world = Spout.getEngine().getWorld(trimmed);
                if (world == null) {
                    continue;
                }
                PoliticsWorld pw = Politics.getWorld(world);
                worlds.add(pw);
            }
        }

        if (worlds.size() <= 0) {
            source.sendMessage(MessageStyle.ERROR, "There were no valid worlds specified.");
            return;
        }

        Universe universe = Politics.getUniverseManager().createUniverse(name, theRules);
        PoliticsEventFactory.callUniverseCreateEvent(universe);
        source.sendMessage(MessageStyle.SUCCESS, "You have created the universe '" + name + "' with the rules '" + rules + "'.");
    }

    @Override
    protected String[] getAliases() {
        return new String[] { "new", "c", "n" };
    }

    @Override
    public void setupCommand(Command cmd) {
        cmd.setArgBounds(2, -1);
        cmd.setHelp("Creates a new Universe with the given rules.");
        cmd.setUsage("<name> <rules> [worlds...]");
    }
}
