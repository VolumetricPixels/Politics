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
package com.volumetricpixels.politics.world;

import gnu.trove.list.TIntList;

import java.util.List;

import org.spout.api.geo.Protection;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.discrete.Point;

import com.volumetricpixels.politics.event.PoliticsEventFactory;
import com.volumetricpixels.politics.event.plot.PlotOwnerChangeEvent;
import com.volumetricpixels.politics.group.Group;
import com.volumetricpixels.politics.group.level.GroupLevel;
import com.volumetricpixels.politics.protection.PoliticsProtectionService;

/**
 * A Plot wraps around a Chunk as well as storing a PoliticsWorld and owners
 */
public class Plot extends Protection {
    /**
     * World of the plot.
     */
    private final PoliticsWorld world;

    /**
     * The Chunk the Plot is in
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
    Plot(PoliticsWorld world, int x, int y, int z) {
        super("Plot(" + world.getName() + "," + x + "," + y + "," + z + ")", world.getWorld());
        this.world = world;
        this.chunk = world.getWorld().getChunk(x, y, z);

        PoliticsProtectionService.getInstance().addProtection(this);
    }

    /**
     * Gets the PoliticsWorld this Plot is located in
     *
     * @return This Plot's PoliticsWorld
     */
    public PoliticsWorld getPoliticsWorld() {
        return world;
    }

    /**
     * Gets the Chunk of the Plot
     *
     * @return The Chunk the Plot is inside
     */
    public Chunk getChunk() {
        return chunk;
    }

    /**
     * Gets the x chunk coordinate of the Plot
     *
     * @return The Plot's Chunk's x coordinate
     */
    public int getX() {
        return chunk.getX();
    }

    /**
     * Gets the y chunk coordinate of the Plot
     *
     * @return The Plot's Chunk's y coordinate
     */
    public int getY() {
        return chunk.getY();
    }

    /**
     * Gets the z chunk coordinate of the Plot
     *
     * @return The Plot's Chunk's z coordinate
     */
    public int getZ() {
        return chunk.getZ();
    }

    /**
     * Gets the point at the base of the plot.
     *
     * @return
     */
    public Point getBase() {
        return chunk.getBase();
    }

    /**
     * Gets the IDs of the owners of the plot.
     *
     * @return
     */
    public TIntList getOwnerIds() {
        return world.getOwnerIds(getX(), getY(), getZ());
    }

    /**
     * Gets the list of the owners of the plot.
     *
     * @return
     */
    public List<Group> getOwners() {
        return world.getOwners(getX(), getY(), getZ());
    }

    /**
     * Gets the group owning this plot at the given level.
     *
     * @param level
     * @return
     */
    public Group getOwner(GroupLevel level) {
        for (Group owner : getOwners()) {
            if (owner.getLevel().equals(level)) {
                return owner;
            }
        }
        return null;
    }

    /**
     * Adds an owner to the plot.
     *
     * @param group
     * @return True if successful
     */
    public boolean addOwner(Group group) {
        for (Group g : getOwners()) {
            if (g.equals(group)) {
                return false;
            }

            if (g.getLevel().equals(group.getLevel())) {
                return false;
            }
        }
        TIntList list = world.getInternalOwnerList(getX(), getY(), getZ());
        PlotOwnerChangeEvent event = PoliticsEventFactory.callPlotOwnerChangeEvent(this, group.getUid(), true);
        if (event.isCancelled()) {
            return false;
        }
        return list.add(group.getUid());
    }

    /**
     * Removes an owner from the plot.
     *
     * @param id
     * @return True if successful
     */
    public boolean removeOwner(int id) {
        TIntList list = world.getInternalOwnerList(getX(), getY(), getZ());
        if (!list.contains(id)) {
            return true; // Not in there
        }
        PlotOwnerChangeEvent event = PoliticsEventFactory.callPlotOwnerChangeEvent(this, id, true);
        if (event.isCancelled()) {
            return false;
        }
        return list.remove(id);
    }

    /**
     * Removes the given owner from this plot's owners.
     *
     * @param group
     * @return True if successful
     */
    public boolean removeOwner(Group group) {
        return removeOwner(group.getUid());
    }

    /**
     * Returns true if the given owner id is an owner of this plot.
     *
     * @param id
     * @return
     */
    public boolean isOwner(int id) {
        return world.getInternalOwnerList(getX(), getY(), getZ()).contains(id);
    }

    /**
     * Returns true if the given owner is an owner of this plot.
     *
     * @param group
     * @return
     */
    public boolean isOwner(Group group) {
        return isOwner(group.getUid());
    }

    @Override
    public boolean contains(Point point) {
        return chunk.contains(point);
    }
}
