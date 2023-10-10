package com.lampalon.lifeban.commands;

import com.lampalon.lifeban.Lifeban;
import com.lampalon.lifeban.utils.Profile;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class BanCommand extends Command {
    public BanCommand(String name)
    {
        super(name);
    }
    public void execute(final CommandSender sender, final String[] args) {
        if (sender.hasPermission("lifeban.ban")) {
            if (args.length >= 4) {
                String playerName = args[0];
                String reason = "";
                for (int i = 3; i <= args.length - 1; i++) {
                    reason = reason + args[i] + " ";
                    Lifeban.getConfigManager().save();
                    Profile profile = new Profile(playerName);
                    if (profile != null) {
                        if (!profile.isBanned()) {
                            try {
                                long seconds = Integer.parseInt(args[1]);
                                Lifeban.TimeUnit unit = Lifeban.TimeUnit.getByString(args[2]);
                                if (unit != null) {
                                    seconds *= unit.getSeconds();
                                    profile.setBanned(reason, sender.getName(), seconds);
                                    sender.sendMessage(Lifeban.PREFIX + Lifeban.getConfigManager().getString("messages.ban-banned", new String[]{"{NAME}~" + playerName}));
                                }
                            } catch (NumberFormatException e) {
                                sender.sendMessage("An interal error occured");
                            }
                        } else {
                            sender.sendMessage(Lifeban.PREFIX + Lifeban.getConfigManager().getString("messages.player_already_banned", new String[]{"{NAME}~" + playerName}));
                        }
                    } else {
                        sender.sendMessage(Lifeban.PREFIX + Lifeban.getConfigManager().getString("messages.player_not_found"));
                    }
                }
            } else {
                sender.sendMessage(Lifeban.PREFIX + Lifeban.getConfigManager().getString("messages.ban"));
            }
        } else {
            sender.sendMessage(Lifeban.PREFIX + Lifeban.getConfigManager().getString("messages.no_permissions"));
        }
        ;
    }
}
