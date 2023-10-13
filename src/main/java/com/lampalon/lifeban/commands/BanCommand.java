package com.lampalon.lifeban.commands;

import com.lampalon.lifeban.Lifeban;
import com.lampalon.lifeban.utils.Profile;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class BanCommand extends Command {
    public BanCommand(String name) {
        super(name);
    }

    public void execute(final CommandSender sender, final String[] args) {
        if (sender.hasPermission("lifeban.ban")) {
            if (args.length >= 2) {
                String playerName = args[0];
                String timeAndReason = args[1];
                if (timeAndReason.matches("^(\\d+)([smhd])$")) {
                    String timeString = timeAndReason.replaceAll("[^\\d.]", "");
                    char timeUnit = timeAndReason.charAt(timeAndReason.length() - 1);

                    Lifeban.TimeUnit unit = Lifeban.TimeUnit.getByString(String.valueOf(timeUnit));
                    if (unit != null) {
                        try {
                            long timeValue = Long.parseLong(timeString);
                            long seconds = unit.getSeconds() * timeValue;

                            String reason = args.length > 2 ? String.join(" ", Arrays.copyOfRange(args, 2, args.length)) : Lifeban.getConfigManager().getString("messages.ban.none_reason");

                            Profile profile = new Profile(playerName);
                            if (profile != null) {
                                if (!profile.isBanned()) {
                                    profile.setBanned(reason, sender.getName(), seconds);
                                    sender.sendMessage(Lifeban.PREFIX + Lifeban.getConfigManager().getString("messages.ban.banned", new String[]{"{NAME}~" + playerName}));

                                    ScheduledTask unbanTask = Lifeban.getInstance().getProxy().getScheduler().schedule(Lifeban.getInstance(), () -> {
                                        profile.unban();
                                    }, seconds, TimeUnit.SECONDS);
                                } else {
                                    sender.sendMessage(Lifeban.PREFIX + Lifeban.getConfigManager().getString("messages.player_already_banned", new String[]{"{NAME}~" + playerName}));
                                }
                            } else {
                                sender.sendMessage(Lifeban.PREFIX + Lifeban.getConfigManager().getString("messages.player_not_found"));
                            }
                        } catch (NumberFormatException e) {
                            sender.sendMessage("Internal error occured");
                        }
                    } else {
                        sender.sendMessage(Lifeban.PREFIX + Lifeban.getConfigManager().getString("messages.unknown_timeunit"));
                    }
                } else {
                    sender.sendMessage(Lifeban.PREFIX + Lifeban.getConfigManager().getString("messages.unknown_format"));
                }
            } else {
                sender.sendMessage(Lifeban.PREFIX +  Lifeban.getConfigManager().getString("messages.ban.syntax"));
            }
        } else {
            sender.sendMessage(Lifeban.PREFIX + Lifeban.getConfigManager().getString("messages.no_permissions"));
        }
    }

}