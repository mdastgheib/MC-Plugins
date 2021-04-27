package com.nova.sellall.hooks;

import com.nova.sellall.SellAll;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

/**
 * Rankup API.
 */
public class RankupAPI {
    private static final String rankupPluginName = "ACRankup";

    public static String getRank(Player player) {
        if (hasRankup() && Vault.hasVaultPermissions()) {
            try {
                File pluginDir = SellAll.getInstance().getDataFolder().getAbsoluteFile().getParentFile();
                if (pluginDir != null && pluginDir.exists()) {
                    File rankupDir = new File(pluginDir, rankupPluginName);
                    if (rankupDir.exists()) {
                        File rankupFile = new File(rankupDir, "config.yml");
                        if (rankupFile.exists()) {
                            FileConfiguration rankupConfig = YamlConfiguration.loadConfiguration(rankupFile);
                            if (rankupConfig.contains("settings.ranks")) {
                                String[] playerRanks = Vault.getPermissions().getPlayerGroups(player);
                                String strCurrentRank = Vault.getPermissions().getPrimaryGroup(player);
                                if (strCurrentRank == null || !rankupConfig.contains("settings.ranks." + strCurrentRank)) {
                                    for (String playerRank : playerRanks) {
                                        if (rankupConfig.contains("settings.ranks." + playerRank))
                                            strCurrentRank = playerRank;
                                    }
                                }
                                return strCurrentRank == null ? "Default" : strCurrentRank;
                            }
                        }
                    }
                }
            } catch (Exception ex) {
            }
        }
        String playerRank = Vault.hasVaultPermissions() ? Vault.getPermissions().getPrimaryGroup(player) : "Default";
        return playerRank == null ? "Default" : playerRank;
    }

    public static boolean hasRankup() {
        return Bukkit.getServer().getPluginManager().isPluginEnabled(rankupPluginName);
    }
}
