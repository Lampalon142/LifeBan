package com.lampalon.lifeban.commands;

import com.lampalon.lifeban.Lifeban;
import com.lampalon.lifeban.utils.Profile;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class UnmuteCommand extends Command {
    public UnmuteCommand(String name)
    {
        super(name);
    }
    ;

    public void execute(final CommandSender sender, final String[] args) {
        if(sender.hasPermission("bungeeban.command.unmute")) {
            if(args.length == 1) {
                String playerName = args[0];
                Profile profile = new Profile(playerName);
                if(profile != null) {
                    if(profile.isMuted()) {
                        profile.unmute();
                        sender.sendMessage(Lifeban.PREFIX + Lifeban.getConfigManager().getString("messages.unmute.unmuted", new String[] { "{NAME}~" + playerName }));
                    }
                    else {
                        sender.sendMessage(Lifeban.PREFIX + Lifeban.getConfigManager().getString("messages.player_not_muted", new String[] { "{NAME}~" + playerName }));				  }
                }
                else {
                    sender.sendMessage(Lifeban.PREFIX + Lifeban.getConfigManager().getString("messages.player_not_found"));
                }
            }
            else {
                sender.sendMessage(Lifeban.PREFIX + Lifeban.getConfigManager().getString("messages.unmute.syntax"));
            }
        }
        else {
            sender.sendMessage(Lifeban.PREFIX + Lifeban.getConfigManager().getString("messages.no_permissions"));
        }
    }
}
