package com.volumetricpixels.politics.command.group;

import com.volumetricpixels.politics.group.Group;
import com.volumetricpixels.politics.group.level.GroupLevel;
import com.volumetricpixels.politics.group.level.Role;
import com.volumetricpixels.politics.util.MsgStyle;
import org.spout.api.Spout;
import org.spout.api.command.Command;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.entity.Player;
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

        Player player = Spout.getEngine().getPlayer(args.getString(0), false);
        if (player == null) {
            throw new CommandException("That player is not online.");
        }
        if (!group.isImmediateMember(player.getName())) {
            throw new CommandException("That player is not a member of the group.");
        }

        String rn = args.getString(1);
        Role role = group.getLevel().getRole(rn);
        if (role == null) {
            throw new CommandException("There isn't a role named `" + rn + "'.");
        }
        
        if (!hasAdmin(source)) {
            Role myRole = group.getRole(source.getName());
            if (myRole.getRank() - role.getRank() <= 1) {
                throw new CommandException("You can't set someone to a role equal to or higher than your own.");
            }
        }

        group.setRole(player.getName(), role);
        source.sendMessage(MsgStyle.SUCCESS, player.getName() + " is now part of the " + level.getName() + ".");
    }

    @Override
    public void setupCommand(Command cmd) {
        cmd.setArgBounds(2, -1);
        cmd.setHelp("Sets the role of a player in this " + level.getName() + ".");
        cmd.setUsage("<player> <role> [-g group] [-u universe]");
    }
}
