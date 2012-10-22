package com.volumetricpixels.politics.protection;

import java.util.List;

import org.spout.api.event.EventHandler;
import org.spout.api.event.Listener;
import org.spout.api.event.Order;
import org.spout.api.event.server.protection.EntityCanBreakEvent;

import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.group.Group;
import com.volumetricpixels.politics.world.Plot;

public class PoliticsProtectionServiceListener implements Listener {
    @EventHandler(order = Order.LATEST)
    public void onCanBreak(EntityCanBreakEvent event) {
        Plot plot = Politics.getPlotAt(event.getBlock().getPosition());
        List<Group> owners = plot.getPoliticsWorld().getOwners(plot.getX(), plot.getY(), plot.getZ());

        if (owners.size() == 0) {
            event.setCancelled(false);
        }

        // TODO
    }
}
