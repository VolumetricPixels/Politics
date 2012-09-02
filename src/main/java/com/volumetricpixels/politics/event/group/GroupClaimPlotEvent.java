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

import com.volumetricpixels.politics.group.Group;
import com.volumetricpixels.politics.plot.Plot;
import org.spout.api.command.CommandSource;
import org.spout.api.event.Cancellable;
import org.spout.api.event.HandlerList;

/**
 * Called when a group tries to claim a plot.
 */
public class GroupClaimPlotEvent extends GroupPlotEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    /**
     * The claimer of the plot.
     */
    private final CommandSource claimer;

    /**
     * C'tor
     *
     * @param group
     * @param claimer
     */
    public GroupClaimPlotEvent(Group group, Plot plot, CommandSource claimer) {
        super(group, plot);
        this.claimer = claimer;
    }

    /**
     * Gets the creator of the group.
     *
     * @return
     */
    public CommandSource getClaimer() {
        return claimer;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isCancelled() {
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
