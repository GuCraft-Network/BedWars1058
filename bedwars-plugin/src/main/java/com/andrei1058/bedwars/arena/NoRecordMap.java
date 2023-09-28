package com.andrei1058.bedwars.arena;

import com.andrei1058.bedwars.api.arena.IArena;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.List;

public class NoRecordMap implements Listener {

    public static List<String> NoRecordMap = new ArrayList<>();

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        String cmd = e.getMessage();
        Player player = e.getPlayer();
        IArena a = Arena.getArenaByPlayer(player);
        if (a == null) return;
        if (cmd.startsWith("/tp ") || cmd.startsWith("/gamemode ") || cmd.startsWith("/give ")) {
            if (player.hasPermission("minecraft.teleport.command")) {
                if (a.getWorldName().contains(a.getWorldName())) return;
                if (a.getPlayers().isEmpty()) {
                    return;
                }
                NoRecordMap.add(a.getWorldName());
                for (Player p : a.getPlayers()) {
                    p.sendMessage("§c§l本场游戏不记录战绩！");
                }
            }
        }
    }
}
