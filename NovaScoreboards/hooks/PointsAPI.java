package com.nova.novascoreboards.hooks;

import org.bukkit.Bukkit;

/**
 * Points API
 */
public class PointsAPI {

    public static org.black_ixx.playerpoints.PlayerPoints getPlayerPoints() {
        if (hasPlayerPoints()) {
            try {
                return (org.black_ixx.playerpoints.PlayerPoints) Bukkit.getServer().getPluginManager().getPlugin("PlayerPoints");
            } catch (Exception ex) {
            }
        }
        return null;
    }

    public static boolean hasPlayerPoints() {
        return Bukkit.getServer().getPluginManager().isPluginEnabled("PlayerPoints");
    }

}
