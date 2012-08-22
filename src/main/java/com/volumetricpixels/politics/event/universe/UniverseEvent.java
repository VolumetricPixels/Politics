package com.volumetricpixels.politics.event.universe;

import org.spout.api.event.Event;

import com.volumetricpixels.politics.universe.Universe;

/**
 * An event regarding a Universe.
 */
public abstract class UniverseEvent extends Event {
	/**
	 * The Universe
	 */
	protected final Universe universe;

	/**
	 * C'tor
	 * 
	 * @param universe
	 */
	public UniverseEvent(Universe universe) {
		this.universe = universe;
	}

	/**
	 * Gets the universe involved in this event.
	 * 
	 * @return
	 */
	public Universe getUniverse() {
		return universe;
	}
}
