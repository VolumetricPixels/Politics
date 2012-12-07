/*
 * This file is part of Politics.
 *
 * Copyright (c) 2012-2012, VolumetricPixels <http://volumetricpixels.com/>
 * Politics is licensed under the Affero General Public License Version 3.
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
package com.volumetricpixels.politics.protection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.spout.api.Spout;
import org.spout.api.geo.Protection;
import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;
import org.spout.api.plugin.services.ProtectionService;

import com.volumetricpixels.politics.Politics;

import static java.lang.Integer.parseInt;

/**
 * The ProtectionService of Politics
 */
public class PoliticsProtectionService extends ProtectionService {
    private static PoliticsProtectionService instance;

    private final List<Protection> protections = new ArrayList<Protection>();

    public PoliticsProtectionService() {
        instance = this;

        Spout.getEventManager().registerEvents(new PoliticsProtectionServiceListener(), Politics.getPlugin());
    }

    public boolean addProtection(final Protection prot) {
        if (prot != null && protections.contains(prot) == false) {
            return protections.add(prot);
        }
        return false;
    }

    public boolean removeProtection(final Protection prot) {
        if (prot != null) {
            if (protections.contains(prot)) {
                return protections.add(prot);
            }
        }
        return false;
    }

    @Override
    public Collection<Protection> getAllProtections() {
        return protections;
    }

    @Override
    public Collection<Protection> getAllProtections(final World world) {
        final Collection<Protection> result = new ArrayList<Protection>();
        for (final Protection p : protections) {
            if (p.getWorld().equals(world)) {
                result.add(p);
            }
        }
        return result;
    }

    @Override
    public Collection<Protection> getAllProtections(final Point location) {
        final Collection<Protection> result = new ArrayList<Protection>();
        for (final Protection p : protections) {
            if (p.contains(location)) {
                result.add(p);
            }
        }
        return result;
    }

    @Override
    public Protection getProtection(final String name) {
        String[] split = name.split(",");
        Point point = new Point(Spout.getEngine().getWorld(split[0]), parseInt(split[1]), parseInt(split[2]), parseInt(split[3]));
        return Politics.getWorld(point.getWorld()).getPlotAt(point.getBlockX(), point.getBlockY(), point.getBlockZ());
    }

    public static PoliticsProtectionService getInstance() {
        return instance;
    }
}
