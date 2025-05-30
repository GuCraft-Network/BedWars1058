package com.andrei1058.bedwars.commands.bedwars.subcmds.regular;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.command.ParentCommand;
import com.andrei1058.bedwars.api.command.SubCommand;
import com.andrei1058.bedwars.api.server.ServerType;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.Misc;
import com.andrei1058.bedwars.arena.tasks.RefreshAvailableArenaTask;
import com.andrei1058.bedwars.commands.bedwars.MainCommand;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

import static com.andrei1058.bedwars.BedWars.getServerType;

public class CmdPlayAgain extends SubCommand {

    public static String Group = "";

    public CmdPlayAgain(ParentCommand parent, String name) {
        super(parent, name);
        showInList(true);
        setDisplayInfo(com.andrei1058.bedwars.commands.bedwars.MainCommand.createTC("§6 ▪ §7/" + MainCommand.getInstance().getName() + " " + getSubCommandName() + " §8 - §e再来一局。",
                "/" + getParent().getName() + " " + getSubCommandName(), "§f再来一局。"));
    }

    /**
     * 这个东西没什么用, 挺弱智的, 容易匹不到一块
     * 仅在BUNGEE(不是_LEGACY!) 模式时使用
     * 背景: 大概当时某人说机子不够, 我就压到两个子服
     * 作用是如果当前子服没有其他符合条件的地图, 就sj传走 顺带移回大厅
     * 25/5/30
     */

    @Override
    public boolean execute(String[] args, CommandSender s) {
        if (!(s instanceof Player)) return true;
        Player p = (Player) s;

        //判断是否可以执行
        IArena a = Arena.getArenaByPlayer(p);
        if (a == null) return true;
        if (getServerType() != ServerType.BUNGEE) return true;

        p.setGameMode(GameMode.SPECTATOR);
        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false));

        boolean isAvailable;
        if (RefreshAvailableArenaTask.getAvailableArena() == -1) {
            nextServer(p, a);
            isAvailable = false;
        } else {
            isAvailable = true;
        }

        if (isAvailable) {
            if (a.isPlayer(p)) {
                a.removePlayer(p, false);
            } else {
                a.removeSpectator(p, false);
            }

            IArena targetArena = Arena.getArenas().get(RefreshAvailableArenaTask.getAvailableArena());

            Bukkit.getScheduler().runTaskLater(BedWars.plugin, () -> {
                if (!targetArena.addPlayer(p, false)) {
                    nextServer(p, a);
                }
            }, 10L);
        }

        Bukkit.getScheduler().runTaskLater(BedWars.plugin, () -> {
            if (!isAvailable || Arena.getArenaByPlayer(p) == null || p.getWorld().getName().equals(Bukkit.getWorlds().get(0).getName())) {
                nextServer(p, a);
                Misc.moveToLobbyOrKick(p, null, true);
            }
        }, 20L);
        return true;
    }

    public void nextServer(Player p, IArena a) {
        if (Bukkit.getPluginManager().getPlugin("ServerJoiner") == null) return;
        if (a != null) {
            switch (a.getGroup()) {
                case "solo":
                    Group = "hyp1v";
                    break;
                case "doubles":
                    Group = "hyp2v";
                    break;
                case "4v4v4v4":
                    Group = "hyp4v";
                    break;
                case "rush_doubles":
                    Group = "rhyp2v";
                    break;
                case "rush_4v4v4v4":
                    Group = "rhyp4v";
                    break;
                case "ultimate_4v4v4v4":
                    Group = "uhyp4v";
                    break;
                case "swap_4v4v4v4":
                    Group = "shyp4v";
            }
            p.performCommand("sj fastjoin " + Group);
        } else {
            Misc.moveToLobbyOrKick(p, null, true);
        }
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
