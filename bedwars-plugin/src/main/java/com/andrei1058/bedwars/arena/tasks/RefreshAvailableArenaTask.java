package com.andrei1058.bedwars.arena.tasks;

import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.arena.Arena;

import java.util.LinkedList;

public class RefreshAvailableArenaTask implements Runnable {
    public static int availableArena = -1;

    public static boolean isArenaAvailable() {
        return availableArena != -1;
    }

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

            if (arena.getStatus() != GameState.playing && arena.getStatus() != GameState.restarting) {
                if (numPlayers > maxPlayers || numPlayers == maxPlayers && i < maxPlayersIndex) {
                    // 找到人数最多的竞技场
                    maxPlayers = numPlayers;
                    maxPlayersIndex = i;
                }
            }
        }

        if (maxPlayersIndex != -1) {
            // 执行人数最多的竞技场的相关操作
            availableArena = maxPlayersIndex;
        }
    }
}