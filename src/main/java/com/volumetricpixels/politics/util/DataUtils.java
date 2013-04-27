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
package com.volumetricpixels.politics.util;

import org.spout.api.geo.World;

import org.bson.BasicBSONObject;
import org.bson.types.BasicBSONList;

import com.volumetricpixels.politics.Politics;

public class DataUtils {
    public static World getWorld(final String string) {
        if (string == null) {
            throw new IllegalArgumentException("Unable to get world from '" + string + "'");
        }
        final World world = Politics.getPlugin().getEngine().getWorld(string);
        if (world == null) {
            throw new IllegalArgumentException("'" + string + "' is not a valid world.");
        }
        return world;
    }

    public static BasicBSONList getList(final Object object) {
        if (!(object instanceof BasicBSONList)) {
            throw new IllegalArgumentException("Supplied object is not a list!");
        }
        return (BasicBSONList) object;
    }

    public static BasicBSONObject toBasicBSONObject(final Object object) {
        if (!(object instanceof BasicBSONObject)) {
            throw new IllegalArgumentException("Supplied object is not a BasicBSONObject!");
        }
        return (BasicBSONObject) object;
    }

    private DataUtils() {
    }
}
