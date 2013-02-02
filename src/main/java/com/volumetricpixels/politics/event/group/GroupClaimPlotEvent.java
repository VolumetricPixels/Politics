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
package com.volumetricpixels.politics.event.group;

import org.spout.api.command.CommandSource;
import org.spout.api.event.Cancellable;
import org.spout.api.event.HandlerList;

import com.volumetricpixels.politics.group.Group;
import com.volumetricpixels.politics.world.Plot;

/**
 * Called when a {@link Group} tries to claim a {@link Plot}.
 */
public class GroupClaimPlotEvent extends GroupPlotEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    /**
     * The person claiming the {@link Plot} for the {@link Group}
     */
    private final CommandSource claimer;

    /**
     * C'tor
     * 
     * @param group
     *            The {@link Group} claiming the {@link Plot}
     * @param plot
     *            The {@link Plot} being claimed
     * @param claimer
     *            The person claiming the {@link Plot} for the {@link Group}
     */
    public GroupClaimPlotEvent(final Group group, final Plot plot, final CommandSource claimer) {
        super(group, plot);
        this.claimer = claimer;
    }

    /**
     * Gets the claimer of the plot.
     * 
     * @return The {@link CommandSender} claiming the {@link Plot} for the
     *         {@link Group}
     */
    public CommandSource getClaimer() {
        return claimer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCancelled() {
        return super.isCancelled();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCancelled(final boolean cancelled) {
        super.setCancelled(cancelled);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Gets the event's handlers
     * 
     * @return The {@link HandlerList} for the event
     */
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
