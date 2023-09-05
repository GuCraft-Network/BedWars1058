package com.andrei1058.bedwars.sidebar;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.events.player.PlayerJoinArenaEvent;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public class getCurServerName implements PluginMessageListener {
    private static String servername = null;
    private static String gamename = null;

    public static String getName() {
        return gamename;
    }

    public static String getGamename() {
        if (servername.startsWith("bwhyp2")) {
            gamename = servername.replace("bwshyp2", "N");
        }
        if (servername.startsWith("bwhyp4")) {
            gamename = servername.replace("bwhyp4", "N");
        }
        if (servername.startsWith("bwrhyp4")) {
            gamename = servername.replace("bwrhyp4", "R");
        }
        if (servername.startsWith("bwuhyp4")) {
            gamename = servername.replace("bwuhyp4", "U");
        }
        if (servername.startsWith("bwshyp4")) {
            gamename = servername.replace("bwshyp4", "S");
        }
        return servername;
    }

    @EventHandler
    public void onJoin(PlayerJoinArenaEvent e) {
        if (servername != null || gamename != null) return;
        try {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("GetServer");
            e.getPlayer().sendPluginMessage(BedWars.plugin, "BungeeCord", out.toByteArray());
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }
        try {
            DataInputStream input = new DataInputStream(new ByteArrayInputStream(message));
            if (input.readUTF().equals("GetServer")) {
                servername = input.readUTF();
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }
    }
}
