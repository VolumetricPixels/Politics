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

import org.spout.api.event.Cancellable;
import org.spout.api.event.HandlerList;

import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.group.Group;
import com.volumetricpixels.politics.world.Plot;

/**
 * Called when a plot's owners changes
 */
public final class PlotOwnerChangeEvent extends PlotEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    /**
     * The creator of the group.
     */
    private final int group;

    /**
     * True if this adds a group.
     */
    private final boolean add;

    /**
     * C'tor
     * 
     * @param plot
     * @param group
     * @param add
     */
    public PlotOwnerChangeEvent(final Plot plot, final int group, final boolean add) {
        super(plot);
        this.group = group;
        this.add = add;
    }

    /**
     * Gets the id of the group that will change ownership.
     * 
     * @return
     */
    public int getGroupId() {
        return group;
    }

    /**
     * Gets the group that will change ownership.
     * 
     * @return
     */
    public Group getGroup() {
        return Politics.getUniverseManager().getGroupById(group);
    }

    /**
     * Returns true if this adds the group.
     * 
     * @return
     */
    public boolean isAdd() {
        return add;
    }

    /**
     * Returns true if this removes the group.
     * 
     * @return
     */
    public boolean isRemove() {
        return !add;
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
     * Gets the HandlerList of the event.
     * 
     * @return
     */
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
