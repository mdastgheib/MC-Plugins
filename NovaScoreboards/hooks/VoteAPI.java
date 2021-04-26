package com.nova.novascoreboards.hooks;

import com.nova.novascoreboards.NovaScoreboards;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * Vote API.
 */
public class VoteAPI {
    private static final String votePluginName = "VoteCountdown";

    public static int getVotes() {
        if (hasVotes()) {
            try {
                File pluginDir = NovaScoreboards.getInstance().getDataFolder().getAbsoluteFile().getParentFile();
                if (pluginDir != null && pluginDir.exists()) {
                    File voteDir = new File(pluginDir, votePluginName);
                    if (voteDir.exists()) {
                        File voteFile = new File(voteDir, "config.yml");
                        if (voteFile.exists()) {
                            FileConfiguration voteConfig = YamlConfiguration.loadConfiguration(voteFile);
                            return voteConfig.getInt("vote-count", 0);
                        }
                    }
                }
            } catch (Exception ex) {
            }
        }
        return 0;
    }

    public static int getVotesLeft() {
        if (hasVotes()) {
            try {
                File pluginDir = NovaScoreboards.getInstance().getDataFolder().getAbsoluteFile().getParentFile();
                if (pluginDir != null && pluginDir.exists()) {
                    File voteDir = new File(pluginDir, votePluginName);
                    if (voteDir.exists()) {
                        File voteFile = new File(voteDir, "config.yml");
                        if (voteFile.exists()) {
                            FileConfiguration voteConfig = YamlConfiguration.loadConfiguration(voteFile);
                            int votesLeft = voteConfig.getInt("vote-amount", 0) - voteConfig.getInt("vote-count", 0);
                            return votesLeft >= 0 ? votesLeft : 0;
                        }
                    }
                }
            } catch (Exception ex) {
            }
        }
        return 0;
    }

    public static boolean hasVotes() {
        return Bukkit.getServer().getPluginManager().isPluginEnabled("VoteCountdown");
    }

}
