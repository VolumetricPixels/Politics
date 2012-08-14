package com.simplyian.colonies;

import java.util.logging.Level;

import org.spout.api.plugin.CommonPlugin;

/**
 * Colonies plugin main class.
 */
public class ColoniesPlugin extends CommonPlugin {

	@Override
	public void onEnable() {
		getLogger().log(Level.INFO, "Colonies enabled!");
	}

	@Override
	public void onDisable() {
		getLogger().log(Level.INFO, "Colonies disabled!");
	}

}
