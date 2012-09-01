package com.volumetricpixels.politics.event.group;

import com.volumetricpixels.politics.group.Group;
import org.spout.api.command.CommandSource;
import org.spout.api.event.Cancellable;
import org.spout.api.event.HandlerList;

/**
 *
 * @author simplyianm
 */
public final class GroupCreateEvent extends GroupEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();

	/**
	 * The creator of the group.
	 */
	private final CommandSource creator;

	/**
	 * C'tor
	 *
	 * @param group
	 * @param creator
	 */
	public GroupCreateEvent(Group group, CommandSource creator) {
		super(group);
		this.creator = creator;
	}

	/**
	 * Gets the creator of the group.
	 *
	 * @return
	 */
	public CommandSource getCreator() {
		return creator;
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
