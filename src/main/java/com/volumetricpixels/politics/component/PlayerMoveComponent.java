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
        public void onTick(float dt) {
            if (prev != null && !prev.equals(getOwner().getTransform().getTransform())) {
                Player player = (Player) getOwner();
                Point from = prev.getPosition();
                Point to = getOwner().getTransform().getPosition();

                // Check for chunk movement
                if (from.getChunkX() == to.getChunkX() && from.getChunkY() == to.getChunkY() && from.getChunkZ() == to.getChunkZ()) {
                    return;
                }

                final Plot prev = Politics.getPlotAt(from);
                final Plot now = Politics.getPlotAt(to);

                // Check for different plot
                if (prev.equals(now)) {
                    return;
                }

                // Call event
                final PlayerChangePlotEvent pcpe = PoliticsEventFactory.callPlayerChangePlotEvent(player, prev, now);
                if (pcpe.isCancelled()) {
                    getOwner().getTransform().setPosition(from);
                }

                this.prev = getOwner().getTransform().getTransform();
            }
        }
}
