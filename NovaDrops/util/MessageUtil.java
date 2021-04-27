package me.nova.novadrops.util;


import me.nova.novadrops.NovaDrops;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MessageUtil {
    private static Map<UUID, Long> lastMessage = new HashMap<UUID, Long>();

    public static boolean canSendMessage(Player player) {
        return player != null && (!lastMessage.containsKey(player.getUniqueId()) || System.currentTimeMillis() - lastMessage.get(player.getUniqueId()).longValue() >= 1000);
    }

    public static void clearLastMessages() {
        lastMessage.clear();
    }

    public static void clearMessage(Player player) {
        if (player != null) lastMessage.remove(player.getUniqueId());
    }

    public static String replace(String msg) {
        return msg != null ? ChatColor.translateAlternateColorCodes('&', msg) : "";
    }

    public static List<String> replace(List<String> lines) {
        if (lines != null) {
            for (int i = 0; i < lines.size(); i++) {
                lines.set(i, replace(lines.get(i)));
            }
        }
        return lines;
    }

    public static void sendMessage(Player player, String message) {
        if (player != null) {
            if (lastMessage.containsKey(player.getUniqueId())) {
                Long lastMsg = lastMessage.get(player.getUniqueId());
                if (lastMsg != null) {
                    if (System.currentTimeMillis() - lastMsg.longValue() >= 1000)
                        lastMessage.remove(player.getUniqueId());
                } else {
                    lastMessage.remove(player.getUniqueId());
                }
            }
            if (!lastMessage.containsKey(player.getUniqueId())) {
                if (NovaDrops.getInstance().getConfig().getBoolean("chat", true)) {
                    player.sendMessage(replace(message));
                    lastMessage.put(player.getUniqueId(), System.currentTimeMillis());
                }
            }
        }
    }

}
