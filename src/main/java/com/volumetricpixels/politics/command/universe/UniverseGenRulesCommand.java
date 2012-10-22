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

import java.util.Set;

import org.spout.api.command.Command;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.exception.CommandException;

import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.universe.RuleTemplates;
import com.volumetricpixels.politics.universe.UniverseRules;
import com.volumetricpixels.politics.util.MsgStyle;

/**
 * Creates Universe rules.
 */
public class UniverseGenRulesCommand extends UniverseCommand {
    /**
     * C'tor
     */
    public UniverseGenRulesCommand() {
        super("genrules");
    }

    @Override
    public void processCommand(CommandSource source, Command command, CommandContext args) throws CommandException {
        String templateName = args.getString(0).toLowerCase();
        Set<String> templateNames = RuleTemplates.listTemplateNames();
        if (!templateNames.contains(templateName)) {
            source.sendMessage(MsgStyle.ERROR, "A template with the name of '" + templateName + "' does not exist.");
            return;
        }

        String name = args.getFlagString('n', templateName).toLowerCase();
        UniverseRules existing = Politics.getUniverseManager().getRules(name);

        boolean force = args.hasFlag('f');
        if (existing != null && !force) {
            source.sendMessage(MsgStyle.ERROR, "A set of rules with the name of '" + name
                    + "' already exists. Use the '-f' option to overwrite an existing rule set.");
            return;
        }

        RuleTemplates.copyTemplate(templateName, name);
        source.sendMessage(MsgStyle.SUCCESS, "A new set of rules named '" + name + "' based on the template '" + templateName
                + "' has been generated. Please restart the server to see your changes.");
    }

    @Override
    protected String[] getAliases() {
        return new String[] { "gr" };
    }

    @Override
    public void setupCommand(Command cmd) {
        cmd.setArgBounds(1, -1);
        cmd.setHelp("Generates a set of rules.");
        cmd.setUsage("<template> [-f] [-n name]");
    }
}
