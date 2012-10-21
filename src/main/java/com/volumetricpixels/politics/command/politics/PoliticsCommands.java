package com.volumetricpixels.politics.command.politics;

import com.volumetricpixels.politics.command.Commands;
import org.spout.api.command.Command;

/**
 * Contains all Politics commands.
 */
public class PoliticsCommands extends Commands {
    /**
     * C'tor
     */
    public PoliticsCommands() {
        super("politics");
    }

    @Override
    public void setup(Command cmd) {
        (new PoliticsAboutCommand()).register(cmd);
    }
}
