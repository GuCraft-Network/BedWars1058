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
            // 处理列表为空的情况
            availableArena = -1;
            return;
        }

        int maxPlayers = -1;
        int smallestIndex = Integer.MAX_VALUE;
        boolean arenaFound = false;

        for (int i = 0; i < arenas.size(); i++) {
            IArena arena = arenas.get(i);
            int numPlayers = arena.getPlayers().size();

            if (arena.getStatus() != GameState.playing && arena.getStatus() != GameState.restarting) {
                // 找到可用竞技场，设置标志并跳出循环
                availableArena = i;
                arenaFound = true;
                break;
            }

            if (numPlayers > maxPlayers) {
                // 当前竞技场人数更多，更新最大人数和最小索引
                maxPlayers = numPlayers;
                smallestIndex = i;
            } else if (numPlayers == maxPlayers && i < smallestIndex) {
                // 当前竞技场人数与最大人数相等，比较索引，取较小者
                smallestIndex = i;
            }
        }

        if (!arenaFound) {
            // 如果没有找到可用竞技场，则使用最小索引
            availableArena = smallestIndex;
        }
    }
}