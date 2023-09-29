package com.andrei1058.bedwars.listeners.bungee;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.arena.Arena;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class StaffListener implements Listener {

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        String cmd = e.getMessage();
        if (!cmd.startsWith("/tp ") || !cmd.startsWith("/teleport ")) return;

        Player player = e.getPlayer();
        if (!player.hasPermission("minecraft.teleport.command")) return;

        String[] args = cmd.split(" ", 3);
        if (args.length < 2) return;
        e.setCancelled(true);
        IArena arena = Arena.getArenaByPlayer(player);
        if (arena != null && arena.getStatus() == GameState.playing && !arena.isSpectator(player)) {
            player.sendMessage("§c你正在一场游戏中！");
            return;
        }

        Player targetPlayer = Bukkit.getPlayerExact(args[1]);
        if (targetPlayer == null) {
            player.sendMessage("§c该玩家不存在！");
            return;
        }

        IArena targetArena = Arena.getArenaByPlayer(targetPlayer);
        if (targetArena == null || targetArena.getStatus() != GameState.playing) {
            player.sendMessage("§c该玩家还没开始游戏！");
            return;
        }

        if (arena != null) {
            arena.removeSpectator(player, false);
            Bukkit.getScheduler().runTaskLater(BedWars.plugin, () -> targetArena.addSpectator(player, false, null), 10L);
        }
        targetArena.addSpectator(player, false, null);
    }

}