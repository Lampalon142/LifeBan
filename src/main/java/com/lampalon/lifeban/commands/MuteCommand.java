package com.lampalon.lifeban.commands;

import com.lampalon.lifeban.Lifeban;
import com.lampalon.lifeban.utils.Profile;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class MuteCommand extends Command {
    public MuteCommand(String name)
    {
        super(name);
    }
    ;

    public void execute(final CommandSender sender, final String[] args) {
        if(sender.hasPermission("lifeban.mute")) {
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
                            String reason = args.length > 2 ? String.join(" ", Arrays.copyOfRange(args, 2, args.length)) : Lifeban.getConfigManager().getString("messages.mute.none_reason");

                            Profile profile = new Profile(playerName);
                            if (profile != null) {
                                if (!profile.isMuted()) {
                                    profile.setMute(reason, sender.getName(), seconds);
                                    sender.sendMessage(Lifeban.PREFIX + Lifeban.getConfigManager().getString("messages.mute.muted", new String[]{"{NAME}~" + playerName}));

                                    ScheduledTask unmuteTask = Lifeban.getInstance().getProxy().getScheduler().schedule(Lifeban.getInstance(), () -> {
                                        profile.unmute();
                                    }, seconds, TimeUnit.SECONDS);
                                } else {
                                    sender.sendMessage(Lifeban.PREFIX + Lifeban.getConfigManager().getString("messages.player_already_muted", new String[]{"{NAME}~" + playerName}));
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
                sender.sendMessage(Lifeban.PREFIX +  Lifeban.getConfigManager().getString("messages.mute.syntax"));
            }
        } else {
            sender.sendMessage(Lifeban.PREFIX + Lifeban.getConfigManager().getString("messages.no_permissions"));
        };
    }
}
