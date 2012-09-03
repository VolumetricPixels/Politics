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
package com.volumetricpixels.politics.plot;

import com.volumetricpixels.politics.event.PoliticsEventFactory;
import com.volumetricpixels.politics.event.plot.PlotOwnerChangeEvent;
import gnu.trove.list.TIntList;

import java.util.List;

import com.volumetricpixels.politics.group.Group;
import com.volumetricpixels.politics.group.level.GroupLevel;
import org.spout.api.geo.discrete.Point;

/**
 * GroupsWorld wrapper.
 */
public class Plot {
    /**
     * World of the plot.
     */
    private final PoliticsWorld world;

    /**
     * X of the plot.
     */
    private final int x;

    /**
     * Y of the plot.
     */
    private final int y;

    /**
     * Z of the plot.
     */
    private final int z;

    /**
     * C'tor
     *
     * @param world
     * @param x
     * @param y
     * @param z
     */
    Plot(PoliticsWorld world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * @return the world
     */
    public PoliticsWorld getWorld() {
        return world;
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @return the z
     */
    public int getZ() {
        return z;
    }

    /**
     * Gets the point at the base of the plot.
     *
     * @return
     */
    public Point getBase() {
        return new Point(world.getConfig().getPlotSizeVector().multiply(16.0), world.getWorld());
    }

    /**
     * Gets the IDs of the owners of the plot.
     *
     * @return
     */
    public TIntList getOwnerIds() {
        return world.getOwnerIds(x, y, z);
    }

    /**
     * Gets the list of the owners of the plot.
     *
     * @return
     */
    public List<Group> getOwners() {
        return world.getOwners(x, y, z);
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
     * @param id
     * @return True if successful
     */
    public boolean addOwner(int id) {
        TIntList list = world.getInternalOwnerList(x, y, z);
        if (list.contains(id)) {
            return true; // Already added
        }
        PlotOwnerChangeEvent event = PoliticsEventFactory.callPlotOwnerChangeEvent(this, id, true);
        if (event.isCancelled()) {
            return false;
        }
        list.add(id);
        return true;
    }

    /**
     * Adds an owner to the plot.
     *
     * @param group
     * @return True if successful
     */
    public boolean addOwner(Group group) {
        return addOwner(group.getUid());
    }

    /**
     * Removes an owner from the plot.
     *
     * @param id
     * @return True if successful
     */
    public boolean removeOwner(int id) {
        TIntList list = world.getInternalOwnerList(x, y, z);
        if (!list.contains(id)) {
            return true; // Not in there
        }
        PlotOwnerChangeEvent event = PoliticsEventFactory.callPlotOwnerChangeEvent(this, id, true);
        if (event.isCancelled()) {
            return false;
        }
        list.remove(id);
        return true;
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
        return world.getInternalOwnerList(x, y, z).contains(id);
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
}
