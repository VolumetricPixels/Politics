package com.simplyian.colonies.universe;

import java.util.HashMap;
import java.util.Map;

import com.simplyian.colonies.colony.ColonyLevel;

/**
 * Represents the sum of the properties of a given universe. In essence, it is a
 * configuration object.
 * 
 * <p>
 * "...the conclusion is not so much that the Universe is fine-tuned for fun
 * gameplay; rather it is fine-tuned for the building blocks and environments
 * that fun gameplay requires". - adapted from Paul Davies
 * </p>
 */
public class UniverseProperties {
	/**
	 * The ColonyLevels apparent in this Universe.
	 */
	private Map<String, ColonyLevel> colonyLevels;

	/**
	 * C'tor
	 */
	public UniverseProperties() {
		// TODO load
		colonyLevels = new HashMap<String, ColonyLevel>();
	}

	/**
	 * Gets the ColonyLevel with the given name.
	 * 
	 * @param name
	 * @return
	 */
	public ColonyLevel getColonyLevel(String name) {
		return colonyLevels.get(name.toLowerCase());
	}
}
