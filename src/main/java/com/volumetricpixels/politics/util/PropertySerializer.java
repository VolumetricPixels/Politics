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
package com.volumetricpixels.politics.util;

import org.spout.api.Spout;
import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;

/**
 * Contains various methods for serializing and deserializing certain properties
 * as strings.
 */
public class PropertySerializer {
    /**
     * Serializes a point to a string.
     *
     * @param point The point to serialize.
     * @return The string representing the serialization.
     */
    public static String serializePoint(Point point) {
        return new StringBuilder("p/").append(point.getWorld().getName()).append(",").append(point.getX()).append(",").append(point.getY()).append(",").append(point.getZ()).toString();
    }

    /**
     * Deserializes a point from a string.
     *
     * @param string
     * @return
     * @throws PropertyDeserializationException
     */
    public static Point deserializePoint(String string) throws PropertyDeserializationException {
        String[] parts1 = string.split("/");
        if (parts1.length != 2) {
            throw new PropertyDeserializationException("Not a serialized property!");
        }
        if (!parts1[0].equalsIgnoreCase("p")) {
            throw new PropertyDeserializationException("Not a point!");
        }
        String[] whatMatters = parts1[1].split(",");
        if (whatMatters.length <= 4) {
            throw new PropertyDeserializationException("Not enough point data!");
        }

        String world = whatMatters[0];
        World w = Spout.getEngine().getWorld(world);
        if (w == null) {
            throw new PropertyDeserializationException("The world '" + world + "' no longer exists!");
        }

        float x;
        try {
            x = Float.parseFloat(whatMatters[1]);
        } catch (NumberFormatException ex) {
            throw new PropertyDeserializationException("The x is not a float!", ex);
        }

        float y;
        try {
            y = Float.parseFloat(whatMatters[2]);
        } catch (NumberFormatException ex) {
            throw new PropertyDeserializationException("The y is not a float!", ex);
        }

        float z;
        try {
            z = Float.parseFloat(whatMatters[3]);
        } catch (NumberFormatException ex) {
            throw new PropertyDeserializationException("The z is not a float!", ex);
        }
        return new Point(w, x, y, z);
    }

    /**
     * Serializes a block's point to a string.
     *
     * @param point The point of the block to serialize.
     * @return The string representing the serialization.
     */
    public static String serializeBlock(Point point) {
        return new StringBuilder("b/").append(point.getWorld().getName()).append(",").append(Integer.toHexString(point.getBlockX())).append(",").append(Integer.toHexString(point.getBlockY())).append(",").append(Integer.toHexString(point.getBlockZ())).toString();
    }

    /**
     * Deserializes a block's point from a string.
     *
     * @param string
     * @return
     * @throws PropertyDeserializationException
     */
    public static Point deserializeBlock(String string) throws PropertyDeserializationException {
        String[] parts1 = string.split("/");
        if (parts1.length != 2) {
            throw new PropertyDeserializationException("Not a serialized property!");
        }
        if (!parts1[0].equalsIgnoreCase("b")) {
            throw new PropertyDeserializationException("Not a block!");
        }
        String[] whatMatters = parts1[1].split(",");
        if (whatMatters.length <= 4) {
            throw new PropertyDeserializationException("Not enough block data!");
        }

        String world = whatMatters[0];
        World w = Spout.getEngine().getWorld(world);
        if (w == null) {
            throw new PropertyDeserializationException("The world '" + world + "' no longer exists!");
        }

        int x;
        try {
            x = Integer.parseInt(whatMatters[1], 16);
        } catch (NumberFormatException ex) {
            throw new PropertyDeserializationException("The x is not an int!", ex);
        }

        int y;
        try {
            y = Integer.parseInt(whatMatters[2], 16);
        } catch (NumberFormatException ex) {
            throw new PropertyDeserializationException("The y is not an int!", ex);
        }

        int z;
        try {
            z = Integer.parseInt(whatMatters[3], 16);
        } catch (NumberFormatException ex) {
            throw new PropertyDeserializationException("The z is not an int!", ex);
        }
        return new Point(w, x, y, z);
    }
}
