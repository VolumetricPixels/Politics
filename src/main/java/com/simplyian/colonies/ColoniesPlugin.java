/*
 * This file is part of Colonies.
 *
 * Colonies
 * Copyright (c) 2012-2012, THEDevTeam <http://thedevteam.org/>
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

import java.util.logging.Level;
import java.util.logging.Logger;

import org.spout.api.plugin.CommonPlugin;

import com.simplyian.colonies.plot.PlotManager;
import com.simplyian.colonies.universe.UniverseManager;

/**
 * Colonies plugin main class.
 */
public class ColoniesPlugin extends CommonPlugin {
	/**
	 * Singleton.
	 */
	private static ColoniesPlugin instance;

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
		plotManager.loadAll();

		universeManager = new UniverseManager(this);
		universeManager.loadRules();
		universeManager.loadUniverses();

		getLogger().log(Level.INFO, "Colonies enabled!");
	}

	@Override
	public void onDisable() {
		universeManager.saveUniverses();

		getLogger().log(Level.INFO, "Colonies disabled!");
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
	 * Gets the instance of ColoniesPlugin.
	 * 
	 * @return
	 */
	public static ColoniesPlugin getInstance() {
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
