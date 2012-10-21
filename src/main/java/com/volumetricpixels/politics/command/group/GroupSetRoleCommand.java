package com.volumetricpixels.politics.command.group;

import com.volumetricpixels.politics.group.Group;
import com.volumetricpixels.politics.group.level.GroupLevel;
import org.spout.api.command.Command;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.exception.CommandException;

/**
 * Group set role command
 */
public class GroupSetRoleCommand extends GroupCommand {
    /**
     * C'tor
     *
     * @param level
     */
    public GroupSetRoleCommand(GroupLevel level) {
        super(level, "setrole");
    }

    @Override
    public void processCommand(CommandSource source, Command cmd, CommandContext args) throws CommandException {
        Group group = findGroup(source, cmd, args);

        // TODO implement me
    }

    @Override
    public void setupCommand(Command cmd) {
        cmd.setArgBounds(2, -1);
        cmd.setHelp("Sets the role of a player in this " + level.getName() + ".");
        cmd.setUsage("<player> <role> [-g group] [-u universe]");
    }
}
