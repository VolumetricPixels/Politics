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

import gnu.trove.iterator.TLongIterator;
import gnu.trove.iterator.TLongObjectIterator;
import gnu.trove.list.TLongList;
import gnu.trove.list.array.TLongArrayList;
import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TLongObjectHashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;
import org.bson.types.BasicBSONList;
import org.spout.api.util.map.TInt21TripleObjectHashMap;

import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.data.Storable;
import com.volumetricpixels.politics.group.Group;
import com.volumetricpixels.politics.group.GroupLevel;
import com.volumetricpixels.politics.universe.Universe;

/**
 * Represents a world containing plots.
 */
public class PoliticsWorld implements Storable {
	/**
	 * The name of the GroupsWorld.
	 */
	private final String name;

	/**
	 * Contains all owners corresponding to their proper positions.
	 */
	private final TInt21TripleObjectHashMap<TLongList> owners;

	/**
	 * Creates a new GroupsWorld.
	 * 
	 * @param name
	 */
	PoliticsWorld(String name) {
		this(name, new TInt21TripleObjectHashMap<TLongList>());
	}

	/**
	 * C'tor
	 * 
	 * @param owners
	 */
	private PoliticsWorld(String name, TInt21TripleObjectHashMap<TLongList> owners) {
		this.name = name;
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
	 * Gets the internal list of owners of a given location. Creates this list
	 * if it doesn't exist.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	TLongList getInternalOwnerList(int x, int y, int z) {
		TLongList list = owners.get(x, y, z);
		if (list == null) {
			list = new TLongArrayList();
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
	public TLongList getOwnerIds(int x, int y, int z) {
		return new TLongArrayList(getInternalOwnerList(x, y, z));
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
		TLongList ownerIdList = getInternalOwnerList(x, y, z);
		List<Group> ret = new ArrayList<Group>();
		TLongIterator it = ownerIdList.iterator();
		while (it.hasNext()) {
			long id = it.next();
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

	@Override
	public BSONObject toBSONObject() {
		BasicBSONObject bson = new BasicBSONObject();
		TLongObjectIterator<TLongList> it = owners.iterator();
		while (it.hasNext()) {
			it.advance();
			String key = Long.toHexString(it.key());
			TLongList theOwners = it.value();
			if (theOwners.isEmpty()) {
				continue; // No point in serializing an empty list
			}
			BasicBSONList theOwnersBson = new BasicBSONList();

			TLongIterator theOwnersIt = theOwners.iterator();
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
	 * @param object
	 * @return
	 */
	public static PoliticsWorld fromBSONObject(String name, BSONObject object) {
		if (!(object instanceof BasicBSONObject)) {
			throw new IllegalArgumentException("object is not a BasicBSONObject!");
		}
		TLongObjectMap<TLongList> ownersLongs = new TLongObjectHashMap<TLongList>();
		BasicBSONObject bobject = (BasicBSONObject) object;
		for (Entry<String, Object> entry : bobject.entrySet()) {
			String longStr = entry.getKey();
			long key = Long.parseLong(longStr, 16);
			Object listVal = entry.getValue();
			if (!(listVal instanceof BasicBSONList)) {
				throw new IllegalArgumentException("listVal is not a BasicBSONList!");
			}

			TLongList longs = new TLongArrayList();
			BasicBSONList list = (BasicBSONList) listVal;
			for (Object obj : list) {
				if (!(obj instanceof Long)) {
					throw new IllegalArgumentException("obj is not a Long!");
				}
				long val = ((Long) obj).longValue();
				longs.add(val);
			}

			ownersLongs.put(key, longs);
		}

		TInt21TripleObjectHashMap<TLongList> owners = new TInt21TripleObjectHashMap<TLongList>(ownersLongs);
		return new PoliticsWorld(name, owners);
	}
}
