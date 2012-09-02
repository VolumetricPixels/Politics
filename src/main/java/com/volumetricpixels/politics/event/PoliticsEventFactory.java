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
package com.volumetricpixels.politics.event;

import com.volumetricpixels.politics.event.group.GroupCreateEvent;
import org.spout.api.Spout;
import org.spout.api.entity.Player;
import org.spout.api.event.Event;

import com.volumetricpixels.politics.event.player.PlayerChangePlotEvent;
import com.volumetricpixels.politics.event.plot.PlotOwnerChangeEvent;
import com.volumetricpixels.politics.event.universe.UniverseCreateEvent;
import com.volumetricpixels.politics.event.universe.UniverseDestroyEvent;
import com.volumetricpixels.politics.group.Group;
import com.volumetricpixels.politics.plot.Plot;
import com.volumetricpixels.politics.universe.Universe;
import org.spout.api.command.CommandSource;

/**
 * Factory for creating events.
 */
public class PoliticsEventFactory {
    /**
     * Calls a new group create event.
     *
     * @param group
     * @param creator
     * @return
     */
    public static GroupCreateEvent callGroupCreateEvent(Group group, CommandSource creator) {
        return callEvent(new GroupCreateEvent(group, creator));
    }

    /**
     * Calls a new plot change event.
     *
     * @param player
     * @param from
     * @param to
     * @return
     */
    public static PlayerChangePlotEvent callPlayerChangePlotEvent(Player player, Plot from, Plot to) {
        return callEvent(new PlayerChangePlotEvent(player, from, to));
    }

    /**
     * Calls a new PlotOwnerChangeEvent.
     *
     * @param plot
     * @param group
     * @return
     */
    public static PlotOwnerChangeEvent callPlotOwnerChangeEvent(Plot plot, Group group) {
        return callEvent(new PlotOwnerChangeEvent(plot, group));
    }

    /**
     * Calls a new universe create event.
     *
     * @param universe
     * @return
     */
    public static UniverseCreateEvent callUniverseCreateEvent(Universe universe) {
        return callEvent(new UniverseCreateEvent(universe));
    }

    /**
     * Calls a new universe destroy event.
     *
     * @param universe
     * @return
     */
    public static UniverseDestroyEvent callUniverseDestroyEvent(Universe universe) {
        return callEvent(new UniverseDestroyEvent(universe));
    }

    /**
     * Calls the given event.
     *
     * @param event
     */
    private static <T extends Event> T callEvent(T event) {
        return Spout.getEngine().getEventManager().callEvent(event);
    }
}
