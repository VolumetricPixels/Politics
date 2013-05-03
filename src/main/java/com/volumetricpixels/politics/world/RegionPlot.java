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
package com.volumetricpixels.politics.world;

import org.spout.api.geo.cuboid.Cuboid;
import org.spout.api.geo.discrete.Point;
import org.spout.api.math.Vector3;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;

/**
 * A RegionPlot wraps around a cuboid
 */
public class RegionPlot extends Plot {
    private final Cuboid cuboid;

    /**
     * C'tor
     * 
     * @param world
     * @param xSize
     * @param ySize
     * @param zSize
     */
    RegionPlot(final PoliticsWorld world, final Point basePoint, final int xSize, final int ySize, final int zSize) {
        super(world);
        cuboid = new Cuboid(basePoint, new Vector3(xSize, ySize, zSize));
    }

    /**
     * C'tor
     * 
     * @param world
     * @param x
     * @param y
     * @param z
     */
    RegionPlot(final BasicBSONObject object) {
        super(object);
        final Object x = object.get("x");
        final Object y = object.get("y");
        final Object z = object.get("z");
        final Object xSize = object.get("xSize");
        final Object ySize = object.get("ySize");
        final Object zSize = object.get("zSize");
        if (!(x instanceof Integer)) {
            throw new IllegalArgumentException("x was not an Integer.");
        }
        if (!(y instanceof Integer)) {
            throw new IllegalArgumentException("y was not an Integer.");
        }
        if (!(z instanceof Integer)) {
            throw new IllegalArgumentException("z was not an Integer.");
        }
        if (!(xSize instanceof Integer)) {
            throw new IllegalArgumentException("xSize was not  an Integer.");
        }
        if (!(ySize instanceof Integer)) {
            throw new IllegalArgumentException("ySize was not  an Integer.");
        }
        if (!(zSize instanceof Integer)) {
            throw new IllegalArgumentException("zSize was not  an Integer.");
        }
        cuboid = new Cuboid(new Point(getPoliticsWorld().getWorld(), (Integer) x, (Integer) y, (Integer) z), new Vector3((Integer) xSize, (Integer) ySize, (Integer) zSize));
    }

    /**
     * Gets the Cuboid
     * 
     * @return The Cuboid the RegionPlot is inside
     */
    public Cuboid getCuboid() {
        return cuboid;
    }

    /**
     * Gets the point at the base of the plot.
     * 
     * @return
     */
    @Override
    public Point getBasePoint() {
        return cuboid.getBase();
    }

    @Override
    public boolean contains(final Point point) {
        return cuboid.contains(point);
    }

    @Override
    public BSONObject toBSONObject() {
        final BSONObject obj = super.toBSONObject();
        obj.put("x", getX());
        obj.put("y", getY());
        obj.put("z", getZ());
        final Vector3 size = cuboid.getSize();
        obj.put("xSize", size.getX());
        obj.put("ySize", size.getY());
        obj.put("zSize", size.getZ());
        obj.put("type", Type.REGION.name());
        return obj;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RegionPlot other = (RegionPlot) obj;
        if (cuboid != other.cuboid && (cuboid == null || !cuboid.equals(other.cuboid))) {
            return false;
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + (cuboid != null ? cuboid.hashCode() : 0);
        return hash;
    }
}
