package com.volumetricpixels.politics.protection;

import java.util.Collection;

import org.spout.api.Spout;
import org.spout.api.geo.Protection;
import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;
import org.spout.api.plugin.services.ProtectionService;

import com.volumetricpixels.politics.Politics;

public class PoliticsProtectionService extends ProtectionService {
    private static PoliticsProtectionService instance;

    public PoliticsProtectionService() {
        instance = this;

        Spout.getEventManager().registerEvents(new PoliticsProtectionServiceListener(), Politics.getPlugin());
    }

    @Override
    public Collection<Protection> getAllProtections() {
        return null;
    }

    @Override
    public Collection<Protection> getAllProtections(World world) {
        return null;
    }

    @Override
    public Collection<Protection> getAllProtections(Point location) {
        return null;
    }

    @Override
    public Protection getProtection(String name) {
        return null;
    }

    public static PoliticsProtectionService getInstance() {
        return instance;
    }
}
