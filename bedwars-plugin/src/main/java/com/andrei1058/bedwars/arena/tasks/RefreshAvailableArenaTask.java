package com.andrei1058.bedwars.arena.tasks;

import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.arena.Arena;

import java.util.LinkedList;

public class RefreshAvailableArenaTask implements Runnable {

    /**
     * 这个东西没什么用, 挺弱智的, 容易匹不到一块
     * 仅在BUNGEE(不是_LEGACY!) 模式时使用
     * 背景: 大概当时某人说机子不够, 我就压到两个子服
     * 作用是把当前子服上 人数最多 并且处于 等待中 的 游戏
     * 挑选设为加入子服后首要加入的地图, 并且同步到MOTD人数(BungeePingListener)
     * BUNGEE模式可以绕过bw1058proxy直接加入游戏
     * 25/5/30
     */

    public static int availableArena = -1;

    public static int getAvailableArena() {
        return availableArena;
    }

    @Override
    public void run() {
        LinkedList<IArena> arenas = Arena.getArenas();

        if (arenas.isEmpty()) {
            availableArena = -1;
            // 处理列表为空的情况
            return;
        }

        int maxPlayers = -1;
        int maxPlayersIndex = -1;

        for (int i = 0; i < arenas.size(); i++) {
            IArena arena = arenas.get(i);
            int numPlayers = arena.getPlayers().size();

            if (arena.getStatus() == GameState.waiting || arena.getStatus() == GameState.starting && arena.getStartingTask().getCountdown() > 1) {
                if (numPlayers > maxPlayers || numPlayers == maxPlayers && i < maxPlayersIndex) {
                    // 找到人数最多的竞技场
                    maxPlayers = numPlayers;
                    maxPlayersIndex = i;
                }
            }
        }

        availableArena = maxPlayersIndex;
    }
}