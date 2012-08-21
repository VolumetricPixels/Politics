/*
 * This file is part of Colonies.
 *
 * Copyright (c) 2012-2012, THEDevTeam <http://thedevteam.org/>
 * Colonies is licensed under the Apache License Version 2.
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
package com.simplyian.colonies.plot;

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

import com.simplyian.colonies.Colonies;
import com.simplyian.colonies.colony.Colony;
import com.simplyian.colonies.colony.ColonyLevel;
import com.simplyian.colonies.data.Storable;
import com.simplyian.colonies.universe.Universe;

/**
 * Represents a world containing plots.
 */
public class ColoniesWorld implements Storable {
	/**
	 * The name of the ColoniesWorld.
	 */
	private final String name;

	/**
	 * Contains all owners corresponding to their proper positions.
	 */
	private final TInt21TripleObjectHashMap<TLongList> owners;

	/**
	 * C'tor
	 * 
	 * @param owners
	 */
	private ColoniesWorld(String name, TInt21TripleObjectHashMap<TLongList> owners) {
		this.name = name;
		this.owners = owners;
	}

	/**
	 * Gets the name of the ColoniesWorld.
	 * 
	 * @return
	 */
	public String getName() {
		return this.name;
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
		return new TLongArrayList(owners.get(x, y, z));
	}

	/**
	 * Gets the owners of a given plot location within this world.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public List<Colony> getOwners(int x, int y, int z) {
		TLongList ownerIdList = owners.get(x, y, z);
		List<Colony> ret = new ArrayList<Colony>();
		TLongIterator it = ownerIdList.iterator();
		while (it.hasNext()) {
			long id = it.next();
			Colony colony = Colonies.getUniverseManager().getColonyById(id);
			if (colony == null) {
				ownerIdList.remove(id); // Colony no longer exists
			} else {
				ret.add(colony);
			}
		}
		return ret;
	}

	/**
	 * Gets a universe from its ColonyLevel.
	 * 
	 * @param level
	 * @return
	 */
	public Universe getUniverse(ColonyLevel level) {
		return Colonies.getUniverseManager().getUniverse(this, level);
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
	 * Gets a ColoniesWorld from a BSON object.
	 * 
	 * @param name
	 * @param object
	 * @return
	 */
	public static ColoniesWorld fromBSONObject(String name, BSONObject object) {
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
		return new ColoniesWorld(name, owners);
	}
}
