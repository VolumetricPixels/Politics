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
package com.volumetricpixels.politics.event.plot;

import com.volumetricpixels.politics.group.Group;
import com.volumetricpixels.politics.plot.Plot;
import org.spout.api.event.Cancellable;
import org.spout.api.event.HandlerList;

/**
 * Called when a plot's owners changes.
 */
public final class PlotOwnerChangeEvent extends PlotEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    /**
     * The creator of the group.
     */
    private final Group group;

    /**
     * C'tor
     *
     * @param group
     * @param creator
     */
    public PlotOwnerChangeEvent(Plot plot, Group group) {
        super(plot);
        this.group = group;
    }

    /**
     * Gets the group that will be the next owner of the plot.
     *
     * @return
     */
    public Group getGroup() {
        return group;
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
