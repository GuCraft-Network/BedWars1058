package com.andrei1058.bedwars.sidebar;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public class GetCurServerName implements Listener, PluginMessageListener {

    private static String gamename = "Null";
    private String servername = "Null";

    public static String getGameName(IArena a) {
        if (gamename.equals("Null")) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(BedWars.plugin, () -> {
                SidebarService.getInstance().refreshPlaceholders(a);
            }, 20L);
        }
        return gamename;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (!("Null".equals(servername) && "Null".equals(gamename))) {
            return;
        }
        Bukkit.getScheduler().runTaskLaterAsynchronously(BedWars.plugin, () -> {
            try {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("GetServer");
                e.getPlayer().sendPluginMessage(BedWars.plugin, "BungeeCord", out.toByteArray());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }, 2L);
    }

    private void GetGameName() {
        if (servername.startsWith("bwhyp2")) {
            gamename = servername.replace("bwhyp2v", "N");
        } else if (servername.startsWith("bwhyp4")) {
            gamename = servername.replace("bwhyp4v", "N");
        } else if (servername.startsWith("bwrhyp4")) {
            gamename = servername.replace("bwrhyp4v", "R");
        } else if (servername.startsWith("bwuhyp4")) {
            gamename = servername.replace("bwuhyp4v", "U");
        } else if (servername.startsWith("bwshyp4")) {
            gamename = servername.replace("bwshyp4v", "S");
        } else {
            gamename = "Null";
        }
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if ("BungeeCord".equals(channel)) {
            try {
                DataInputStream input = new DataInputStream(new ByteArrayInputStream(message));
                if ("GetServer".equals(input.readUTF())) {
                    servername = input.readUTF();
                    GetGameName();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
