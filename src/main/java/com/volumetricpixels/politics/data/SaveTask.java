package com.volumetricpixels.politics.data;

import com.volumetricpixels.politics.Politics;

/**
 * Saves everything in the plugin worth saving.
 */
public class SaveTask implements Runnable {

	@Override
	public void run() {
		Politics.getPlotManager().saveWorlds();
		Politics.getUniverseManager().saveUniverses();
	}

}
