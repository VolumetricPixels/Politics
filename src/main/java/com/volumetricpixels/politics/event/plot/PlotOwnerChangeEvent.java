package com.volumetricpixels.politics.event.plot;

import com.volumetricpixels.politics.group.Group;
import com.volumetricpixels.politics.plot.Plot;
import org.spout.api.event.Cancellable;
import org.spout.api.event.HandlerList;

/**
 * Called when a plot's owners changes.
 */
public final class PlotOwnerChangeEvent extends PlotEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    /**
     * The creator of the group.
     */
    private final Group group;

    /**
     * C'tor
     *
     * @param group
     * @param creator
     */
    public PlotOwnerChangeEvent(Plot plot, Group group) {
        super(plot);
        this.group = group;
    }

    /**
     * Gets the group that will be the next owner of the plot.
     *
     * @return
     */
    public Group getGroup() {
        return group;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isCancelled() {
        return super.isCancelled();
    }

    /**
     * {@inheritDoc}
     */
    public void setCancelled(boolean cancelled) {
        super.setCancelled(cancelled);
    }

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
