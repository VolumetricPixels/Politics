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
package com.volumetricpixels.politics.util;

public final class Base64Coder {
    // The line separator string of the operating system.
    private static final String systemLineSeparator = System.getProperty("line.separator");
    // Mapping table from 6-bit nibbles to Base64 characters.
    private static final char[] map1 = new char[64];

    static {
        int i = 0;
        for (char c = 'A'; c <= 'Z'; c++) {
            map1[i++] = c;
        }
        for (char c = 'a'; c <= 'z'; c++) {
            map1[i++] = c;
        }
        for (char c = '0'; c <= '9'; c++) {
            map1[i++] = c;
        }
        map1[i++] = '+';
        map1[i++] = '/';
    }

    // Mapping table from Base64 characters to 6-bit nibbles.
    private static final byte[] map2 = new byte[128];

    static {
        for (int i = 0; i < map2.length; i++) {
            map2[i] = -1;
        }
        for (int i = 0; i < 64; i++) {
            map2[map1[i]] = (byte) i;
        }
    }

    public static char[] encode(final byte[] in, final int iOff, final int iLen) {
        final int oDataLen = (iLen * 4 + 2) / 3; // output length without
        // padding
        final int oLen = (iLen + 2) / 3 * 4; // output length including padding
        final char[] out = new char[oLen];
        int ip = iOff;
        final int iEnd = iOff + iLen;
        int op = 0;

        while (ip < iEnd) {
            final int i0 = in[ip++] & 0xff;
            final int i1 = ip < iEnd ? in[ip++] & 0xff : 0;
            final int i2 = ip < iEnd ? in[ip++] & 0xff : 0;
            final int o0 = i0 >>> 2;
            final int o1 = (i0 & 3) << 4 | i1 >>> 4;
            final int o2 = (i1 & 0xf) << 2 | i2 >>> 6;
            final int o3 = i2 & 0x3F;
            out[op++] = map1[o0];
            out[op++] = map1[o1];
            out[op] = op < oDataLen ? map1[o2] : '=';
            op++;
            out[op] = op < oDataLen ? map1[o3] : '=';
            op++;
        }

        return out;
    }

    public static byte[] decode(final char[] in, final int iOff, int iLen) {
        if (iLen % 4 != 0) {
            throw new IllegalArgumentException("Length of Base64 encoded input string is not a multiple of 4.");
        }

        while (iLen > 0 && in[iOff + iLen - 1] == '=') {
            iLen--;
        }

        final int oLen = iLen * 3 / 4;
        final byte[] out = new byte[oLen];
        int ip = iOff;
        final int iEnd = iOff + iLen;
        int op = 0;

        while (ip < iEnd) {
            final int i0 = in[ip++];
            final int i1 = in[ip++];
            final int i2 = ip < iEnd ? in[ip++] : 'A';
            final int i3 = ip < iEnd ? in[ip++] : 'A';

            if (i0 > 127 || i1 > 127 || i2 > 127 || i3 > 127) {
                throw new IllegalArgumentException("Illegal character in Base64 encoded data.");
            }

            final int b0 = map2[i0];
            final int b1 = map2[i1];
            final int b2 = map2[i2];
            final int b3 = map2[i3];

            if (b0 < 0 || b1 < 0 || b2 < 0 || b3 < 0) {
                throw new IllegalArgumentException("Illegal character in Base64 encoded data.");
            }

            final int o0 = b0 << 2 | b1 >>> 4;
            final int o1 = (b1 & 0xf) << 4 | b2 >>> 2;
            final int o2 = (b2 & 3) << 6 | b3;
            out[op++] = (byte) o0;

            if (op < oLen) {
                out[op++] = (byte) o1;
            }

            if (op < oLen) {
                out[op++] = (byte) o2;
            }
        }

        return out;
    }

    private Base64Coder() {
    }
}
