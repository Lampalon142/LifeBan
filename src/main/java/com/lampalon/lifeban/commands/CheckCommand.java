package com.lampalon.lifeban.commands;

import com.lampalon.lifeban.Lifeban;
import com.lampalon.lifeban.utils.Profile;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import java.util.List;

public class CheckCommand extends Command {
    public CheckCommand(String name)
    {
        super(name);
    }

    public void execute(final CommandSender sender, final String[] args)
    {
        BungeeCord.getInstance().getScheduler().runAsync(Lifeban.getInstance(), new Runnable()
        {
            public void run()
            {
                if (sender.hasPermission("lifeban.check"))
                {
                    if (args.length == 1)
                    {
                        Profile profile = new Profile(args[0]);
                        if (profile != null)
                        {
                            String playername = args[0];
                            sender.sendMessage(Lifeban.PREFIX + Lifeban.getConfigManager().getString("messages.check", new String[] { "{PLAYERNAME}~" + playername }));
                            if (profile.isBanned())
                            {
                                List<String> msgs = Lifeban.getConfigManager().getStringList("messages.check.banned.positive", new String[] { "{NAME}~" + playername, "{REASON}~" +  profile.getBanReason(), "{BY}~" + profile.getBanBy(), "{REMAININGTIME}~" + profile.getRemainingbanTime() });
                                for (String msg : msgs) {
                                    sender.sendMessage(Lifeban.PREFIX + msg);
                                }
                            }
                            else
                            {
                                List<String> msgs = Lifeban.getConfigManager().getStringList("messages.check.banned.negative", new String[] { "{NAME}~" + playername });
                                for (String msg : msgs) {
                                    sender.sendMessage(Lifeban.PREFIX + msg);
                                }
                            }
                            if (profile.isMuted())
                            {
                                List<String> msgs = Lifeban.getConfigManager().getStringList("messages.check.muted.positive", new String[] { "{NAME}~" + playername, "{REASON}~" + profile.getMuteReason(), "{BY}~" + profile.getMutedBy(), "{REMAININGTIME}~" + profile.getRemainingmuteTime() });
                                for (String msg : msgs) {
                                    sender.sendMessage(Lifeban.PREFIX + msg);
                                }
                            }
                            else
                            {
                                List<String> msgs = Lifeban.getConfigManager().getStringList("messages.check.muted.negative", new String[] { "{NAME}~" + playername });
                                for (String msg : msgs) {
                                    sender.sendMessage(Lifeban.PREFIX + msg);
                                }
                            }
                        }
                        else
                        {
                            sender.sendMessage(Lifeban.PREFIX + Lifeban.getConfigManager().getString("messages.player_not_found"));
                        }
                    }
                    else
                    {
                        sender.sendMessage(Lifeban.PREFIX + Lifeban.getConfigManager().getString("messages.check.syntax"));
                    }
                }
                else {
                    sender.sendMessage(Lifeban.PREFIX + Lifeban.getConfigManager().getString("messages.no_permissions"));
                }
            }
        });
    }
}
