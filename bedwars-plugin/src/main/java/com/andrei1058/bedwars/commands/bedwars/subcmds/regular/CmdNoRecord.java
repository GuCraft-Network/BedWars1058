package com.andrei1058.bedwars.commands.bedwars.subcmds.regular;

import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.command.ParentCommand;
import com.andrei1058.bedwars.api.command.SubCommand;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.NoRecordMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CmdNoRecord extends SubCommand {

    public CmdNoRecord(ParentCommand parent, String name) {
        super(parent, name);
        setPermission("bw.norecord");
        showInList(false);
    }

    @Override
    public boolean execute(String[] args, CommandSender s) {
        if (!(s instanceof Player)) return true;
        Player p = (Player) s;

        //判断是否可以执行
        IArena a = Arena.getArenaByPlayer(p);
        if (a == null) return true;
        if (!NoRecordMap.NoRecordMap.contains(a.getWorldName())) {
            NoRecordMap.NoRecordMap.add(a.getWorldName());
            p.sendMessage("§a成功！");
        }
        if (a.getStatus() == GameState.playing) {
            for (Player arenaPlayers : a.getPlayers()) {
                if (a.getPlayers().isEmpty()) {
                    break;
                }
                arenaPlayers.sendMessage("§c§l本场游戏不记录战绩！");
            }
        }
        return true;
    }

    @Override
    public List<String> getTabComplete() {
        return null;
    }

    @Override
    public boolean canSee(CommandSender s, com.andrei1058.bedwars.api.BedWars api) {
        return false;
    }
}
