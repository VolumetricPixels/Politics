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

import org.spout.api.event.HandlerList;

import com.volumetricpixels.politics.group.Group;

/**
 * Called when a property is set. Note that transforms, points, and blocks will
 * all be strings.
 */
public class GroupPropertySetEvent extends GroupEvent {
    private static final HandlerList handlers = new HandlerList();

    /**
     * The property that was set.
     */
    private final int property;

    /**
     * The value of the property.
     */
    private final Object value;

    /**
     * C'tor
     * 
     * @param group
     * @param value
     *            The value
     */
    public GroupPropertySetEvent(Group group, int property, Object value) {
        super(group);
        this.property = property;
        this.value = value;
    }

    /**
     * Gets the property that will be set.
     * 
     * @return
     */
    public int getProperty() {
        return property;
    }

    /**
     * Gets the value that the property will be set to.
     * 
     * @return
     */
    public Object getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
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
