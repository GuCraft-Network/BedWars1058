package com.andrei1058.bedwars.listeners;

import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.configuration.Sounds;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class EnderPearlLanded implements Listener {

    @EventHandler
    public void onPearlHit(ProjectileHitEvent e) {

        if (!(e.getEntity() instanceof EnderPearl)) return;
        if (!(e.getEntity().getShooter() instanceof Player)) return;

        Player player = (Player) e.getEntity().getShooter();
        IArena iArena = Arena.getArenaByPlayer(player);

        if (!Arena.isInArena(player) || iArena.isSpectator(player) || iArena.isReSpawning(player))
            return;//isRespawning 旁观者或重生时不播放珍珠音效

        Sounds.playSound("ender-pearl-landed", iArena.getPlayers());
    }

    //旁观者或重生时不传送
    @EventHandler
    public void onPearlTeleport(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        if (p == null) return;
        if (e.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL) return;
        IArena a = Arena.getArenaByPlayer(p);
        if (a.isSpectator(p) || a.isReSpawning(p)) e.setCancelled(true);
    }

}
