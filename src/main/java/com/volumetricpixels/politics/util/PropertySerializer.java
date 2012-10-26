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

import java.io.IOException;
import java.io.Serializable;

import org.spout.api.geo.discrete.Point;
import org.spout.api.geo.discrete.Transform;

import com.volumetricpixels.utils.io.IOUtils;

import com.volumetricpixels.politics.exception.PropertyDeserializationException;
import com.volumetricpixels.politics.exception.PropertySerializationException;

/**
 * Contains various methods for serializing and deserializing certain properties
 * as strings
 */
public final class PropertySerializer {
    /**
     * Serializes any serializable object to a Base64 encoded String
     * 
     * @param obj
     *            The Serializable object to serialize
     * @return The serialized string
     */
    public static String serialize(final Serializable obj) throws PropertySerializationException {
        try {
            return IOUtils.toString(obj);
        } catch (final IOException e) {
            throw new PropertySerializationException("IOException occurred while serializing an object of type " + obj.getClass().getName() + "!", e);
        }
    }

    /**
     * Deserializes any Base64 encoded string into an object from whence it came
     * 
     * @param string
     *            The serialized String
     * @return The deserialized Object from the given string
     */
    public static Object deserialize(final String string) throws PropertyDeserializationException {
        try {
            return IOUtils.fromString(string);
        } catch (final ClassNotFoundException e) {
            throw new PropertyDeserializationException("Could not find the object class for the given string while deserializing!", e);
        } catch (final IOException e) {
            throw new PropertyDeserializationException("IOException occurred while deserializing a string!", e);
        }
    }

    /**
     * Serializes a point to a string.
     * 
     * @param point
     *            The point to serialize.
     * @return The string representing the serialization.
     */
    public static String serializePoint(final Point point) throws PropertySerializationException {
        return serialize(point);
    }

    /**
     * Deserializes a point from a string.
     * 
     * @param string
     * @return
     * @throws PropertyDeserializationException
     */
    public static Point deserializePoint(final String string) throws PropertyDeserializationException {
        return (Point) deserialize(string);
    }

    /**
     * Serializes a transform to a string.
     * 
     * @param transform
     *            The transform to serialize.
     * @return The string representing the serialization.
     */
    public static String serializeTransform(final Transform transform) throws PropertySerializationException {
        return serialize(transform);
    }

    /**
     * Deserializes a transform from a string.
     * 
     * @param string
     * @return
     * @throws PropertyDeserializationException
     */
    public static Transform deserializeTransform(final String string) throws PropertyDeserializationException {
        return (Transform) deserialize(string);
    }

    private PropertySerializer() {
    }
}
