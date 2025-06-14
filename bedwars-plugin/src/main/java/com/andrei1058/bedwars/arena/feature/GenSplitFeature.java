package com.andrei1058.bedwars.arena.feature;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.configuration.ConfigPath;
import com.andrei1058.bedwars.api.events.player.PlayerGeneratorCollectEvent;
import com.andrei1058.bedwars.arena.Arena;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GenSplitFeature implements Listener {

    /**
     * https://github.com/tomkeuper/BedWars2023/blob/development/bedwars-plugin/src/main/java/com/tomkeuper/bedwars/arena/feature/GenSplitFeature.java
     * 25/5/30
     */
    private static GenSplitFeature instance;

    public GenSplitFeature() {
        Bukkit.getPluginManager().registerEvents(this, BedWars.plugin);
    }

    public static void init() {
        if (BedWars.config.getBoolean(ConfigPath.GENERAL_CONFIGURATION_PERFORMANCE_GENERATOR_SPLIT)) {
            if (instance == null) {
                instance = new GenSplitFeature();
            }
        }
    }


    @EventHandler
    public void onIslandGenPickup(PlayerGeneratorCollectEvent e) {
        if (!e.isCancelled() && (e.getItemStack().getType() == Material.IRON_INGOT || e.getItemStack().getType() == Material.GOLD_INGOT)) {
            Location pl = e.getPlayer().getLocation();
            Player p = e.getPlayer();
            int splitRange = Arena.getArenaByPlayer(p).getConfig().getInt(ConfigPath.ARENA_GENERATOR_SPLIT_RANGE);
            List<Entity> nearbyEntities = (List) pl.getWorld().getNearbyEntities(pl, splitRange, splitRange, 2.0);
            for (Entity entity : pl.getWorld().getEntities()) {
                if (nearbyEntities.contains(entity) && entity instanceof Player) {
                    Player pickupPlayer = (Player) entity;
                    if (pickupPlayer.getUniqueId() != p.getUniqueId()) {
                        ITeam team = Arena.getArenaByPlayer(pickupPlayer).getTeam(p);
                        ITeam rt = Arena.getArenaByPlayer(pickupPlayer).getTeam(pickupPlayer);
                        if (team == rt) {
                            ItemStack item = new ItemStack(e.getItemStack().getType(), e.getItemStack().getAmount());
                            if (!BedWars.getAPI().getAFKUtil().isPlayerAFK(pickupPlayer))
                                pickupPlayer.getInventory().addItem(item);
                        }
                    }
                }
            }
        }
    }
}
