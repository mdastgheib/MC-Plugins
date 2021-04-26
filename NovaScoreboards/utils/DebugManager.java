package com.nova.novascoreboards.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DebugManager {
    private static List<UUID> pluginDebuggers = new ArrayList<UUID>();

    public static void sendMessage(String message) {
        if (message != null) {
            message = ChatColor.translateAlternateColorCodes('&', message);

            List<Integer> removeIndexes = new ArrayList<Integer>();
            for (int i = 0; i < pluginDebuggers.size(); i++) {
                UUID pluginDebugger = pluginDebuggers.get(i);
                if (pluginDebugger != null) {
                    Player debugger = Bukkit.getPlayer(pluginDebugger);
                    if (debugger != null) {
                        debugger.sendMessage(message.replace("<player>", debugger.getName()).replace("<uuid>", debugger.getUniqueId().toString()).replace("<health>", String.valueOf(debugger.getHealth()).replace("<hunger>", String.valueOf(debugger.getFoodLevel()))));
                    } else {
                        removeIndexes.add(i);
                    }
                } else {
                    removeIndexes.add(i);
                }
            }
            for (Integer integer : removeIndexes) {
                removeIndexes.remove(integer.intValue());
            }
        }
    }

    public static boolean toggleDebugger(UUID playerUUID) {
        if (playerUUID != null) {
            if (pluginDebuggers.contains(playerUUID)) {
                pluginDebuggers.remove(playerUUID);
                return false;
            } else {
                pluginDebuggers.add(playerUUID);
                return true;
            }
        } else {
            throw new NullPointerException("Player UUID is null!");
        }
    }

}
