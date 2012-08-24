package com.volumetricpixels.politics.command.group;

import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.Command;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;

import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.group.Group;
import com.volumetricpixels.politics.group.GroupLevel;

public class GroupInfoCommand extends GroupCommand {

	public GroupInfoCommand(GroupLevel level) {
		super(level, "info");
	}

	@Override
	public void processCommand(CommandSource source, Command cmd, CommandContext context) throws CommandException {
		if (!(source instanceof Player)) {
			source.sendMessage("Consoles are part of a group");
			return;
		}
		Player p = (Player)source;
		
		StringBuilder sb = new StringBuilder();
		for(Group group : Politics.getUniverse(p.getWorld().getName()).getGroups()) {
			if (group.isImmediateMember(p.getName())) {
				sb.append(group.getProperty(0x1)).append(", ");
			}
		}
		p.sendMessage(ChatStyle.BLUE + "============= INFO =============");
		p.sendMessage(ChatStyle.DARK_GREEN + "Current Group: " + Politics.getPlotAt(p.getPosition()));
		p.sendMessage(ChatStyle.DARK_GREEN + "Immediate Groups: " + sb.toString().substring(0, sb.length()-2));
		//p.sendMessage(ChatStyle.DARK_GREEN + "Current Universe: " + Politics.getUniverseManager().get);
		p.sendMessage(ChatStyle.BLUE + "================================");
	}
}
