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
package com.volumetricpixels.politics.event.player;

import org.spout.api.entity.Player;
import org.spout.api.event.Cancellable;
import org.spout.api.event.HandlerList;
import org.spout.api.event.player.PlayerEvent;

import com.volumetricpixels.politics.plot.Plot;

/**
 * Called when a player changes the plot they are in.
 */
public final class PlayerChangePlotEvent extends PlayerEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();

	/**
	 * From plot
	 */
	private final Plot from;

	/**
	 * To plot
	 */
	private final Plot to;

	/**
	 * C'tor
	 * 
	 * @param p
	 * @param from
	 * @param to
	 */
	public PlayerChangePlotEvent(Player p, Plot from, Plot to) {
		super(p);
		this.from = from;
		this.to = to;
	}

	/**
	 * Gets the plot the player left.
	 * 
	 * @return
	 */
	public Plot getFrom() {
		return from;
	}

	/**
	 * Gets the plot the player entered.
	 * 
	 * @return
	 */
	public Plot getTo() {
		return to;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isCancelled(boolean cancelled) {
		return super.isCancelled();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setCancelled(boolean cancelled) {
		super.setCancelled(cancelled);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	/**
	 * Gets the HandlerList of the event.
	 * 
	 * @return
	 */
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
