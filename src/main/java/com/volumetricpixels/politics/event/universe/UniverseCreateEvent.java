package com.volumetricpixels.politics.event.universe;

import org.spout.api.event.HandlerList;

import com.volumetricpixels.politics.universe.Universe;

/**
 * Called when a universe is created using the universe creation command.
 */
public class UniverseCreateEvent extends UniverseEvent {
	private static HandlerList handlers = new HandlerList();

	/**
	 * C'tor
	 * 
	 * @param universe
	 */
	public UniverseCreateEvent(Universe universe) {
		super(universe);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	/**
	 * Gets the handler list of this event.
	 * 
	 * @return
	 */
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
