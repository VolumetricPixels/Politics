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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.spout.api.entity.Player;
import org.spout.api.geo.Protection;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.discrete.Point;

import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.event.PoliticsEventFactory;
import com.volumetricpixels.politics.event.plot.PlotOwnerChangeEvent;
import com.volumetricpixels.politics.group.Group;
import com.volumetricpixels.politics.group.privilege.Privilege;
import com.volumetricpixels.politics.protection.PoliticsProtectionService;
import com.volumetricpixels.politics.universe.Universe;

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
     * Adds an owner to the plot.
     * 
     * @param group
     * @return True if successful
     */
    public boolean addOwner(Group group) {
        PlotOwnerChangeEvent event = PoliticsEventFactory.callPlotOwnerChangeEvent(this, group.getUid(), true);
        if (event.isCancelled()) {
            return false;
        }

        for (Group g : getOwners()) {
            if (g.equals(group)) {
                return false; // Already owns the plot
            }

            if (g.getUniverse().equals(group.getUniverse()) && group.getParent().equals(g)) {
                removeOwner(g);
                break; // We are a sub-plot
            }
            return false;
        }

        TIntList list = world.getInternalOwnerList(getX(), getY(), getZ());
        return list.add(group.getUid());
    }

    /**
     * Adds an owner to the plot.
     * 
     * @param id
     *            The id of the owner.
     * @return True if successful.s
     */
    public boolean addOwner(int id) {
        return addOwner(Politics.getUniverseManager().getGroupById(id));
    }

    @Override
    public boolean contains(Point point) {
        return chunk.contains(point);
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
     * Gets the Chunk of the Plot
     * 
     * @return The Chunk the Plot is inside
     */
    public Chunk getChunk() {
        return chunk;
    }

    /**
     * Gets the group owning this plot in the given universe.
     * 
     * @param universe
     * @return
     */
    public Group getOwner(Universe universe) {
        for (Group owner : getOwners()) {
            if (owner.getUniverse().equals(universe)) {
                return owner;
            }
        }
        return null;
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
     * Gets the owners of this plot in the universe. It's a chain.
     * 
     * @param universe
     * @return
     */
    public List<Group> getOwners(Universe universe) {
        List<Group> owners = new ArrayList<Group>();
        Group group = getOwner(universe);
        while (group != null) {
            owners.add(group);
            group = group.getParent();
        }
        return owners;
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
     * Gets the privileges of the player.
     * 
     * @param player
     * @return
     */
    public Set<Privilege> getPrivileges(Player player) {
        Set<Privilege> privileges = new HashSet<Privilege>();
        return privileges;
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
     * Returns true if the given owner is an owner of this plot.
     * 
     * @param group
     * @return
     */
    public boolean isOwner(Group group) {
        return isOwner(group.getUid());
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
     * Removes the given owner from this plot's owners.
     * 
     * @param group
     * @return True if successful
     */
    public boolean removeOwner(Group group) {
        return removeOwner(group.getUid());
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
}