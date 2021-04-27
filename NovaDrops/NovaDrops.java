package me.nova.novadrops;

import me.nova.novadrops.commands.AutoSmeltCommand;
import me.nova.novadrops.commands.DropsCommand;
import me.nova.novadrops.commands.NovaDropsCommand;
import me.nova.novadrops.events.EventListener;
import me.nova.novadrops.util.DebugManager;
import me.nova.novadrops.util.HoloAPI;
import me.nova.novadrops.util.MessageUtil;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NovaDrops extends JavaPlugin {
    public static String fullInv;
    public static String world;
    private static NovaDrops plugin;
    public static List<String> allowedWorlds;
    public static List<UUID> playerSmelt = new ArrayList<UUID>();
    public static List<UUID> playerDrops = new ArrayList<UUID>();

    public void onEnable() {
        plugin = this;

        this.saveDefaultConfig();
        this.loadConfig();

        this.getCommand("autosmelt").setExecutor(new AutoSmeltCommand(this));
        this.getCommand("drops").setExecutor(new DropsCommand(this));
        this.getCommand("novadrops").setExecutor(new NovaDropsCommand(this));
        this.getServer().getPluginManager().registerEvents(new EventListener(), this);

        this.getServer().getScheduler().runTaskTimer(this, new Runnable() {
            @Override
            public void run() {
                HoloAPI.clearOldHolograms();
            }
        }, 10L, 10L);
    }

    public void onDisable() {
        this.getServer().getScheduler().cancelTasks(this);
        HoloAPI.clearHolograms();
        MessageUtil.clearLastMessages();
        DebugManager.clearDebuggers();

        if (allowedWorlds != null) allowedWorlds.clear();
        playerSmelt.clear();
        playerDrops.clear();

        plugin = null;
    }

    public void loadConfig() {
        allowedWorlds = this.getConfig().getStringList("allowedWorlds");
        fullInv = this.getConfig().getString("messages.fullinv");
        world = this.getConfig().getString("world");
    }

    public static NovaDrops getInstance() {
        return plugin;
    }
}