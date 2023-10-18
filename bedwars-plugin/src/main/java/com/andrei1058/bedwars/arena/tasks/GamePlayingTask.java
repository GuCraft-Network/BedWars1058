/*
 * BedWars1058 - A bed wars mini-game.
 * Copyright (C) 2021 Andrei Dascălu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * Contact e-mail: andrew.dascalu@gmail.com
 */

package com.andrei1058.bedwars.arena.tasks;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.generator.IGenerator;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.configuration.ConfigPath;
import com.andrei1058.bedwars.api.events.player.PlayerInvisibilityPotionEvent;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.api.tasks.PlayingTask;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.Misc;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;

import static com.andrei1058.bedwars.BedWars.getChatSupport;
import static com.andrei1058.bedwars.BedWars.nms;
import static com.andrei1058.bedwars.api.language.Language.getMsg;

public class GamePlayingTask implements Runnable, PlayingTask {

    private final Arena arena;
    private final BukkitTask task;
    private int beds_destroy_countdown, dragon_spawn_countdown, game_end_countdown;

    public GamePlayingTask(Arena arena) {
        this.arena = arena;
        this.beds_destroy_countdown = BedWars.config.getInt(ConfigPath.GENERAL_CONFIGURATION_BEDS_DESTROY_COUNTDOWN);
        this.dragon_spawn_countdown = BedWars.config.getInt(ConfigPath.GENERAL_CONFIGURATION_DRAGON_SPAWN_COUNTDOWN);
        this.game_end_countdown = BedWars.config.getInt(ConfigPath.GENERAL_CONFIGURATION_GAME_END_COUNTDOWN);
        this.task = Bukkit.getScheduler().runTaskTimer(BedWars.plugin, this, 0, 20L);
    }

    public Arena getArena() {
        return arena;
    }

    @Override
    public BukkitTask getBukkitTask() {
        return task;
    }

    /**
     * Get task ID
     */
    public int getTask() {
        return task.getTaskId();
    }

    public int getBedsDestroyCountdown() {
        return beds_destroy_countdown;
    }

    public int getDragonSpawnCountdown() {
        return dragon_spawn_countdown;
    }

    public int getGameEndCountdown() {
        return game_end_countdown;
    }

