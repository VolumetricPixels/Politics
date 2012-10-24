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
package com.volumetricpixels.politics;

import org.spout.api.entity.Player;
import org.spout.api.event.EventHandler;
import org.spout.api.event.Listener;
import org.spout.api.event.Order;
import org.spout.api.event.entity.EntityMoveEvent;
import org.spout.api.geo.discrete.Point;

import com.volumetricpixels.politics.event.PoliticsEventFactory;
import com.volumetricpixels.politics.event.player.PlayerChangePlotEvent;
import com.volumetricpixels.politics.world.Plot;

/**
 * The Listener of Politics
 * 
 * Used for calling custom events and tracking and/or cancelling other events
 */
public class PoliticsListener implements Listener {
    @EventHandler(order = Order.EARLIEST)
    public void checkPlotChange(final EntityMoveEvent event) {
        if (!(event.getEntity() instanceof Player) || event.isCancelled()) {
            return;
        }

        final Point from = event.getFrom();
        final Point to = event.getTo();

        // Check for chunk movement
        if (from.getChunkX() != to.getChunkX() || from.getChunkY() != to.getChunkY() || from.getChunkZ() != to.getChunkZ()) {
            return;
        }

        final Player player = (Player) event.getEntity();
        final Plot prev = Politics.getPlotAt(from);
        final Plot now = Politics.getPlotAt(to);

        // Check for different plot
        if (prev.equals(now)) {
            return;
        }

        // Call event
        final PlayerChangePlotEvent pcpe = PoliticsEventFactory.callPlayerChangePlotEvent(player, prev, now);
        if (pcpe.isCancelled()) {
            event.setCancelled(true);
        }
    }
}
