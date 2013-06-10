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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

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
            return PropertySerializer.toString(obj);
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
            return PropertySerializer.fromString(string);
        } catch (final ClassNotFoundException e) {
            throw new PropertyDeserializationException("Could not find the object class for the given string while deserializing!", e);
        } catch (final IOException e) {
            throw new PropertyDeserializationException("IOException occurred while deserializing a string!", e);
        }
    }

    /**
     * Converts a Serializable Object into a Serialized String that is encoded
     * in Base64.
     * 
     * Can be used along with fromString(String) to store Objects as Strings and
     * later get them back. Please note this can be used to save to Files as
     * well, meaning it is a great way for storing Objects without loads of
     * manual parsing and creation of objects.
     * 
     * @param o
     *            The Serializable Object to stringify
     * @return The Serialized String
     * 
     * @throws IOException
     *             When there is a write error
     */
    private static String toString(final Serializable o) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.close();
        final byte[] byteArray = baos.toByteArray();
        return new String(Base64Coder.encode(byteArray, 0, byteArray.length));
    }

    /**
     * Uses Base64 along with Java's Serialization API to get an Object from a
     * Serialized Base64 String.
     * 
     * Please note the input String should be encoded in Base64. If you use the
     * toString(Serializable) method, the result will be encoded in Base64, so
     * as long as you got the Serialized String via toString(Serializable) in
     * the first place, everything should work.
     * 
     * You can cast the returned Object to whatever type the String was from in
     * the first place.
     * 
     * @param s
     *            The String to convert
     * @return The Object the String was converted to
     * 
     * @throws IOException
     *             If there is a read error
     * @throws ClassNotFoundException
     *             If the written Object's class is not found
     */
    private static Object fromString(final String s) throws IOException, ClassNotFoundException {
        final byte[] data = Base64Coder.decode(s.toCharArray(), 0, s.length());
        final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
        final Object o = ois.readObject();
        ois.close();
        return o;
    }

    private PropertySerializer() {
    }
}
