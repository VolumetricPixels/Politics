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

import org.bson.BSONObject;
import org.bson.BasicBSONObject;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.discrete.Point;

/**
 * A ChunkPlot wraps around a Chunk as well as storing a PoliticsWorld and
 * owners
 */
public class ChunkPlot extends Plot {

    /**
     * The Chunk the ChunkPlot is in
     */
    private final Chunk chunk;

    /**
     * C'tor
     * 
     * @param world
     * @param x
     * @param y
     * @param z
     */
    ChunkPlot(final PoliticsWorld world, final int x, final int y, final int z) {
        super(world);
        chunk = world.getWorld().getChunk(x, y, z);
    }

    /**
     * C'tor
     * 
     * @param world
     * @param x
     * @param y
     * @param z
     */
    ChunkPlot(final BasicBSONObject object) {
        super(object);
        final Object x = object.get("x");
        final Object y = object.get("y");
        final Object z = object.get("z");
        if (!(x instanceof Integer)) {
            throw new IllegalArgumentException("X was not available.");
        }
        if (!(y instanceof Integer)) {
            throw new IllegalArgumentException("Y was not available.");
        }
        if (!(z instanceof Integer)) {
            throw new IllegalArgumentException("Z was not available.");
        }
        chunk = getPoliticsWorld().getWorld().getChunk((Integer) x, (Integer) y, (Integer) z);
    }

    /**
     * Gets the Chunk of the ChunkPlot
     * 
     * @return The Chunk the ChunkPlot is inside
     */
    public Chunk getChunk() {
        return chunk;
    }

    /**
     * Gets the point at the base of the plot.
     * 
     * @return
     */
    @Override
    public Point getBasePoint() {
        return chunk.getBase();
    }

    @Override
    public boolean contains(final Point point) {
        return chunk.contains(point);
    }

    @Override
    public BSONObject toBSONObject() {
        final BSONObject obj = super.toBSONObject();
        obj.put("x", getX());
        obj.put("y", getY());
        obj.put("z", getZ());
        obj.put("type", Type.CHUNK.name());
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
        final ChunkPlot other = (ChunkPlot) obj;
        if (chunk != other.chunk && (chunk == null || !chunk.equals(other.chunk))) {
            return false;
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + (chunk != null ? chunk.hashCode() : 0);
        return hash;
    }
}
