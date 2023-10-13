package com.lampalon.lifeban.events;

import com.lampalon.lifeban.Lifeban;
import com.lampalon.lifeban.utils.Profile;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ChatEvent implements Listener {

    @EventHandler
    public void onChat(net.md_5.bungee.api.event.ChatEvent e){
        ProxiedPlayer p = (ProxiedPlayer) e.getSender();
        Profile profile = new Profile(p.getName());
        if(profile.isMuted()){
            if(e.getMessage().startsWith("/")) {
                return;
            }
            long end = profile.getMuteEnd();
            long current = System.currentTimeMillis();
            if(end == -1L) {
                e.setCancelled(true);
                e.setMessage("");
                p.sendMessage(Lifeban.PREFIX + profile.getMuteMessage());
                return;
            }
            if(end > current) {
                e.setCancelled(true);
                e.setMessage("");
                p.sendMessage(Lifeban.PREFIX + profile.getMuteMessage());
                return;
            }
            else{
                e.setCancelled(false);
                profile.unmute();
            }
        }
    }
}
