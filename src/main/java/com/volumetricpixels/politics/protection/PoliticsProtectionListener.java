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

import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.event.EventHandler;
import org.spout.api.event.Listener;
import org.spout.api.event.Order;
import org.spout.api.event.server.protection.EntityCanBreakEvent;
import org.spout.api.event.server.protection.EntityCanBuildEvent;

import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.group.Group;
import com.volumetricpixels.politics.group.privilege.GroupPlotPrivileges;
import com.volumetricpixels.politics.world.AbstractPlot;
import com.volumetricpixels.politics.world.ChunkPlot;

/**
 * Deals with ChunkPlot protections in Politics
 */
public class PoliticsProtectionListener implements Listener {
    @EventHandler(order = Order.LATEST)
    public void onEntityCanBreak(final EntityCanBreakEvent event) {
        final Entity entity = event.getEntity();
        final AbstractPlot plot = Politics.getPlotAt(event.getBlock().getPosition());
        final List<Group> owners = plot.getPoliticsWorld().getOwners(plot.getBasePoint().getBlockX(), plot.getBasePoint().getBlockY(), plot.getBasePoint().getBlockZ());

        if (owners.isEmpty()) {
            return;
        }

        if (entity instanceof Player) {
            Player player = (Player) entity;
            for (Group group : owners) {
                if (!group.can(player, GroupPlotPrivileges.BUILD)) {
                    event.setCancelled(true);
                }
            }
        } else {
            // TODO: Configurable entity damage to plots (eg creeper explosions, AI)... Toggleable per plot
        }
    }

    @EventHandler(order = Order.LATEST)
    public void onEntityCanBuild(final EntityCanBuildEvent event) {
        final Entity entity = event.getEntity();
        final AbstractPlot plot = Politics.getPlotAt(event.getPoint());
        final List<Group> owners = plot.getPoliticsWorld().getOwners(plot.getBasePoint().getBlockX(), plot.getBasePoint().getBlockY(), plot.getBasePoint().getBlockZ());

        if (owners.isEmpty()) {
            return;
        }

        if (entity instanceof Player) {
            Player player = (Player) entity;
            for (Group group : owners) {
                if (!group.can(player, GroupPlotPrivileges.BUILD)) {
                    event.setCancelled(true);
                }
            }
        } else {
            // TODO: other entities (such as AI)
        }
    }
}
