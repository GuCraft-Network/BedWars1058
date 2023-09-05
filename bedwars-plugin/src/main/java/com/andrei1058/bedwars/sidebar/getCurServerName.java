package com.andrei1058.bedwars.sidebar;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.events.player.PlayerJoinArenaEvent;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public class GetCurServerName implements PluginMessageListener, Listener {
    private static String serverName = null;

    public static String getGameName() {
        String gameName = null;
        if (serverName != null) {
            // 使用正则表达式进行替换操作
            if (serverName.contains("2v2")) {
                gameName = serverName.replaceAll("bw([shru])hyp2", "$1".toUpperCase());
            } else {
                gameName = serverName.replaceAll("bw([shru])hyp4", "$1".toUpperCase());
            }
        }
        return gameName;
    }

    @EventHandler
    public void onJoin(PlayerJoinArenaEvent e) {
        if (serverName != null) return;
        try {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("GetServer");
            e.getPlayer().sendPluginMessage(BedWars.plugin, "BungeeCord", out.toByteArray());
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }
        try {
            DataInputStream input = new DataInputStream(new ByteArrayInputStream(message));
            if (input.readUTF().equals("GetServer")) {
                serverName = input.readUTF();
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }
    }
}
