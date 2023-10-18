package com.andrei1058.bedwars.listeners.bungee;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.configuration.Permissions;
import com.andrei1058.bedwars.support.paper.TeleportManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class StaffListener implements Listener {

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        if (e.isCancelled()) return;
        String cmd = e.getMessage();
        if (!cmd.startsWith("/tp ") && !cmd.startsWith("/teleport ")) return;

        Player player = e.getPlayer();
        if (!player.hasPermission(Permissions.PERMISSION_SPECCHAT)) return;

        String[] args = cmd.split(" ");
        if (args.length >= 3) return;
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
        if (targetArena == null) {
            player.sendMessage("§c该玩家还没进入游戏！");
            return;
        }
        if (targetArena.getStatus() == GameState.waiting || targetArena.getStatus() == GameState.starting && !(targetArena.getStartingTask().getCountdown() < 1)) {
            if (arena != null) {
                if (arena.isPlayer(player)) {
                    arena.removePlayer(player, false);
                } else {
                    arena.removeSpectator(player, false);
                }
            }
            Bukkit.getScheduler().runTaskLater(BedWars.plugin, () -> targetArena.addPlayer(player, false), 10L);
            return;
        }

        if (arena != null) {
            if (arena.isPlayer(player)) {
                arena.removePlayer(player, false);
            } else {
                arena.removeSpectator(player, false);
            }
            Bukkit.getScheduler().runTaskLater(BedWars.plugin, () -> targetArena.addSpectator(player, false, null), 10L);
            Bukkit.getScheduler().runTaskLater(BedWars.plugin, () -> TeleportManager.teleportC(player, targetPlayer.getLocation(), PlayerTeleportEvent.TeleportCause.COMMAND), 15L);
            return;
        }
        targetArena.addSpectator(player, false, null);
        Bukkit.getScheduler().runTaskLater(BedWars.plugin, () -> TeleportManager.teleportC(player, targetPlayer.getLocation(), PlayerTeleportEvent.TeleportCause.COMMAND), 15L);
    }

}