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
package com.volumetricpixels.politics.data;

import org.bson.BSONObject;

/**
 * Represents something that can be stored.
 * 
 * As well as implementing toBSONObject and canStore, classes that implement
 * Storable should have a static <code>fromBSONObject(BSONObject)</code> method
 */
public interface Storable {
    /**
     * Converts this object into a {@link BSONObject}
     * 
     * @return A BSONObject that represents this storable object
     */
    BSONObject toBSONObject();

    /**
     * Returns whether this Storable object can currently be stored
     * 
     * @return Whether this Storable object can currently be stored
     */
    boolean canStore();
}
