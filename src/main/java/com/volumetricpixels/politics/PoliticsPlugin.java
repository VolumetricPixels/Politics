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
package com.volumetricpixels.politics;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.spout.api.Engine;
import org.spout.api.plugin.Plugin;
import org.spout.api.scheduler.TaskPriority;

import com.volumetricpixels.politics.command.Commands;
import com.volumetricpixels.politics.data.PoliticsFileSystem;
import com.volumetricpixels.politics.data.SaveTask;
import com.volumetricpixels.politics.group.privilege.PrivilegeManager;
import com.volumetricpixels.politics.protection.PoliticsProtectionListener;
import com.volumetricpixels.politics.universe.UniverseManager;
import com.volumetricpixels.politics.world.PlotManager;

/**
 * The main class of the Politics Spout plugin
 */
public final class PoliticsPlugin extends Plugin {
    /**
     * Singleton instance of the plugin
     */
    private static PoliticsPlugin instance;
    /**
     * The file system for Politics
     */
    private PoliticsFileSystem fileSystem;
    /**
     * Politics' Plot Manager
     */
    private PlotManager plotManager;
    /**
     * Politics' Privilege Manager
     */
    private PrivilegeManager privilegeManager;
    /**
     * Politics' Universe Manager
     */
    private UniverseManager universeManager;

    @Override
    public void onEnable() {
        instance = this;

        // Initialise PoliticsFileSystem
        fileSystem = new PoliticsFileSystem();

        // Load privileges
        privilegeManager = new PrivilegeManager();

        // Load plots and worlds
        plotManager = new PlotManager();
        plotManager.loadWorlds();

        // Load universes and their rules
        universeManager = new UniverseManager();
        universeManager.loadRules();
        universeManager.loadUniverses();

        // Register all commands
        Commands.registerAll();

        // Register and schedule things with Spout
        final Engine e = getEngine();
        e.getEventManager().registerEvents(new PoliticsListener(), this);
        e.getEventManager().registerEvents(new PoliticsProtectionListener(), this);
        e.getScheduler().scheduleSyncRepeatingTask(this, new SaveTask(), 5 * 60 * 20, 5 * 60 * 20, TaskPriority.LOWEST);

        getLogger().log(Level.INFO, "Politics enabled!");
    }

    @Override
    public void onDisable() {
        instance = null;

        plotManager.saveWorlds();
        universeManager.saveUniverses();

        getLogger().log(Level.INFO, "Politics disabled!");
    }

    /**
     * Gets the version of Politics this is.
     * 
     * @return The version of Politics.
     */
    public String getVersion() {
        return getDescription().getVersion();
    }

    /**
     * Gets the file system of the plugin.
     * 
     * @return The PoliticsFileSystem Politics uses for File interaction
     */
    public PoliticsFileSystem getFileSystem() {
        return fileSystem;
    }

    /**
     * Gets the PlotManager of the plugin.
     * 
     * @return Politics' PlotManager
     */
    public PlotManager getPlotManager() {
        return plotManager;
    }

    /**
     * Gets the PrivilegeManager of the plugin.
     * 
     * @return
     */
    public PrivilegeManager getPrivilegeManager() {
        return privilegeManager;
    }

    /**
     * Gets the UniverseManager of the plugin.
     * 
     * @return Politics' PlotManager
     */
    public UniverseManager getUniverseManager() {
        return universeManager;
    }

    /**
     * Gets the instance of PoliticsPlugin.
     * 
     * @return The running PoliticsPlugin instance
     */
    public static PoliticsPlugin getInstance() {
        return instance;
    }

    /**
     * Gets the logger of the plugin.
     * 
     * @return Politics' Logger
     */
    public static Logger logger() {
        return instance.getLogger();
    }
}
