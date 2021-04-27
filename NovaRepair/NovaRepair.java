package me.nova.novarepair;
import me.nova.novarepair.utils.HoloAPI;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class NovaRepair extends JavaPlugin {
    private static NovaRepair pluginInstance = null;
    private static Economy vaultEconomy = null;

    private short repairDurability = 2;
    private boolean fixed = false;
    private double fixedCost = 5000D;
    private double fortuneMultiplierCost = 2000D;

    private int messageDelay = 2;
    private boolean warningChat = true;
    private boolean warningHolograms = true;
    private List<String> hologramMessages = new ArrayList<String>();

    private List<UUID> repairIgnorers = null;
    public List<UUID> repairAuto = null;
    public Map<UUID, Long> messageDelays = new HashMap<UUID, Long>();

    public Permission reloadPermission = new Permission("novarepair.reload");
    public Permission repairPermission = new Permission("novarepair.repair");
    public Permission togglePermission = new Permission("novarepair.toggle");
    public Permission autoPermission = new Permission("novarepair.auto");

    @Override
    public void onEnable() {
        pluginInstance = this;

        Lang.init(this);
        this.loadConfiguration();
        this.repairAuto = new ArrayList<UUID>();

        if (this.getCommand("novarepair") != null) this.getCommand("novarepair").setExecutor(new CommandListener(this));

        this.getServer().getPluginManager().addPermission(this.reloadPermission);
        this.getServer().getPluginManager().addPermission(this.repairPermission);
        this.getServer().getPluginManager().addPermission(this.togglePermission);
        this.getServer().getPluginManager().addPermission(this.autoPermission);
        this.getServer().getPluginManager().registerEvents(new EventListener(this), this);

        if (!this.setupEconomy()) {
            this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "*** Failed to load Vault Economy! ***");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.getServer().getScheduler().runTaskTimer(this, new Runnable() {
            @Override
            public void run() {
                HoloAPI.clearOldHolograms();
            }
        }, 10L, 10L);
    }

    @Override
    public void onDisable() {
        this.getServer().getScheduler().cancelTasks(this);
        HoloAPI.clearHolograms();

        this.getServer().getPluginManager().removePermission(this.reloadPermission);
        this.getServer().getPluginManager().removePermission(this.repairPermission);
        this.getServer().getPluginManager().removePermission(this.togglePermission);
        this.getServer().getPluginManager().removePermission(this.autoPermission);

        this.repairIgnorers.clear();
        this.repairIgnorers = null;

        this.repairAuto.clear();
        this.repairAuto = null;

        this.messageDelays.clear();
        this.messageDelays = null;

        pluginInstance = null;
    }

    public boolean addIgnorer(Player player) {
        if (player != null && this.repairIgnorers != null) {
            if (!this.repairIgnorers.contains(player.getUniqueId())) {
                this.repairIgnorers.add(player.getUniqueId());
                List<String> configIgnorers = this.getConfig().getStringList("Ignorers");
                if (configIgnorers != null && !configIgnorers.contains(player.getUniqueId().toString())) {
                    configIgnorers.add(player.getUniqueId().toString());
                    this.getConfig().set("Ignorers", configIgnorers);
                    this.saveConfig();
                }
                return true;
            }
        }
        return false;
    }

    public boolean canWarn(int warningType) {
        return warningType == 1 ? this.warningChat : (warningType == 2 ? this.warningHolograms : false);
    }

    public double getCost(int fortuneLevel) {
        return this.fixed ? this.fixedCost : (fortuneLevel <= 0 ? this.fortuneMultiplierCost / 2 : this.fortuneMultiplierCost * fortuneLevel);
    }

    public List<String> getHologramMessages() {
        return this.hologramMessages;
    }

    public int getMessageDelay() {
        return this.messageDelay;
    }

    public short getRepairDurability() {
        return this.repairDurability;
    }

    public boolean isIgnorer(Player player) {
        return player != null && this.repairIgnorers != null && this.repairIgnorers.contains(player.getUniqueId());
    }

    public void loadConfiguration() {
        this.getConfig().options().header("NovaRepair configuration");
        this.getConfig().addDefault("Repair durability", (short) 2);
        this.getConfig().addDefault("Fixed price", true);
        this.getConfig().addDefault("Cost.Fixed", 5000D);
        this.getConfig().addDefault("Cost.Fortune multiplier", 2000D);
        this.getConfig().addDefault("Message delay", 2);
        this.getConfig().addDefault("Warnings.Chat", true);
        this.getConfig().addDefault("Warnings.Holograms", true);
        this.getConfig().addDefault("Hologram messages", Arrays.asList("&bType &c/novarepair &bto repair your pickaxe!", "&bType &c/novarepair off &bto ignore."));
        this.getConfig().addDefault("Ignorers", new ArrayList<String>());
        this.getConfig().options().copyDefaults(true);
        this.getConfig().options().copyHeader(true);
        this.saveConfig();

        this.repairDurability = Short.parseShort(this.getConfig().get("Repair durability", (short) 2).toString());
        this.fixed = this.getConfig().getBoolean("Fixed price", true);
        this.fixedCost = this.getConfig().getDouble("Cost.Fixed", 5000D);
        this.fortuneMultiplierCost = this.getConfig().getDouble("Cost.Fortune multiplier", 2000D);

        this.messageDelay = this.getConfig().getInt("Message delay", 2);
        this.warningChat = this.getConfig().getBoolean("Warnings.Chat", true);
        this.warningHolograms = this.getConfig().getBoolean("Warnings.Holograms", true);

        if (this.fixedCost < 0D) this.fixedCost = 5000D;
        if (this.fortuneMultiplierCost < 0D) this.fortuneMultiplierCost = 2000D;
        if (this.messageDelay < 0) this.messageDelay = 2;

        this.hologramMessages = Lang.replaceChatColours(this.getConfig().getStringList("Hologram messages"));

        this.repairIgnorers = new ArrayList<UUID>();
        List<String> configIgnorers = this.getConfig().getStringList("Ignorers");
        for (String configIgnorer : configIgnorers) {
            try {
                this.repairIgnorers.add(UUID.fromString(configIgnorer));
            } catch (Exception ex) {
                continue;
            }
        }
    }

    public boolean removeIgnorer(Player player) {
        if (player != null && this.repairIgnorers != null && this.repairIgnorers.contains(player.getUniqueId())) {
            this.repairIgnorers.remove(player.getUniqueId());
            List<String> configIgnorers = this.getConfig().getStringList("Ignorers");
            if (configIgnorers != null) {
                configIgnorers.remove(player.getUniqueId().toString());
                this.getConfig().set("Ignorers", configIgnorers);
                this.saveConfig();
            }
            return true;
        }
        return false;
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = this.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) vaultEconomy = economyProvider.getProvider();
        return (vaultEconomy != null);
    }

    public static Economy getEconomy() {
        return vaultEconomy;
    }

    public static NovaRepair getInstance() {
        return pluginInstance;
    }

}
