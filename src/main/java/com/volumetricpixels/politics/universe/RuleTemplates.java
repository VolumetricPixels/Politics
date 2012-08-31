package com.volumetricpixels.politics.universe;

import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.PoliticsPlugin;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.CodeSource;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

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
	 * Copies a template with the given name to a new ruleset with the given
	 * name.
	 *
	 * @param name The name of the template.
	 * @param as The new name of the copied template.
	 */
	public static void copyTemplate(String name, String as) {
		InputStream templateStream = RuleTemplates.class.getResourceAsStream("templates/" + name.toLowerCase() + ".yml");
		if (templateStream == null) {
			throw new IllegalArgumentException("Template does not exist!");
		}
		StringWriter writer = new StringWriter();
		try {
			IOUtils.copy(templateStream, writer, Charset.defaultCharset());
		} catch (IOException ex) {
			PoliticsPlugin.logger().log(Level.SEVERE, "Could not read template " + name + "!", ex);
		}

		File dest = new File(Politics.getFileSystem().getRulesDir(), as.toLowerCase() + ".yml");
		try {
			FileUtils.writeStringToFile(dest, writer.toString());
		} catch (IOException ex) {
			PoliticsPlugin.logger().log(Level.SEVERE, "Could not write template as " + dest.getPath() + "!", ex);
		}
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
					if (entryName.startsWith("templates") && entryName.endsWith(".yml")) {
						set.add(entryName.substring("templates".length(), entryName.length() - 4));
					}
				}

			}
		} catch (IOException ex) {
			PoliticsPlugin.logger().log(Level.SEVERE, "Could not list template names!", ex);
		}

		return set;
	}
}
