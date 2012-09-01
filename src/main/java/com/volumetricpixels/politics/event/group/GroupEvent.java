package com.volumetricpixels.politics.event.group;

import com.volumetricpixels.politics.group.Group;
import org.spout.api.event.Event;

/**
 * An event related to a group.
 */
public abstract class GroupEvent extends Event {
	/**
	 * The group.
	 */
	private final Group group;

	/**
	 * C'tor
	 *
	 * @param group
	 */
	protected GroupEvent(Group group) {
		this.group = group;
	}

	/**
	 * Gets the group in this event.
	 *
	 * @return
	 */
	public Group getGroup() {
		return group;
	}
}
