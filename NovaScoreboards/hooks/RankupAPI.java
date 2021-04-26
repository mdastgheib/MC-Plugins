package com.nova.novascoreboards.hooks;

import com.nova.novascoreboards.NovaScoreboards;
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

    public static long getNextRankCost(Player player) {
        if (hasRankup() && Vault.hasVaultPermissions()) {
            try {
                File pluginDir = NovaScoreboards.getInstance().getDataFolder().getAbsoluteFile().getParentFile();
                if (pluginDir != null && pluginDir.exists()) {
                    File rankupDir = new File(pluginDir, rankupPluginName);
                    if (rankupDir.exists()) {
                        File rankupFile = new File(rankupDir, "config.yml");
                        if (rankupFile.exists()) {
                            FileConfiguration rankupConfig = YamlConfiguration.loadConfiguration(rankupFile);
                            if (rankupConfig.contains("settings.ranks")) {
                                String strCurrentRank = getRank(player);
                                if (strCurrentRank != null) {
                                    return rankupConfig.getLong("settings.ranks." + strCurrentRank + ".price", -1L);
                                }
                            }
                        }
                    }
                }
            } catch (Exception ex) {
            }
        }
        return -1L;
    }

    public static String getRank(Player player) {
        if (hasRankup() && Vault.hasVaultPermissions()) {
            try {
                File pluginDir = NovaScoreboards.getInstance().getDataFolder().getAbsoluteFile().getParentFile();
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
        return Vault.hasVaultPermissions() ? Vault.getPermissions().getPrimaryGroup(player) : "Default";
    }

    public static boolean hasRankup() {
        return Bukkit.getServer().getPluginManager().isPluginEnabled(rankupPluginName);
    }

}
