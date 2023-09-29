package com.andrei1058.bedwars.commands.bedwars.subcmds.regular;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.command.ParentCommand;
import com.andrei1058.bedwars.api.command.SubCommand;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.api.server.ServerType;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.Misc;
import com.andrei1058.bedwars.arena.tasks.RefreshAvailableArenaTask;
import com.andrei1058.bedwars.commands.bedwars.MainCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static com.andrei1058.bedwars.BedWars.getParty;
import static com.andrei1058.bedwars.BedWars.getServerType;
import static com.andrei1058.bedwars.api.language.Language.getMsg;

public class CmdPlayAgain extends SubCommand {

    public static String Group = "";

    public CmdPlayAgain(ParentCommand parent, String name) {
        super(parent, name);
        showInList(true);
        setDisplayInfo(com.andrei1058.bedwars.commands.bedwars.MainCommand.createTC("§6 ▪ §7/" + MainCommand.getInstance().getName() + " " + getSubCommandName() + " §8 - §e再来一局。",
                "/" + getParent().getName() + " " + getSubCommandName(), "§f再来一局。"));
    }

    @Override
    public boolean execute(String[] args, CommandSender s) {
        if (!(s instanceof Player)) return true;
        Player p = (Player) s;

        //判断是否可以执行
        IArena a = Arena.getArenaByPlayer(p);
        if (a == null) return true;

        Bukkit.getScheduler().runTaskLaterAsynchronously(BedWars.plugin, () -> Misc.moveToLobbyOrKick(p, a, a.isSpectator(p.getUniqueId())), 20L);
        if (!RefreshAvailableArenaTask.isArenaAvailable() || getServerType() != ServerType.BUNGEE && !BedWars.getAPI().getArenaUtil().canAutoScale(a.getArenaName())) {
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
            Bukkit.dispatchCommand(p, "sj fastjoin " + Group);
        } else {
            IArena targetArena = Arena.getArenas().get(RefreshAvailableArenaTask.getAvailableArena());
            if (getParty().hasParty(p)) {
                if (getParty().getOwner(p).equals(p)) {
                    for (Player partyPlayers : getParty().getMembers(p)) {
                        if (partyPlayers == null) continue;
                        if (a.isPlayer(p)) {
                            a.removePlayer(partyPlayers, false);
                        } else {
                            a.removeSpectator(partyPlayers, false);
                        }
                        Bukkit.getScheduler().runTaskLater(BedWars.plugin, () -> targetArena.addPlayer(partyPlayers, true), 10L);
                    }
                } else {
                    p.sendMessage(getMsg(p, Messages.COMMAND_JOIN_DENIED_NOT_PARTY_LEADER));
                }
            } else {
                if (a.isPlayer(p)) {
                    a.removePlayer(p, false);
                } else {
                    a.removeSpectator(p, false);
                }
                Bukkit.getScheduler().runTaskLater(BedWars.plugin, () -> targetArena.addPlayer(p, true), 10L);
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
