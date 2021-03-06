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
package com.volumetricpixels.politics.exception;

/**
 * Thrown when a property is not serialized successfully
 */
public class PropertySerializationException extends Exception {
    private static final long serialVersionUID = -3792841042998190396L;

    /**
     * C'tor
     * 
     * @param string
     */
    public PropertySerializationException(final String string) {
        super(string);
    }

    /**
     * C'tor
     * 
     * @param string
     * @param thrwbl
     */
    public PropertySerializationException(final String string, final Throwable thrwbl) {
        super(string, thrwbl);
    }
}
