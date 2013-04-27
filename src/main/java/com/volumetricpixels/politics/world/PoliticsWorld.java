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

import java.util.List;

import org.spout.api.geo.World;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;
import org.bson.types.BasicBSONList;

import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.data.Storable;
import com.volumetricpixels.politics.group.Group;
import com.volumetricpixels.politics.group.level.GroupLevel;
import com.volumetricpixels.politics.universe.Universe;
import com.volumetricpixels.politics.util.DataUtils;
import org.spout.api.util.map.concurrent.TSyncInt21TripleObjectHashMap;
import org.spout.api.util.map.concurrent.TripleIntObjectMap;

/**
 * Represents a world containing plots
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
    private final TripleIntObjectMap<Plot> plots;

    /**
     * Creates a new GroupsWorld.
     * 
     * @param name
     */
    PoliticsWorld(final String name, final WorldConfig config) {
        this(name, config, new TSyncInt21TripleObjectHashMap<Plot>());
    }

    /**
     * C'tor
     * 
     * @param name
     * @param config
     * @param owners
     */
    private PoliticsWorld(final String name, final WorldConfig config, final TripleIntObjectMap<Plot> plots) {
        this.name = name;
        this.config = config;
        this.plots = plots;
    }

    /**
     * Gets a GroupsWorld from a BSON object.
     * 
     * @param name
     * @param config
     * @param object
     */
    public PoliticsWorld(final String name, final WorldConfig config, final BasicBSONObject object) {
        this.name = object.getString("name", null);
        plots = new TSyncInt21TripleObjectHashMap<Plot>();
        final BasicBSONList list = DataUtils.getList(object.get("plots"));
        for (final Object o : list) {
            if (!(o instanceof BasicBSONObject)) {
                throw new IllegalArgumentException("List must only contain more objects!");
            }
            final BasicBSONObject plotObj = (BasicBSONObject) o;
            final String string = plotObj.getString("type", null);
            if (string == null || Plot.Type.valueOf(string) == null) {
                throw new IllegalArgumentException("Type is not a recognized string");
            }
            switch (Plot.Type.valueOf(string)) {
                case CHUNK: {
                    final ChunkPlot p = new ChunkPlot(plotObj);
                    plots.put(p.getX(), p.getY(), p.getZ(), p);
                    break;
                }
                case REGION: {
                    final RegionPlot p = new RegionPlot(plotObj);
                    plots.put(p.getX(), p.getY(), p.getZ(), p);
                    break;
                }
                default:
                    throw new IllegalStateException("Was unable to handle Type");
            }
        }
        this.config = config;
    }

    /**
     * Gets the name of the GroupsWorld.
     * 
     * @return
     */
    public String getName() {
        return name;
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
        return Politics.getPlugin().getEngine().getWorld(name);
    }

//    /**
//     * Gets the internal list of owners of a given location. Creates this list
//     * if it doesn't exist.
//     * 
//     * @param x
//     * @param y
//     * @param z
//     * @return the internal list of owners for given location
//     */
//    TIntList getInternalOwnerList(final int x, final int y, final int z) {
//        plots.get(x, y, z).geto
//        TIntList list = owners.get(x, y, z);
//        if (list == null) {
//            list = new TIntArrayList();
//            owners.put(x, y, z, list);
//        }
//        return list;
//    }
//    /**
//     * Gets the list of owners at the given location.
//     * 
//     * @param x
//     * @param y
//     * @param z
//     * @return
//     */
//    public TIntList getOwnerIds(final int x, final int y, final int z) {
//        return new TIntArrayList(getInternalOwnerList(x, y, z));
//    }
    /**
     * Gets the owners of a given plot location within this world.
     * 
     * @param x
     * @param y
     * @param z
     * @return
     */
    public List<Group> getOwners(final int x, final int y, final int z) {
        return plots.get(x, y, z).getOwners();
    }

    /**
     * Gets a universe from its GroupLevel.
     * 
     * @param level
     * @return
     */
    public Universe getUniverse(final GroupLevel level) {
        return Politics.getUniverseManager().getUniverse(this, level);
    }

    /**
     * Gets the plot at the given point.
     * 
     * @param x
     * @param y
     * @param z
     * @return
     */
    public Plot getPlotAt(final int x, final int y, final int z) {
        return plots.get(x, y, z);
    }

    /**
     * Gets the plot at the given chunk position.
     * 
     * @param x
     *            chunkX
     * @param y
     *            chunkY
     * @param z
     *            chunkZ
     * @return
     */
    public ChunkPlot getPlotAtChunkPosition(final int x, final int y, final int z) {
        return new ChunkPlot(this, x, y, z);
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
        final BasicBSONObject bson = new BasicBSONObject();
        bson.put("name", name);
        final BasicBSONList plotList = new BasicBSONList();
        for (final Plot plot : plots.valueCollection()) {
            if (!plot.canStore()) {
                continue;
            }
            plotList.add(plot.toBSONObject());
        }
        bson.put("plots", plots);
        return bson;
    }

    @Override
    public boolean canStore() {
        return true;
    }
}
