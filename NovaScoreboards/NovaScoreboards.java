package com.nova.novascoreboards;

import com.nova.novascoreboards.hooks.Vault;
import com.nova.novascoreboards.utils.TPSManager;
import com.nova.novascoreboards.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.io.File;
import java.util.*;
import java.util.EventListener;

public class NovaScoreboards extends JavaPlugin {
    private static NovaScoreboards pluginInstance = null;
    private static Random random = null;

    private Settings pluginSettings = null;

    public List<UUID> playersLeaving = new ArrayList<UUID>();
    public List<UUID> scoreboardIgnorers = new ArrayList<UUID>();
    private Map<UUID, Integer> playerKills = new HashMap<UUID, Integer>();
    private Map<UUID, Integer> playerDeaths = new HashMap<UUID, Integer>();

    public void onEnable() {
        pluginInstance = this;
        random = new Random();
        this.pluginSettings = new Settings();
        this.loadConfiguration();

        Vault.setupEconomy();
        Vault.setupPermissions();

        this.getCommand("novascoreboards").setExecutor(new CommandListener());
        this.getServer().getPluginManager().registerEvents(new EventListener(), this);

        this.startTask();
        this.getServer().getScheduler().runTaskTimer(this, new TPSManager(), 100L, 1L);
    }

    public void onDisable() {
        this.getServer().getScheduler().cancelTasks(this);

        List<? extends Player> onlinePlayers = Utils.getOnlinePlayers();
        for (Player onlinePlayer : onlinePlayers) {
            Scoreboard playerScoreboard = onlinePlayer.getScoreboard();
            if (playerScoreboard != null && playerScoreboard.getObjective("novascoreboard") != null) {
                playerScoreboard.clearSlot(DisplaySlot.SIDEBAR);
                onlinePlayer.setScoreboard(playerScoreboard);
            }
        }

        this.playersLeaving.clear();
        this.playerKills.clear();
        this.playerDeaths.clear();

        this.pluginSettings = null;
        Vault.resetInstance();
        random = null;
        pluginInstance = null;
    }

    public void loadConfiguration() {
        this.getConfig().options().header("NovaScoreboards configuration");
        this.getConfig().addDefault("Enabled worlds", Arrays.asList("world"));
        this.getConfig().addDefault("Rankup message", "Type /rankup!");
        this.getConfig().addDefault("Scoreboard title", "&6&lScoreboard");
        this.getConfig().addDefault("Scoreboard keys", Arrays.asList("&bName", "&c<name>", "&bMoney", "&c<money>"));
        this.getConfig().addDefault("Scoreboard refresh rate", 5D);
        this.getConfig().options().copyDefaults(true);
        this.getConfig().options().copyHeader(true);
        this.saveConfig();

        this.pluginSettings.setEnabledWorlds(this.getConfig().getStringList("Enabled worlds"));
        this.pluginSettings.setRankupMessage(this.getConfig().getString("Rankup message", "Type /rankup!"));
        this.pluginSettings.setRefreshRate(this.getConfig().getDouble("Scoreboard refresh rate", 5D));
        this.pluginSettings.setScoreboardTitle(this.getConfig().getString("Scoreboard title", "&6&lScoreboard"));
        this.pluginSettings.setScoreboardKeys(this.getConfig().getStringList("Scoreboard keys"));

        this.loadStats();
    }

    private void loadStats() {
        this.getStatsConfig().options().header("NovaScoreboards player statistics");
        this.getStatsConfig().options().copyHeader(true);

        this.playerKills.clear();
        this.playerDeaths.clear();
        for (Map.Entry<String, Object> playerStats : this.getStatsConfig().getValues(false).entrySet()) {
            if (playerStats.getValue() instanceof ConfigurationSection || playerStats.getValue() instanceof Map) {
                if (isUUID(playerStats.getKey())) {
                    Map<String, Object> playerStatsMap = playerStats.getValue() instanceof ConfigurationSection ? ((ConfigurationSection) playerStats.getValue()).getValues(false) : (Map<String, Object>) playerStats.getValue();
                    if (playerStatsMap.containsKey("Kills")) {
                        try {
                            this.playerKills.put(UUID.fromString(playerStats.getKey()), Integer.parseInt(playerStatsMap.get("Kills").toString()));
                        } catch (Exception ex) {
                        }
                    }
                    if (playerStatsMap.containsKey("Deaths")) {
                        try {
                            this.playerDeaths.put(UUID.fromString(playerStats.getKey()), Integer.parseInt(playerStatsMap.get("Deaths").toString()));
                        } catch (Exception ex) {
                        }
                    }
                    continue;
                }
            }
            this.getStatsConfig().set(playerStats.getKey(), null);
        }
        this.saveStatsConfig();
    }

    public String formatString(String string) {
        return string != null ? (string.length() > 16 ? string.substring(0, 16) : string) : "";
    }

    public int getDeaths(Player player) {
        return player != null ? (this.playerDeaths.containsKey(player.getUniqueId()) ? this.playerDeaths.get(player.getUniqueId()) : 0) : 0;
    }

