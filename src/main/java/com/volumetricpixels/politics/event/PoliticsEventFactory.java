/*
 * This file is part of Politics.
 * 
 * Copyright (c) 2012-2012, VolumetricPixels <http://volumetricpixels.com/>
 * Politics is licensed under the Affero General Public License Version 3.
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.volumetricpixels.politics.event;

import org.spout.api.command.CommandSource;
import org.spout.api.entity.Player;
import org.spout.api.event.Event;

import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.event.group.GroupClaimPlotEvent;
import com.volumetricpixels.politics.event.group.GroupCreateEvent;
import com.volumetricpixels.politics.event.group.GroupPropertySetEvent;
import com.volumetricpixels.politics.event.group.GroupUnclaimPlotEvent;
import com.volumetricpixels.politics.event.player.PlayerChangePlotEvent;
import com.volumetricpixels.politics.event.plot.PlotOwnerChangeEvent;
import com.volumetricpixels.politics.event.universe.UniverseCreateEvent;
import com.volumetricpixels.politics.event.universe.UniverseDestroyEvent;
import com.volumetricpixels.politics.group.Group;
import com.volumetricpixels.politics.universe.Universe;
import com.volumetricpixels.politics.world.Plot;

/**
 * Factory for creating events
 */
public final class PoliticsEventFactory {
    /**
     * Calls a new group claim plot event.
     * 
     * @param group
     *            The Group claiming the Plot
     * @param plot
     *            The Plot being claimed
     * @param claimer
     *            The person claiming the Plot for the Group
     * @return The called GroupClaimPlotEvent
     */
    public static GroupClaimPlotEvent callGroupClaimPlotEvent(final Group group, final Plot plot, final CommandSource claimer) {
        return callEvent(new GroupClaimPlotEvent(group, plot, claimer));
    }

    /**
     * Calls a new group property set event.
     * 
     * @param group
     *            The Group of which the property is being set
     * @param property
     *            The type of GroupProperty being set
     * @param value
     *            The value the property is being set to
     * @return The called GroupPropertySetEvent
     */
    public static GroupPropertySetEvent callGroupPropertySetEvent(final Group group, final int property, final Object value) {
        return callEvent(new GroupPropertySetEvent(group, property, value));
    }

    /**
     * Calls a new group unclaim plot event.
     * 
     * @param group
     *            The Group which is unclaiming the Plot
     * @param plot
     *            The Plot which is being unclaimed
     * @param unclaimer
     *            The person who is unclaiming the Plot for the Group
     * @return The called GroupUnclaimPlotEvent
     */
    public static GroupUnclaimPlotEvent callGroupUnclaimPlotEvent(final Group group, final Plot plot, final CommandSource unclaimer) {
        return callEvent(new GroupUnclaimPlotEvent(group, plot, unclaimer));
    }

    /**
     * Calls a new group create event.
     * 
     * @param group
     *            The Group that has been created
     * @param creator
     *            The creator of the Group
     * @return The called GroupCreateEvent
     */
    public static GroupCreateEvent callGroupCreateEvent(final Group group, final CommandSource creator) {
        return callEvent(new GroupCreateEvent(group, creator));
    }

    /**
     * Calls a new plot change event.
     * 
     * @param player
     *            The Player changing Plot
     * @param from
     *            The Plot from which the Player moved
     * @param to
     *            The Plot to which the Player is moving
     * @return The called PlayerChangePlotEvent
     */
    public static PlayerChangePlotEvent callPlayerChangePlotEvent(final Player player, final Plot from, final Plot to) {
        return callEvent(new PlayerChangePlotEvent(player, from, to));
    }

    /**
     * Calls a new PlotOwnerChangeEvent.
     * 
     * @param plot
     *            The Plot of which the owner is changing
     * @param group
     *            The ID of the group the plot belongs to
     * @param add
     *            Whether the owner is being added
     * @return The called PlotOwnerChangeEvent
     */
    public static PlotOwnerChangeEvent callPlotOwnerChangeEvent(final Plot plot, final int group, final boolean add) {
        return callEvent(new PlotOwnerChangeEvent(plot, group, add));
    }

    /**
     * Calls a new universe create event.
     * 
     * @param universe
     *            The universe being created
     * @return The called UniverseCreateEvent
     */
    public static UniverseCreateEvent callUniverseCreateEvent(final Universe universe) {
        return callEvent(new UniverseCreateEvent(universe));
    }

    /**
     * Calls a new universe destroy event.
     * 
     * @param universe
     *            The universe being destroyed
     * @return The called UniverseDestroyEvent
     */
    public static UniverseDestroyEvent callUniverseDestroyEvent(final Universe universe) {
        return callEvent(new UniverseDestroyEvent(universe));
    }

    /**
     * Calls the given event.
     * 
     * @param event
     *            The event to call
     * @return The called Event
     */
    private static <T extends Event> T callEvent(final T event) {
        return Politics.getPlugin().getEngine().getEventManager().callEvent(event);
    }

    private PoliticsEventFactory() {
    }
}
