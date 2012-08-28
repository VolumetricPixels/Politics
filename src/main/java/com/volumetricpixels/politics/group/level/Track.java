/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.volumetricpixels.politics.group.level;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.spout.api.util.config.ConfigurationNode;

/**
 *
 * @author simplyianm
 */
public class Track implements Iterable<Role> {
	/**
	 * The id of the track.
	 */
	private final String id;

	/**
	 * The roles of the track.
	 */
	private final List<Role> roles;

	/**
	 * C'tor
	 *
	 * @param id
	 * @param roles
	 */
	Track(String id, List<Role> roles) {
		this.id = id;
		this.roles = roles;
	}

	/**
	 * Gets the id of the track.
	 *
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * Gets the roles of the track.
	 *
	 * @return
	 */
	public List<Role> getRoles() {
		return new LinkedList<Role>(roles);
	}

	/**
	 * Gets the role before the given role.
	 *
	 * @param role
	 * @return
	 */
	public Role getPreviousRole(Role role) {
		int index = roles.indexOf(role);
		if (index < 0 || index <= 0) {
			return null;
		}
		return roles.get(index - 1);
	}

	/**
	 * Gets the role after the given role.
	 *
	 * @param role
	 * @return
	 */
	public Role getNextRole(Role role) {
		int index = roles.indexOf(role);
		if (index < 0 || index + 2 > roles.size()) {
			return null;
		}
		return roles.get(index + 1);
	}

	@Override
	public Iterator<Role> iterator() {
		return roles.listIterator();
	}

	/**
	 * Loads a Track.
	 *
	 * @param id
	 * @param node
	 * @param roles
	 * @return
	 */
	public static Track load(String id, ConfigurationNode node, Map<String, Role> roles) {
		List<String> rolesNames = node.getStringList(new LinkedList<String>());
		List<Role> rolesList = new LinkedList<Role>();
		for (String roleName : rolesNames) {
			Role role = roles.get(roleName.toLowerCase());
			if (role == null) {
				throw new IllegalStateException("The role '" + roleName + "' does not exist.");
			}
			rolesList.add(role);
		}
		return new Track(id, rolesList);
	}
}