    public int getKills(Player player) {
        return player != null ? (this.playerKills.containsKey(player.getUniqueId()) ? this.playerKills.get(player.getUniqueId()) : 0) : 0;
    }

    public Settings getSettings() {
        return this.pluginSettings;
    }

    public boolean hasDeaths(Player player) {
        return player != null && this.playerDeaths.containsKey(player.getUniqueId());
    }

    public boolean hasKills(Player player) {
        return player != null && this.playerKills.containsKey(player.getUniqueId());
    }

    public boolean inScoreboardWorld(Player player) {
        return player != null && player.getWorld() != null && this.pluginSettings.getEnabledWorlds().contains(player.getWorld().getName());
    }

    public void putDeaths(Player player, int deaths) {
        if (player != null) {
            this.playerDeaths.put(player.getUniqueId(), deaths);
            this.getStatsConfig().set(player.getUniqueId().toString() + ".Deaths", deaths);
            this.saveStatsConfig();
        }
    }

    public void putKills(Player player, int kills) {
        if (player != null) {
            this.playerKills.put(player.getUniqueId(), kills);
            this.getStatsConfig().set(player.getUniqueId().toString() + ".Kills", kills);
            this.saveStatsConfig();
        }
    }

    protected Scoreboard setScoreboard(final Player player) {
        if (player != null) {
            if (player.isOnline() && player.isValid()) {
                final Scoreboard playerScoreboard = player.getServer().getScoreboardManager().getNewScoreboard();
                playerScoreboard.clearSlot(DisplaySlot.SIDEBAR);
                Objective objBoard = playerScoreboard.registerNewObjective("novascoreboard", "dummy");
                objBoard.setDisplaySlot(DisplaySlot.SIDEBAR);
                objBoard.setDisplayName(this.formatString(this.pluginSettings.getScoreboardTitle()));

                List<String> enabledKeys = this.pluginSettings.getScoreboardKeys();
                while (enabledKeys.size() > 15) {
                    enabledKeys.remove(enabledKeys.size() - 1);
                }
                for (int keysIndex = 0; keysIndex < enabledKeys.size(); keysIndex++) {
                    objBoard.getScore(Utils.replaceFormats(player, enabledKeys.get(keysIndex))).setScore(enabledKeys.size() - keysIndex);
                }

                return playerScoreboard;
            } else {
                return player.getServer().getScoreboardManager().getNewScoreboard();
            }
        } else {
            return null;
        }
    }

    public void startTask() {
        this.getServer().getScheduler().runTaskTimer(this, new Runnable() {
            @Override
            public void run() {
                Map<Player, Scoreboard> playerScoreboards = new HashMap<Player, Scoreboard>();
                for (final Player onlinePlayer : getServer().getOnlinePlayers()) {
                    if (onlinePlayer.isValid() && onlinePlayer.isOnline() && !playersLeaving.contains(onlinePlayer.getUniqueId()) && !scoreboardIgnorers.contains(onlinePlayer.getUniqueId()))
                        playerScoreboards.put(onlinePlayer, setScoreboard(onlinePlayer));
                }
                for (final Map.Entry<Player, Scoreboard> playerEntry : playerScoreboards.entrySet()) {
                    getServer().getScheduler().runTask(getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            Player onlinePlayer = playerEntry.getKey();
                            if (playerEntry.getValue() != null && onlinePlayer.isValid() && onlinePlayer.isOnline() && !playersLeaving.contains(onlinePlayer.getUniqueId()) && !scoreboardIgnorers.contains(onlinePlayer.getUniqueId()))
                                playerEntry.getKey().setScoreboard(playerEntry.getValue());
                        }
                    });
                }
                playersLeaving.clear();
            }
        }, 20L, (long) (this.pluginSettings.getRefreshRate() * 20L));
    }

    /**
     * Get the NovaScoreboards instance.
     *
     * @return The NovaScoreboards instance.
     */
    public static NovaScoreboards getInstance() {
        return pluginInstance;
    }

    public static ChatColor getRandomColour() {
        ChatColor[] chatColors = ChatColor.values();
        return chatColors[random.nextInt(chatColors.length)];
    }

    private static boolean isUUID(String strUUID) {
        try {
            return UUID.fromString(strUUID) != null;
        } catch (Exception ex) {
            return false;
        }
    }

    private File statsFile = null;
    private FileConfiguration statsConfig = null;

    public FileConfiguration getStatsConfig() {
        if (this.statsFile == null || this.statsConfig == null) this.reloadStatsConfig();
        return this.statsConfig;
    }

    public void reloadStatsConfig() {
        if (this.statsFile == null) this.statsFile = new File(this.getDataFolder(), "stats.yml");
        this.statsConfig = YamlConfiguration.loadConfiguration(this.statsFile);
    }

    public void saveStatsConfig() {
        if (this.statsFile == null || this.statsConfig == null) return;
        try {
            this.statsConfig.save(this.statsFile);
        } catch (Exception ex) {
        }
    }
}
