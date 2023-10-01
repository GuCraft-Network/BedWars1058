package com.andrei1058.bedwars.listeners.bungee;

import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.tasks.RefreshAvailableArenaTask;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BungeePingListener extends PacketAdapter {

    public BungeePingListener(JavaPlugin plugin) {
        super(plugin, ListenerPriority.HIGH, Server.PING);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        if (event.getPacketType() == Server.PING) {
            WrappedServerPing ping = event.getPacket().getServerPings().read(0);
            IArena a = Arena.getArenas().get(RefreshAvailableArenaTask.getAvailableArena());
            boolean isWaitingOrStarting = a != null && (a.getStatus() == GameState.waiting || a.getStatus() == GameState.starting);

            ping.setPlayersOnline(isWaitingOrStarting ? a.getPlayers().size() : Bukkit.getOnlinePlayers().size());
            ping.setPlayersMaximum(isWaitingOrStarting ? a.getMaxPlayers() : Bukkit.getMaxPlayers());
            ping.setMotD(isWaitingOrStarting ? a.getDisplayStatus(Language.getDefaultLanguage()) : Language.getDefaultLanguage().m(Messages.MEANING_FULL));

            event.getPacket().getServerPings().write(0, ping);
        }
    }

}

