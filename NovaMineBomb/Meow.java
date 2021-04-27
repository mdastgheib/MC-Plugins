package com.novaprison.meow;

import com.novaprison.meow.commands.CmdMinebomb;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import static com.sk89q.worldguard.bukkit.BukkitUtil.toVector;

public class Meow extends JavaPlugin {
    public static JavaPlugin pluginInstance;

    @Override
    public void onEnable() {
        pluginInstance = this;

        this.getCommand("minebomb").setExecutor(new CmdMinebomb());

        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        Config.setupConfig();
    }

    @Override
    public void onDisable() {
        this.getServer().getScheduler().cancelTasks(this);
        Config.noPickup.clear();
        Config.cooldown.clear();

        pluginInstance = null;
    }

    public static WorldGuardPlugin getWorldGuard() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) return null;
        return (WorldGuardPlugin) plugin;
    }

    public static boolean isWithinRegion(Block block, String region) {
        return isWithinRegion(block.getLocation(), region);
    }

    public static boolean isWithinRegion(Location loc, String region) {
        WorldGuardPlugin worldGuard = getWorldGuard();
        if (worldGuard == null) return true;
        Vector v = toVector(loc);
        RegionManager manager = worldGuard.getRegionManager(loc.getWorld());
        ApplicableRegionSet set = manager.getApplicableRegions(v);
        for (ProtectedRegion each : set) {
            if (each.getId().equalsIgnoreCase(region)) {
                return true;
            }
        }
        return false;
    }
}
