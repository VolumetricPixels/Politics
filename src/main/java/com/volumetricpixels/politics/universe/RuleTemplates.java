package com.volumetricpixels.politics.universe;

import com.volumetricpixels.politics.PoliticsPlugin;
import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Rule template helper class.
 */
public final class RuleTemplates {
	/**
	 * C'tor
	 */
	private RuleTemplates() {
	}

	/**
	 * Lists all available template names.
	 *
	 * @return
	 */
	public static Set<String> listTemplateNames() {
		CodeSource src = RuleTemplates.class.getProtectionDomain().getCodeSource();
		Set<String> set = new HashSet<String>();

		try {
			if (src != null) {
				URL jar = src.getLocation();
				ZipInputStream zip;
				zip = new ZipInputStream(jar.openStream());
				ZipEntry ze = null;

				while ((ze = zip.getNextEntry()) != null) {
					String entryName = ze.getName();
					if (entryName.startsWith("templates")) {
						set.add(entryName);
					}
				}

			}
		} catch (IOException ex) {
			PoliticsPlugin.logger().log(Level.SEVERE, "Could not list template names!", ex);
		}

		return set;
	}
}
