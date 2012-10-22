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

public class PoliticsProtectionService extends ProtectionService {
    private static PoliticsProtectionService instance;

    private final List<Protection> protections = new ArrayList<Protection>();

    public PoliticsProtectionService() {
        instance = this;

        Spout.getEventManager().registerEvents(new PoliticsProtectionServiceListener(), Politics.getPlugin());
    }

    public boolean addProtection(Protection prot) {
        if (protections.contains(prot) == false) {
            if (prot != null) {
                return protections.add(prot);
            }
        }
        return false;
    }

    public boolean removeProtection(Protection prot) {
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
    public Collection<Protection> getAllProtections(World world) {
        Collection<Protection> result = new ArrayList<Protection>();
        for (Protection p : protections) {
            if (p.getWorld().equals(world)) {
                result.add(p);
            }
        }
        return result;
    }

    @Override
    public Collection<Protection> getAllProtections(Point location) {
        Collection<Protection> result = new ArrayList<Protection>();
        for (Protection p : protections) {
            if (p.contains(location)) {
                result.add(p);
            }
        }
        return result;
    }

    @Override
    public Protection getProtection(String name) {
        return null;
    }

    public static PoliticsProtectionService getInstance() {
        return instance;
    }
}
