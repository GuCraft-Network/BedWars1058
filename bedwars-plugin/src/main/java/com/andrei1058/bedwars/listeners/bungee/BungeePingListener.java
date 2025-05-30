package com.andrei1058.bedwars.listeners.bungee;

import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.tasks.RefreshAvailableArenaTask;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class BungeePingListener implements Listener {

    /**
     * 这个东西没什么用, 挺弱智的, 容易匹不到一块
     * 仅在BUNGEE(不是_LEGACY!) 模式时使用
     * 背景: 大概当时某人说机子不够, 我就压到两个子服
     * 作用是把当前子服上 人数最多 并且处于 等待中 的 游戏
     * 挑选设为加入子服后首要加入的地图, 并且同步到MOTD人数(BungeePingListener)
     * BUNGEE模式可以绕过bw1058proxy直接加入游戏
     * 25/5/30
     */

    @EventHandler
    public void onPing(ServerListPingEvent e) {
        if (!Arena.getArenas().isEmpty()) {
            if (RefreshAvailableArenaTask.getAvailableArena() != -1) {
                IArena a = Arena.getArenas().get(RefreshAvailableArenaTask.getAvailableArena());
                if (a != null) {
                    e.setMotd(a.getDisplayStatus(Language.getDefaultLanguage()));
                }
            } else {
                e.setMotd(Language.getDefaultLanguage().m(Messages.MEANING_FULL));
            }
        }
    }
}