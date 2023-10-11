package com.lampalon.lifeban.commands;

import com.lampalon.lifeban.Lifeban;
import com.lampalon.lifeban.utils.Profile;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class BanCommand extends Command {
    public BanCommand(String name)
    {
        super(name);
    }
    private long parseTime(String timeString) {
        if (timeString.matches("^\\d+m$")) {
            long minutes = Long.parseLong(timeString.replaceAll("[^\\d.]", ""));
            return minutes * 60 * 1000;
        } else {
            return 0;
        }
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

                            String reason = args.length > 2 ? String.join(" ", Arrays.copyOfRange(args, 2, args.length)) : "No reason specified";

                            Profile profile = new Profile(playerName);
                            if (profile != null) {
                                if (!profile.isBanned()) {
                                    profile.setBanned(reason, sender.getName(), seconds);
                                    sender.sendMessage(Lifeban.PREFIX + Lifeban.getConfigManager().getString("messages.ban.banned", new String[]{"{NAME}~" + playerName}));

                                    long timeInMillis = parseTime(timeString);

                                    if (timeInMillis > 0) {
                                        profile.setBanned(reason, sender.getName(), timeInMillis);
                                        sender.sendMessage(Lifeban.PREFIX + Lifeban.getConfigManager().getString("messages.ban.banned", new String[]{"{NAME}~" + playerName}));

                                        ScheduledTask unbanTask = Lifeban.getInstance().getProxy().getScheduler().schedule(Lifeban.getInstance(), () -> {
                                            profile.unban();
                                        }, timeInMillis, TimeUnit.MILLISECONDS);
                                    }
                                } else {
                                    sender.sendMessage(Lifeban.PREFIX + Lifeban.getConfigManager().getString("messages.player_already_banned", new String[]{"{NAME}~" + playerName}));
                                }
                            } else {
                                sender.sendMessage(Lifeban.PREFIX + Lifeban.getConfigManager().getString("messages.player_not_found"));
                            }
                        } catch (NumberFormatException e) {
                            sender.sendMessage("An internal error occurred");
                        }
                    } else {
                        sender.sendMessage(Lifeban.PREFIX + "Invalid time unit. Please use 's' for seconds, 'm' for minutes, 'h' for hours, or 'd' for days.");
                    }
                } else {
                    sender.sendMessage(Lifeban.PREFIX + "Invalid time format. Please use something like '30m' for 30 minutes.");
                }
            } else {
                sender.sendMessage(Lifeban.PREFIX + "Usage: /ban <user> <time> [reason]");
            }
        } else {
            sender.sendMessage(Lifeban.PREFIX + Lifeban.getConfigManager().getString("messages.no_permissions"));
        }
    }
}
