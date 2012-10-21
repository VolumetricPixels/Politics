package com.volumetricpixels.politics.command.politics;

import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.util.MsgStyle;
import org.spout.api.command.Command;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.exception.CommandException;
import org.spout.api.geo.discrete.Point;

/**
 * Politics About Command.
 */
public class PoliticsAboutCommand extends PoliticsCommand {
    /**
     * C'tor
     */
    public PoliticsAboutCommand() {
        super("about");
    }

    @Override
    public void processCommand(CommandSource source, Command command, CommandContext args) throws CommandException {
        source.sendMessage(MsgStyle.INFO, "Politics ", Politics.getPlugin().getVersion());
        source.sendMessage(MsgStyle.INFO, "(c) 2012 Volumetric Pixels."); // Todo make this cool
    }
}
