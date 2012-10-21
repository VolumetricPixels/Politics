package com.volumetricpixels.politics.command.politics;

import com.volumetricpixels.politics.command.PCommand;

/**
 * Politics administration command.
 */
public abstract class PoliticsCommand extends PCommand {
    /**
     * C'tor
     *
     * @param primary
     */
    public PoliticsCommand(String primary) {
        super(primary.toLowerCase());
    }

    @Override
    protected String[] getPermissions() {
        return new String[]{"politics.politics." + primary};
    }
}
