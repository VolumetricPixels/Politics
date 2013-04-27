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
package com.volumetricpixels.politics.command.politics;

import com.volumetricpixels.politics.command.PCommand;

/**
 * Politics administration command.
 */
public abstract class PoliticsCommand extends PCommand {

    /**
     * C'tor
     * 
     * @param primary
     *            The primary command label
     */
    public PoliticsCommand(final String primary) {
        super(primary.toLowerCase());
    }

    @Override
    protected String[] getPermissions() {
        return new String[] { "politics.politics." + primary };
    }
}
