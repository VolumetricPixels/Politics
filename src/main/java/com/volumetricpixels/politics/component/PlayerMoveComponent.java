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
package com.volumetricpixels.politics.component;

import org.spout.api.component.type.EntityComponent;
import org.spout.api.entity.Player;
import org.spout.api.geo.discrete.Point;
import org.spout.api.geo.discrete.Transform;

import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.event.PoliticsEventFactory;
import com.volumetricpixels.politics.event.player.PlayerChangePlotEvent;
import com.volumetricpixels.politics.world.Plot;

public class PlayerMoveComponent extends EntityComponent {
    private Transform prev;

    @Override
    public void onTick(final float dt) {
        if (prev != null && !prev.equals(getOwner().getScene().getTransform())) {
            final Player player = (Player) getOwner();
            final Point from = prev.getPosition();
            final Point to = getOwner().getScene().getPosition();

            final Plot prev = Politics.getPlotAt(from);
            final Plot now = Politics.getPlotAt(to);

            // Check for different plot

            if (prev == now || prev == null || prev.equals(now)) {
                return;
            }

            // Call event
            final PlayerChangePlotEvent pcpe = PoliticsEventFactory.callPlayerChangePlotEvent(player, prev, now);
            if (pcpe.isCancelled()) {
                getOwner().getScene().setPosition(from);
            }

            this.prev = getOwner().getScene().getTransform();
        }
    }
}
