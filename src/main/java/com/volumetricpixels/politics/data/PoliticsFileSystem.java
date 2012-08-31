package com.volumetricpixels.politics.data;

import com.volumetricpixels.politics.Politics;
import java.io.File;

/**
 * Manages... data?
 */
public class PoliticsFileSystem {
	/**
	 * Directory containing rules.
	 */
	private final File rulesDir;

	/**
	 * C'tor
	 */
	public PoliticsFileSystem() {
		File base = Politics.getPlugin().getDataFolder();

		rulesDir = new File(base, "rules/");
	}

	/**
	 * Gets the rules directory.
	 *
	 * @return
	 */
	public File getRulesDir() {
		return rulesDir;
	}
}
