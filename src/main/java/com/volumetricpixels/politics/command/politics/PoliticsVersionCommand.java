package com.volumetricpixels.politics.command.politics;

import org.spout.api.command.Command;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.exception.CommandException;

import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.util.MessageStyle;

public class PoliticsVersionCommand extends PoliticsCommand {
    public PoliticsVersionCommand() {
        super("version");
    }

    @Override
    public void processCommand(final CommandSource source, final Command command, final CommandContext args) throws CommandException {
        source.sendMessage(MessageStyle.INFO, "Politics Version: " + Politics.getVersion());
        source.sendMessage(MessageStyle.INFO, "New versions of Politics can be found at " + Politics.JENKINS_URL);
    }
}
