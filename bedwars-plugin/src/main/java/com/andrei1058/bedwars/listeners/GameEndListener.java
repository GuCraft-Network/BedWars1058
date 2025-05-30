package com.andrei1058.bedwars.listeners;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.events.gameplay.GameEndEvent;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.NoRecordMap;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GameEndListener implements Listener {

    @EventHandler
    public void cleanDroppedItemsAndRespawns(@NotNull GameEndEvent event) {
        if (event.getArena().getPlayers().isEmpty()) {
            return;
        }

        // clear dropped items
        World game = event.getArena().getWorld();
        for (Entity item : game.getEntities()) {
            if (item instanceof Item || item instanceof ItemStack) {
                item.remove();
            }
        }

        event.getArena().getRespawns().clear();
        NoRecordMap.NoRecordMap.remove(event.getArena().getWorldName());
    }

    /**
     * 顾名思义
     * 25/5/30
     */
    @EventHandler
    public void sendPlayerAgainMessage(@NotNull GameEndEvent event) {
        IArena a = event.getArena();
        if (a.getPlayers().isEmpty()) {
            return;
        }

        Bukkit.getScheduler().runTaskLaterAsynchronously(BedWars.plugin, () -> {
            TextComponent tc = new TextComponent("§b要再来一局吗？ " + "§6§l点击这里");
            tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/bw playagain"));
            for (Player p : a.getPlayers()) {
                p.spigot().sendMessage(tc);
            }
            for (Player p : a.getSpectators()) {
                p.spigot().sendMessage(tc);
            }
        }, 40L);
    }


    /**
     * 防止反作弊误判来着 不过建议还是设置allow-flight: true
     * 25/5/30
     */

//    @EventHandler
//    public void setAllPlayersAllowFlight(@NotNull GameEndEvent event) {
//        IArena a = event.getArena();
//        if (a.getPlayers().isEmpty()) {
//            return;
//        }
//
//        for (Player p : a.getPlayers()) {
//            p.setAllowFlight(true);
//        }
//    }
//
//    @EventHandler
//    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
//        Player player = event.getPlayer();
//
//        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;
//
//        if (Arena.getArenaByPlayer(player).getStatus() != GameState.restarting) return;
//
//        if (player.getAllowFlight()) {
//            event.setCancelled(true);
//        }
//    }
}
