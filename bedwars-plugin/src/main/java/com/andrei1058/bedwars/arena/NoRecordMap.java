package com.andrei1058.bedwars.arena;

import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.List;

public class NoRecordMap implements Listener {

    /**
     * 学习某SkyWars插件, 不过不知道有没有用?
     * 顾名思义 在管理进行某些操作后 不记录 这场游戏 在 管理某些操作之后的 战绩数据
     * 25/5/30
     */

    public static List<String> NoRecordMap = new ArrayList<>();

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        String cmd = e.getMessage();
        Player player = e.getPlayer();
        IArena a = Arena.getArenaByPlayer(player);
        if (a == null) return;
        if (cmd.startsWith("/tp ") || cmd.startsWith("/gamemode ") || cmd.startsWith("/give ")) {
            if (player.hasPermission("minecraft.command.teleport")) {
                if (a.getWorldName().contains(a.getWorldName())) return;
                if (a.getStatus() != GameState.playing) return;
                if (a.getPlayers().isEmpty()) {
                    return;
                }
                NoRecordMap.add(a.getWorldName());
                for (Player p : a.getPlayers()) {
                    p.sendMessage("§c§l本场游戏不记录战绩！");
                }
                for (Player p : a.getSpectators()) {
                    p.sendMessage("§c§l本场游戏不记录战绩！");
                }
            }
        }
    }
}
