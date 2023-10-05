package com.andrei1058.bedwars.listeners.bungee;

import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.tasks.RefreshAvailableArenaTask;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class BungeePingListener implements Listener {

    @EventHandler
    public void onPing(ServerListPingEvent e) {
        if (!Arena.getArenas().isEmpty()) {
            if (RefreshAvailableArenaTask.getAvailableArena() != -1) {
                IArena a = Arena.getArenas().get(RefreshAvailableArenaTask.getAvailableArena());
                if (a != null) {
                    e.setMotd(a.getDisplayStatus(Language.getDefaultLanguage()));
                }
            } else {
                e.setMotd(Language.getDefaultLanguage().m(Messages.MEANING_FULL));
            }
        }
    }
}