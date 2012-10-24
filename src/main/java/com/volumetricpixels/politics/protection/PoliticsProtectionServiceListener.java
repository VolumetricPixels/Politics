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
package com.volumetricpixels.politics.protection;

import java.util.List;

import org.spout.api.event.EventHandler;
import org.spout.api.event.Listener;
import org.spout.api.event.Order;
import org.spout.api.event.server.protection.EntityCanBreakEvent;

import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.group.Group;
import com.volumetricpixels.politics.world.Plot;

/**
 * Deals with Plot protections in Politics
 */
public class PoliticsProtectionServiceListener implements Listener {
    @EventHandler(order = Order.LATEST)
    public void onEntityCanBreak(final EntityCanBreakEvent event) {
        final Plot plot = Politics.getPlotAt(event.getBlock().getPosition());
        final List<Group> owners = plot.getPoliticsWorld().getOwners(plot.getX(), plot.getY(), plot.getZ());

        if (owners.size() == 0) {
            event.setCancelled(false);
        }

        // TODO
    }
}
