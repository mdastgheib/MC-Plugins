package com.nova.sellall.hooks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author KingFaris10
 */
public class MultiplierAPI {

    public static double getMultiplier(Player player) {
        if (player != null) {
            if (hasMultiplier()) {
                try {
                    return com.faris.multiplier.MultiplierAPI.getMultiplier(player);
                } catch (Exception ex) {
                }
            }
        }
        return 1D;
    }

    public static boolean hasMultiplier() {
        return Bukkit.getServer().getPluginManager().isPluginEnabled("NovaMultiplier");
    }

}
