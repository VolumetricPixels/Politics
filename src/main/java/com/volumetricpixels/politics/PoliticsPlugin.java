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
package com.volumetricpixels.politics;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.spout.api.plugin.CommonPlugin;
import org.spout.api.scheduler.TaskPriority;

import com.volumetricpixels.politics.command.Commands;
import com.volumetricpixels.politics.data.SaveTask;
import com.volumetricpixels.politics.plot.PlotManager;
import com.volumetricpixels.politics.universe.UniverseManager;

/**
 * Groups plugin main class.
 */
public class PoliticsPlugin extends CommonPlugin {
	/**
	 * Singleton.
	 */
	private static PoliticsPlugin instance;

	/**
	 * The plot manager.
	 */
	private PlotManager plotManager;

	/**
	 * The universe manager.
	 */
	private UniverseManager universeManager;

	@Override
	public void onEnable() {
		instance = this;

		plotManager = new PlotManager();
		plotManager.loadWorlds();

		universeManager = new UniverseManager();
		universeManager.loadRules();
		universeManager.loadUniverses();

		Commands.registerAll();

		// Save task
		getEngine().getScheduler().scheduleSyncRepeatingTask(this, new SaveTask(), 5 * 60 * 20, 5 * 60 * 20, TaskPriority.LOWEST);

		getLogger().log(Level.INFO, "Groups enabled!");
	}

	@Override
	public void onDisable() {
		instance = null;

		plotManager.saveWorlds();
		universeManager.saveUniverses();

		getLogger().log(Level.INFO, "Groups disabled!");
	}

	/**
	 * Gets the PlotManager of the plugin.
	 * 
	 * @return
	 */
	public PlotManager getPlotManager() {
		return plotManager;
	}

	/**
	 * Gets the UniverseManager of the plugin.
	 * 
	 * @return
	 */
	public UniverseManager getUniverseManager() {
		return universeManager;
	}

	/**
	 * Gets the instance of GroupsPlugin.
	 * 
	 * @return
	 */
	public static PoliticsPlugin getInstance() {
		return instance;
	}

	/**
	 * Gets the logger of the plugin.
	 * 
	 * @return
	 */
	public static Logger logger() {
		return instance.getLogger();
	}
}
