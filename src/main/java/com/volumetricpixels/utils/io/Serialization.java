package com.volumetricpixels.utils.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author VolumetricPixels
 */
public final class Serialization {
    /**
     * No instances
     */
    private Serialization() {
    }

    /**
     * Uses Base64 along with Java's Serialization API to get an Object from a
     * Serialized String.
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
    public static Object fromString(String s) throws IOException, ClassNotFoundException {
        byte[] data = Base64Coder.decode(s);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
        Object o = ois.readObject();
        ois.close();
        return o;
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
    public static String toString(Serializable o) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.close();
        return new String(Base64Coder.encode(baos.toByteArray()));
    }
}
