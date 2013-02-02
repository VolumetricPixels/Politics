package com.volumetricpixels.politics.util;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;
import org.bson.types.BasicBSONList;
import org.spout.api.Spout;
import org.spout.api.geo.World;

public class DataUtils {
    public static World getWorld(String string) {
        if (string == null) {
            throw new IllegalArgumentException("Unable to get world from '" + string + "'");
        }
        World world = Spout.getEngine().getWorld(string);
        if (world == null) {
            throw new IllegalArgumentException("'" + string + "' is not a valid world.");
        }
        return world;
    }
    
    public static BasicBSONList getList(Object object) {
        if (!(object instanceof BasicBSONList)) {
            throw new IllegalArgumentException("Supplied object is not a list!");
        }
        return (BasicBSONList) object;
    }
    
    public static BasicBSONObject toBasicBSONObject(Object object) {
        if (!(object instanceof BasicBSONObject)) {
            throw new IllegalArgumentException("Supplied object is not a BasicBSONObject!");
        }
        return (BasicBSONObject) object;
    } 

    private DataUtils() {
    }
}
