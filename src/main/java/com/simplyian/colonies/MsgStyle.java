/*
 * This file is part of Colonies.
 *
 * Copyright (c) 2012-2012, THEDevTeam <http://thedevteam.org/>
 * Colonies is licensed under the Apache License Version 2.
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
package com.simplyian.colonies;

import org.spout.api.chat.style.ChatStyle;

/**
 * Holds styles of messages.
 * 
 * TODO make these configurable
 */
public class MsgStyle {
	public static ChatStyle error() {
		return ChatStyle.RED;
	}

	public static ChatStyle info() {
		return ChatStyle.GOLD;
	}
}
