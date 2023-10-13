package com.lampalon.lifeban.commands;

import com.lampalon.lifeban.Lifeban;
import com.lampalon.lifeban.utils.Profile;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class UnbanCommand extends Command {
    public UnbanCommand(String name)
    {
        super(name);
    }
    ;

    public void execute(final CommandSender sender, final String[] args) {
        if(sender.hasPermission("bungeeban.command.unban")) {
            if(args.length == 1) {
                String playerName = args[0];
                Profile profile = new Profile(playerName);
                if(profile != null) {
                    if(profile.isBanned()) {
                        profile.unban();
                        sender.sendMessage(Lifeban.PREFIX + Lifeban.getConfigManager().getString("messages.unban.unbanned", new String[] { "{NAME}~" + playerName }));
                    }
                    else {
                        sender.sendMessage(Lifeban.PREFIX + Lifeban.getConfigManager().getString("messages.player_not_banned", new String[] { "{NAME}~" + playerName }));				  }
                }
                else {
                    sender.sendMessage(Lifeban.PREFIX + Lifeban.getConfigManager().getString("messages.player_not_found"));			  }
            }
            else {
                sender.sendMessage(Lifeban.PREFIX + Lifeban.getConfigManager().getString("messages.unban.syntax"));
            }
        }
        else {
            sender.sendMessage(Lifeban.PREFIX + Lifeban.getConfigManager().getString("messages.no_permissions"));	  }
    }
}