    @Override
    public void run() {
        switch (getArena().getNextEvent()) {
            case EMERALD_GENERATOR_TIER_II:
            case EMERALD_GENERATOR_TIER_III:
            case DIAMOND_GENERATOR_TIER_II:
            case DIAMOND_GENERATOR_TIER_III:
                if (getArena().upgradeDiamondsCount > 0) {
                    getArena().upgradeDiamondsCount--;
                    if (getArena().upgradeDiamondsCount == 0) {
                        getArena().updateNextEvent();
                    }
                }
                if (getArena().upgradeEmeraldsCount > 0) {
                    getArena().upgradeEmeraldsCount--;
                    if (getArena().upgradeEmeraldsCount == 0) {
                        getArena().updateNextEvent();
                    }
                }
                break;
            case BEDS_DESTROY:
                beds_destroy_countdown--;
                if (getBedsDestroyCountdown() == 0) {
                    for (Player p : getArena().getPlayers()) {
                        nms.sendTitle(p, getMsg(p, Messages.NEXT_EVENT_TITLE_ANNOUNCE_BEDS_DESTROYED), getMsg(p, Messages.NEXT_EVENT_SUBTITLE_ANNOUNCE_BEDS_DESTROYED), 0, 40, 10);
                        p.sendMessage(getMsg(p, Messages.NEXT_EVENT_CHAT_ANNOUNCE_BEDS_DESTROYED));
                    }
                    for (Player p : getArena().getSpectators()) {
                        nms.sendTitle(p, getMsg(p, Messages.NEXT_EVENT_TITLE_ANNOUNCE_BEDS_DESTROYED), getMsg(p, Messages.NEXT_EVENT_SUBTITLE_ANNOUNCE_BEDS_DESTROYED), 0, 40, 10);
                        p.sendMessage(getMsg(p, Messages.NEXT_EVENT_CHAT_ANNOUNCE_BEDS_DESTROYED));
                    }
                    for (ITeam t : getArena().getTeams()) {
                        t.setBedDestroyed(true);
                    }
                    getArena().updateNextEvent();
                }
                break;
            case ENDER_DRAGON:
                dragon_spawn_countdown--;
                if (getDragonSpawnCountdown() == 0) {
                    for (Player p : getArena().getPlayers()) {
                        nms.sendTitle(p, getMsg(p, Messages.NEXT_EVENT_TITLE_ANNOUNCE_SUDDEN_DEATH), getMsg(p, Messages.NEXT_EVENT_SUBTITLE_ANNOUNCE_SUDDEN_DEATH), 0, 40, 10);
                        for (ITeam t : getArena().getTeams()) {
                            if (t.getMembers().isEmpty()) continue;
                            p.sendMessage(getMsg(p, Messages.NEXT_EVENT_CHAT_ANNOUNCE_SUDDEN_DEATH).replace("{TeamDragons}", String.valueOf(t.getDragons()))
                                    .replace("{TeamColor}", t.getColor().chat().toString()).replace("{TeamName}", t.getDisplayName(Language.getPlayerLanguage(p))));
                        }
                    }
                    for (Player p : getArena().getSpectators()) {
                        nms.sendTitle(p, getMsg(p, Messages.NEXT_EVENT_TITLE_ANNOUNCE_SUDDEN_DEATH), getMsg(p, Messages.NEXT_EVENT_SUBTITLE_ANNOUNCE_SUDDEN_DEATH), 0, 40, 10);
                        for (ITeam t : getArena().getTeams()) {
                            if (t.getMembers().isEmpty()) continue;
                            p.sendMessage(getMsg(p, Messages.NEXT_EVENT_CHAT_ANNOUNCE_SUDDEN_DEATH).replace("{TeamDragons}", String.valueOf(t.getDragons()))
                                    .replace("{TeamColor}", t.getColor().chat().toString()).replace("{TeamName}", t.getDisplayName(Language.getPlayerLanguage(p))));
                        }
                    }
                    getArena().updateNextEvent();
                }
                break;
            case GAME_END:
                game_end_countdown--;
                if (getGameEndCountdown() == 0) {
                    getArena().checkWinner();
                    getArena().changeStatus(GameState.restarting);
                }
                break;
        }
        for (ITeam t : getArena().getTeams()) {
            // spawn items
            for (IGenerator o : t.getGenerators()) {
                o.spawn();
            }
            /*
        int distance = 0;
            if (t.getSize() > 1) {
                for (Player p : t.getMembers()) {
                    for (Player p2 : t.getMembers()) {
                        if (!getMsg(p,Messages.FORMATTING_ACTION_BAR_TRACKING).isEmpty() || p2 == p || !p.getLocation().getWorld().equals(p2.getLocation().getWorld())) continue;
                        if (distance == 0) {
                            distance = (int) p.getLocation().distance(p2.getLocation());
                        } else if ((int) p.getLocation().distance(p2.getLocation()) < distance) {
                            distance = (int) p.getLocation().distance(p2.getLocation());
                        }
                    }
                    nms.playAction(p, getMsg(p, Messages.FORMATTING_ACTION_BAR_TRACKING).replace("{team}", t.getColor().chat() + t.getDisplayName(Language.getPlayerLanguage(p)))
                            .replace("{distance}", t.getColor().chat().toString() + distance).replace("&", "§"));
                }
            }*/
        }

        /* AFK SYSTEM FOR PLAYERS */
        int current = 0;
        for (Player p : getArena().getPlayers()) {
            if (Arena.afkCheck.get(p.getUniqueId()) == null) {
                Arena.afkCheck.put(p.getUniqueId(), current);
            } else {
                current = Arena.afkCheck.get(p.getUniqueId());
                current++;
                Arena.afkCheck.replace(p.getUniqueId(), current);
                if (current == 45) {
                    BedWars.getAPI().getAFKUtil().setPlayerAFK(p, true);
                }
                if (current >= 120) {
                    for (int i = 0; i < 10; i++) {
                        final int ticks = i * 2;
                        Bukkit.getScheduler().runTaskLaterAsynchronously(BedWars.plugin,
                                () -> p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0F, 1.0F), ticks);
                    }
                    if (current == 120) {
                        p.sendMessage("§c你将因挂机而被移出游戏。");
                    }
                }
                if (current == 130) {
                    for (Player arenaPlayers : getArena().getPlayers()) {
                        arenaPlayers.sendMessage(arena.getTeam(p).getColor().chat() + p.getDisplayName() + getChatSupport().getSuffix(p) + "§7因挂机离开了游戏。");
                    }
                    Misc.moveToLobbyOrKick(p, arena, arena.isSpectator(p));
                }
            }
        }

        /* INVISIBILITY FOR ARMOR */
        if (!getArena().getShowTime().isEmpty()) {
            for (Map.Entry<Player, Integer> e : getArena().getShowTime().entrySet()) {
                if (e.getValue() <= 0) {
                    for (Player p : e.getKey().getWorld().getPlayers()) {
                        nms.showArmor(e.getKey(), p);
                        //nms.showPlayer(e.getKey(), p);
                    }
                    e.getKey().removePotionEffect(PotionEffectType.INVISIBILITY);
                    Bukkit.getPluginManager().callEvent(new PlayerInvisibilityPotionEvent(PlayerInvisibilityPotionEvent.Type.REMOVED, getArena().getTeam(e.getKey()), e.getKey(), getArena()));
                } else {
                    getArena().getShowTime().replace(e.getKey(), e.getValue() - 1);
                }
            }
        }

        /* SPAWN ITEMS */
        for (IGenerator o : getArena().getOreGenerators()) {
            o.spawn();
        }
    }

    public void cancel() {
        task.cancel();
    }
}


