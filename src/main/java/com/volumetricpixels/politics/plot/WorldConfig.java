package com.volumetricpixels.politics.plot;

import org.spout.api.util.config.Configuration;

/**
 * Configuration of the world.
 */
public class WorldConfig {
	/**
	 * The name of the worldconfig.
	 */
	private final String name;

	/**
	 * X size in chunks of a plot.
	 */
	private int plotSizeX = 1;

	/**
	 * Y size in chunks of a plot.
	 */
	private int plotSizeY = 1;

	/**
	 * Z size in chunks of a plot.
	 */
	private int plotSizeZ = 1;

	/**
	 * C'tor
	 */
	public WorldConfig(String name) {
		this.name = name;
	}

	/**
	 * Gets the name of the WorldConfig.
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the plotSizeX
	 */
	public int getPlotSizeX() {
		return plotSizeX;
	}

	/**
	 * @param plotSizeX
	 *            the plotSizeX to set
	 */
	public void setPlotSizeX(int plotSizeX) {
		this.plotSizeX = plotSizeX;
	}

	/**
	 * @return the plotSizeY
	 */
	public int getPlotSizeY() {
		return plotSizeY;
	}

	/**
	 * @param plotSizeY
	 *            the plotSizeY to set
	 */
	public void setPlotSizeY(int plotSizeY) {
		this.plotSizeY = plotSizeY;
	}

	/**
	 * @return the plotSizeZ
	 */
	public int getPlotSizeZ() {
		return plotSizeZ;
	}

	/**
	 * @param plotSizeZ
	 *            the plotSizeZ to set
	 */
	public void setPlotSizeZ(int plotSizeZ) {
		this.plotSizeZ = plotSizeZ;
	}

	/**
	 * Saves a WorldConfig.
	 * 
	 * @param config
	 */
	public void save(Configuration config) {
		config.getNode("plotsize.x").setValue(plotSizeX);
		config.getNode("plotsize.y").setValue(plotSizeY);
		config.getNode("plotsize.z").setValue(plotSizeZ);
	}

	/**
	 * Loads a WorldConfig.
	 * 
	 * @param name
	 * @param config
	 * @return
	 */
	public static WorldConfig load(String name, Configuration config) {
		WorldConfig wc = new WorldConfig(name);
		{
			wc.plotSizeX = config.getNode("plotsize.x").getInt(1);
			wc.plotSizeY = config.getNode("plotsize.y").getInt(8);
			wc.plotSizeZ = config.getNode("plotsize.z").getInt(1);
		}
		return wc;
	}
}
