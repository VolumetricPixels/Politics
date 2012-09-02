package com.volumetricpixels.politics.command.group;

import com.volumetricpixels.politics.MsgStyle;
import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.event.PoliticsEventFactory;
import com.volumetricpixels.politics.group.Group;
import com.volumetricpixels.politics.group.GroupProperty;
import com.volumetricpixels.politics.group.level.GroupLevel;
import com.volumetricpixels.politics.group.level.Privilege;
import com.volumetricpixels.politics.universe.Universe;
import org.spout.api.command.Command;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;

/**
 * Claims the plot you are in.
 */
public class GroupClaimCommand extends GroupCommand {
    /**
     * C'tor
     *
     * @param level
     */
    public GroupClaimCommand(GroupLevel level) {
        super(level, "claim");
    }

    @Override
    public void processCommand(CommandSource source, Command cmd, CommandContext context) throws CommandException {
        Group group = findGroup(source, cmd, context);
        if (group == null) {
            return;
        }

        if (!group.getRole(source.getName()).hasPrivilege(Privilege.CLAIM) && !source.hasPermission("politics.admin.group." + level.getId() + ".claim")) {
            source.sendMessage(MsgStyle.ERROR, "You don't have permissions to claim land in this " + level.getName() + ".");
            return;
        }
    }

    @Override
    public void setupCommand(Command cmd) {
        cmd.setArgBounds(1, -1);
        cmd.setHelp("Claims land for your " + level.getName() + ".");
        cmd.setUsage("[-g " + level.getName() + "] [-u universe]");
        cmd.setPermissions(true, "politics.group." + level.getId() + ".claim");
    }
}
