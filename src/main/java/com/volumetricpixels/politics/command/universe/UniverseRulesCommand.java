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

import java.util.List;

import org.spout.api.command.Command;
import org.spout.api.command.CommandArguments;
import org.spout.api.command.CommandSource;
import org.spout.api.exception.CommandException;

import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.universe.UniverseRules;

/**
 * Lists available rules.
 */
public class UniverseRulesCommand extends UniverseCommand {
    /**
     * C'tor
     */
    public UniverseRulesCommand() {
        super("rules");
    }

    @Override
    public void execute(final CommandSource source, final Command command, final CommandArguments args) throws CommandException {
        final List<UniverseRules> ruleList = Politics.getUniverseManager().listRules();
        for (final UniverseRules rules : ruleList) {
            source.sendMessage(rules.getName() + " - " + rules.getDescription());
        }
    }

    @Override
    protected String[] getAliases() {
        return new String[] { "r" };
    }

    @Override
    public void setupCommand(final Command cmd) {
        cmd.setHelp("Lists all possible rules to use.");
        cmd.setUsage("");
    }
}
