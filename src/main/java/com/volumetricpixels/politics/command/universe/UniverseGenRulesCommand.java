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

import java.util.Set;

import org.spout.api.command.Command;
import org.spout.api.command.CommandArguments;
import org.spout.api.command.CommandSource;
import org.spout.api.exception.CommandException;

import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.universe.RuleTemplates;
import com.volumetricpixels.politics.universe.UniverseRules;

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
    public void execute(final CommandSource source, final Command command, final CommandArguments args) throws CommandException {
        final String templateName = args.get().get(0).toLowerCase();
        final Set<String> templateNames = RuleTemplates.listTemplateNames();
        if (!templateNames.contains(templateName)) {
            source.sendMessage("A template with the name of '" + templateName + "' does not exist.");
            return;
        }

        final String name = args.getString("n", templateName).toLowerCase();
        final UniverseRules existing = Politics.getUniverseManager().getRules(name);

        final boolean force = args.flags().hasFlag("f");
        if (existing != null && !force) {
            source.sendMessage("A set of rules with the name of '" + name
                    + "' already exists. Use the '-f' option to overwrite an existing rule set.");
            return;
        }

        RuleTemplates.copyTemplate(templateName, name);
        source.sendMessage("A new set of rules named '" + name + "' based on the template '" + templateName
                + "' has been generated. Please restart the server to see your changes.");
    }

    @Override
    protected String[] getAliases() {
        return new String[] { "gr" };
    }

    @Override
    public void setupCommand(final Command cmd) {
        cmd.setHelp("Generates a set of rules.");
        cmd.setUsage("<template> [-f] [-n name]");
    }
}
