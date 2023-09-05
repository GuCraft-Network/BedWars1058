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

package com.andrei1058.bedwars.upgrades.trapaction;

import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.events.player.PlayerInvisibilityPotionEvent;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.api.upgrades.TrapAction;
import com.andrei1058.bedwars.arena.Arena;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import static com.andrei1058.bedwars.api.language.Language.getMsg;

public class RemoveEffectAction implements TrapAction {

    private PotionEffectType potionEffectType;

    public RemoveEffectAction(PotionEffectType potionEffectType){
        this.potionEffectType = potionEffectType;
    }

    @Override
    public String getName() {
        return "remove-effect";
    }

    @Override
    public void onTrigger(@NotNull Player player, ITeam playerTeam, ITeam targetTeam) {
        player.removePotionEffect(potionEffectType);
        //如果有隐身效果，在触发报警陷阱时移除隐身效果以及脚印
        if (potionEffectType.equals(PotionEffectType.INVISIBILITY)) {
            IArena a = Arena.getArenaByPlayer(player);
            if (a.getShowTime().containsKey(player)) {
                player.removePotionEffect(PotionEffectType.INVISIBILITY);
                a.getShowTime().remove(player);
                player.sendMessage(getMsg(player, Messages.INTERACT_INVISIBILITY_REMOVED_TRAP));
                ITeam team = a.getTeam(player);
                Bukkit.getPluginManager().callEvent(new PlayerInvisibilityPotionEvent(PlayerInvisibilityPotionEvent.Type.REMOVED, team, player, a));
            }
        }
    }
}
