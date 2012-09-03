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

import gnu.trove.iterator.TIntIterator;
import gnu.trove.iterator.TLongObjectIterator;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TLongObjectHashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;
import org.bson.types.BasicBSONList;

import org.spout.api.util.map.TInt21TripleObjectHashMap;

import com.volumetricpixels.politics.api.Politics;
import com.volumetricpixels.politics.data.Storable;
import com.volumetricpixels.politics.group.Group;
import com.volumetricpixels.politics.group.level.GroupLevel;
import com.volumetricpixels.politics.universe.Universe;
import org.spout.api.Spout;
import org.spout.api.geo.World;

/**
 * Represents a world containing plots.
 */
public class PoliticsWorld implements Storable {
    /**
     * The name of the GroupsWorld.
     */
    private final String name;

    /**
     * The config of the world.
     */
    private final WorldConfig config;

    /**
     * Contains all owners corresponding to their proper positions.
     */
    private final TInt21TripleObjectHashMap<TIntList> owners;

    /**
     * Creates a new GroupsWorld.
     *
     * @param name
     */
    PoliticsWorld(String name, WorldConfig config) {
        this(name, config, new TInt21TripleObjectHashMap<TIntList>());
    }

    /**
     * C'tor
     *
     * @param name
     * @param config
     * @param owners
     */
    private PoliticsWorld(String name, WorldConfig config, TInt21TripleObjectHashMap<TIntList> owners) {
        this.name = name;
        this.config = config;
        this.owners = owners;
    }

    /**
     * Gets the name of the GroupsWorld.
     *
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the configuration of this world.
     *
     * @return
     */
    public WorldConfig getConfig() {
        return config;
    }

    /**
     * Gets the World of this PoliticsWorld.
     *
     * @return
     */
    public World getWorld() {
        return Spout.getEngine().getWorld(name);
    }

    /**
     * Gets the internal list of owners of a given location. Creates this list
     * if it doesn't exist.
     *
     * @param x
     * @param y
     * @param z
     * @return the internal list of owners for given location
     */
    TIntList getInternalOwnerList(int x, int y, int z) {
        TIntList list = owners.get(x, y, z);
        if (list == null) {
            list = new TIntArrayList();
            owners.put(x, y, z, list);
        }
        return list;
    }

    /**
     * Gets the list of owners at the given location.
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public TIntList getOwnerIds(int x, int y, int z) {
        return new TIntArrayList(getInternalOwnerList(x, y, z));
    }

    /**
     * Gets the owners of a given plot location within this world.
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public List<Group> getOwners(int x, int y, int z) {
        TIntList ownerIdList = getInternalOwnerList(x, y, z);
        List<Group> ret = new ArrayList<Group>();
        TIntIterator it = ownerIdList.iterator();
        while (it.hasNext()) {
            int id = it.next();
            Group group = Politics.getUniverseManager().getGroupById(id);
            if (group == null) {
                ownerIdList.remove(id); // Group no longer exists
            } else {
                ret.add(group);
            }
        }
        return ret;
    }

    /**
     * Gets a universe from its GroupLevel.
     *
     * @param level
     * @return
     */
    public Universe getUniverse(GroupLevel level) {
        return Politics.getUniverseManager().getUniverse(this, level);
    }

    /**
     * Gets the plot at the given location.
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public Plot getPlotAt(int x, int y, int z) {
        return new Plot(this, x, y, z);
    }

    /**
     * Gets the plot at the given chunk position.
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public Plot getPlotAtChunkPosition(int x, int y, int z) {
        return getPlotAt(x / config.getPlotSizeX(), y / config.getPlotSizeY(), z / config.getPlotSizeZ());
    }

    /**
     * Gets the GroupLevels within this world.
     *
     * @return
     */
    public List<GroupLevel> getLevels() {
        return Politics.getUniverseManager().getLevelsOfWorld(this);
    }

    @Override
    public BSONObject toBSONObject() {
        BasicBSONObject bson = new BasicBSONObject();
        TLongObjectIterator<TIntList> it = owners.iterator();
        while (it.hasNext()) {
            it.advance();
            String key = Long.toHexString(it.key());
            TIntList theOwners = it.value();
            if (theOwners.isEmpty()) {
                continue; // No point in serializing an empty list
            }
            BasicBSONList theOwnersBson = new BasicBSONList();

            TIntIterator theOwnersIt = theOwners.iterator();
            while (theOwnersIt.hasNext()) {
                long val = theOwnersIt.next();
                theOwnersBson.add(val);
            }

            bson.put(key, theOwnersBson);
        }
        return bson;
    }

    /**
     * Gets a GroupsWorld from a BSON object.
     *
     * @param name
     * @param config
     * @param object
     * @return
     */
    public static PoliticsWorld fromBSONObject(String name, WorldConfig config, BSONObject object) {
        if (!(object instanceof BasicBSONObject)) {
            throw new IllegalArgumentException("object is not a BasicBSONObject!");
        }
        TLongObjectMap<TIntList> ownersIds = new TLongObjectHashMap<TIntList>();
        BasicBSONObject bobject = (BasicBSONObject) object;
        for (Entry<String, Object> entry : bobject.entrySet()) {
            String intStr = entry.getKey();
            int key = Integer.parseInt(intStr, 16);
            Object listVal = entry.getValue();
            if (!(listVal instanceof BasicBSONList)) {
                throw new IllegalArgumentException("listVal is not a BasicBSONList!");
            }

            TIntList longs = new TIntArrayList();
            BasicBSONList list = (BasicBSONList) listVal;
            for (Object obj : list) {
                if (!(obj instanceof Integer)) {
                    throw new IllegalArgumentException("obj is not an Integer!");
                }
                int val = ((Integer) obj).intValue();
                longs.add(val);
            }

            ownersIds.put(key, longs);
        }

        TInt21TripleObjectHashMap<TIntList> owners = new TInt21TripleObjectHashMap<TIntList>(ownersIds);
        return new PoliticsWorld(name, config, owners);
    }
}
