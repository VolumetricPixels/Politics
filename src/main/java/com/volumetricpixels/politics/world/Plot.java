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

import gnu.trove.iterator.TIntIterator;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.spout.api.entity.Player;
import org.spout.api.geo.discrete.Point;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;
import org.bson.types.BasicBSONList;

import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.data.Storable;
import com.volumetricpixels.politics.event.PoliticsEventFactory;
import com.volumetricpixels.politics.event.plot.PlotOwnerChangeEvent;
import com.volumetricpixels.politics.group.Group;
import com.volumetricpixels.politics.group.privilege.Privilege;
import com.volumetricpixels.politics.universe.Universe;
import com.volumetricpixels.politics.util.DataUtils;

/**
 * A Plot wraps around a Chunk as well as storing a PoliticsWorld and owners
 */
public abstract class Plot implements Storable {
    /**
     * World of the plot
     */
    private final PoliticsWorld world;
    /**
     * Owners of this plot
     */
    private final TIntList owners;

    /**
     * C'tor
     * 
     * @param world
     * @param x
     * @param y
     * @param z
     */
    Plot(final PoliticsWorld world) {
        this(world, new TIntArrayList());
    }

    /**
     * C'tor
     * 
     * @param world
     * @param x
     * @param y
     * @param z
     */
    Plot(final PoliticsWorld world, final TIntList owners) {
        this.world = world;
        this.owners = owners;
    }

    /**
     * C'tor
     * 
     * @param world
     * @param x
     * @param y
     * @param z
     */
    Plot(final BasicBSONObject object) {
        world = Politics.getWorld(DataUtils.getWorld(object.getString("world", null)));
        if (object.containsField("owners")) {
            owners = (TIntList) object.get("owners"); // TODO does this work?
        } else {
            owners = new TIntArrayList();
        }

        // based on toBSONObject(), below code never made sense
//      final BasicBSONList list = DataUtils.getList(object);
//      final TIntList tList = new TIntArrayList();
//      for (final Object obj : list) {
//          if (!(obj instanceof Integer)) {
//              throw new IllegalArgumentException("obj is not an Integer!"); 
//          }
//          final int val = (Integer) obj;
//          tList.add(val);
//      }
//      owners = tList;
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
     * Gets the x coordinate of the Plot
     * 
     * @return The Plot's x coordinate
     */
    public final int getX() {
        return getBasePoint().getBlockX();
    }

    /**
     * Gets the y coordinate of the Plot
     * 
     * @return The Plot's y coordinate
     */
    public final int getY() {
        return getBasePoint().getBlockY();
    }

    /**
     * Gets the z coordinate of the Plot
     * 
     * @return The Plot's Chunk's z coordinate
     */
    public final int getZ() {
        return getBasePoint().getBlockZ();
    }

    /**
     * Gets the point at the base of the Plot as determined by the specific type
     * of Plot This is used for saving and is completely up to implementation.
     * 
     * @return
     */
    public abstract Point getBasePoint();

    /**
     * Gets the IDs of the owners of the plot.
     * 
     * @return
     */
    public TIntList getOwnerIds() {
        return owners;
    }

    /**
     * Gets the list of the owners of the plot.
     * 
     * @return
     */
    public List<Group> getOwners() {
        final List<Group> ret = new ArrayList<Group>();
        final TIntIterator it = owners.iterator();
        while (it.hasNext()) {
            final int id = it.next();
            final Group group = Politics.getUniverseManager().getGroupById(id);
            if (group == null) {
                owners.remove(id); // Group no longer exists
            } else {
                ret.add(group);
            }
        }
        return ret;
    }

    /**
     * Gets the group owning this plot in the given universe.
     * 
     * @param universe
     * @return
     */
    public Group getOwner(final Universe universe) {
        for (final Group owner : getOwners()) {
            if (owner.getUniverse().equals(universe)) {
                return owner;
            }
        }
        return null;
    }

    /**
     * Gets the owners of this plot in the universe. It's a chain.
     * 
     * @param universe
     * @return
     */
    public List<Group> getOwners(final Universe universe) {
        final List<Group> owners = new ArrayList<Group>();
        Group group = getOwner(universe);
        while (group != null) {
            owners.add(group);
            group = group.getParent();
        }
        return owners;
    }

    /**
     * Adds an owner to the plot.
     * 
     * @param id
     *            The id of the owner.
     * @return True if successful.s
     */
    public boolean addOwner(final int id) {
        return addOwner(Politics.getUniverseManager().getGroupById(id));
    }

    /**
     * Adds an owner to the plot.
     * 
     * @param group
     * @return True if successful
     */
    public boolean addOwner(final Group group) {
        final PlotOwnerChangeEvent event = PoliticsEventFactory.callPlotOwnerChangeEvent(this, group.getUid(), true);
        if (event.isCancelled()) {
            return false;
        }

        for (final Group g : getOwners()) {
            if (g.equals(group)) {
                return false; // Already owns the plot
            }

            if (g.getUniverse().equals(group.getUniverse()) && group.getParent().equals(g)) {
                removeOwner(g);
                break; // We are a sub-plot
            }
            return false;
        }
        return owners.add(group.getUid());
    }

    /**
     * Removes an owner from the plot.
     * 
     * @param id
     * @return True if successful
     */
    public boolean removeOwner(final int id) {
        if (!owners.contains(id)) {
            return true; // Not in there
        }
        final PlotOwnerChangeEvent event = PoliticsEventFactory.callPlotOwnerChangeEvent(this, id, true);
        if (event.isCancelled()) {
            return false;
        }
        return owners.remove(id);
    }

    /**
     * Removes the given owner from this plot's owners.
     * 
     * @param group
     * @return True if successful
     */
    public boolean removeOwner(final Group group) {
        return removeOwner(group.getUid());
    }

    /**
     * Returns true if the given owner id is an owner of this plot.
     * 
     * @param id
     * @return
     */
    public boolean isOwner(final int id) {
        return owners.contains(id);
    }

    /**
     * Returns true if the given owner is an owner of this plot.
     * 
     * @param group
     * @return
     */
    public boolean isOwner(final Group group) {
        return isOwner(group.getUid());
    }

    /**
     * Gets the privileges of the player.
     * 
     * @param player
     * @return
     */
    public Set<Privilege> getPrivileges(final Player player) {
        final Set<Privilege> privileges = new HashSet<Privilege>();
        // TODO
        return privileges;
    }

    @Override
    public BSONObject toBSONObject() {
        final BasicBSONObject obj = new BasicBSONObject();
        obj.put("world", world.getName());
        obj.put("owners", owners);
        return obj;
    }

    @Override
    public boolean canStore() {
        return true;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Plot other = (Plot) obj;
        if (world != other.world && (world == null || !world.equals(other.world))) {
            return false;
        }
        if (owners != other.owners && (owners == null || !owners.equals(other.owners))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash *= 73 + (world != null ? world.hashCode() : 0) + (owners != null ? owners.hashCode() : 0);
        return hash;
    }

    public abstract boolean contains(final Point point);
}
