package com.simplyian.colonies.plot;

import java.util.HashMap;
import java.util.Map;

import org.spout.api.geo.World;

/**
 * Manages plots.
 */
public class PlotManager {
	/**
	 * World names mapped to ColonyWorlds.
	 */
	private Map<String, ColoniesWorld> worlds = new HashMap<String, ColoniesWorld>();

	/**
	 * Gets a ColonyWorld from its name.
	 * 
	 * @param name
	 * @return
	 */
	public ColoniesWorld getWorld(String name) {
		return worlds.get(name);
	}

	/**
	 * Gets a ColonyWorld from its World.
	 * 
	 * @param world
	 * @return
	 */
	public ColoniesWorld getWorld(World world) {
		return getWorld(world.getName());
	}

}
