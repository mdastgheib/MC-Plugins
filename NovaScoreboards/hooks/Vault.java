package com.nova.novascoreboards.hooks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * Vault API Hook.
 */
public class Vault {
    private static Object vaultEconomy = null;
    private static Object vaultPermissions = null;

    public static net.milkbowl.vault.economy.Economy getEconomy() {
        return vaultEconomy != null ? (net.milkbowl.vault.economy.Economy) vaultEconomy : null;
    }

    public static net.milkbowl.vault.permission.Permission getPermissions() {
        return vaultPermissions != null ? (net.milkbowl.vault.permission.Permission) vaultPermissions : null;
    }

    public static boolean hasVaultEconomy() {
        return vaultEconomy != null;
    }

    public static boolean hasVaultPermissions() {
        return vaultPermissions != null;
    }

    public static void resetInstance() {
        vaultEconomy = null;
        vaultPermissions = null;
    }

    public static boolean setupEconomy() {
        try {
            if (Bukkit.getServer().getPluginManager().isPluginEnabled("Vault")) {
                RegisteredServiceProvider<net.milkbowl.vault.economy.Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
                if (economyProvider != null) vaultEconomy = economyProvider.getProvider();
                return (vaultEconomy != null);
            }
        } catch (Exception ex) {
        }
        return false;
    }

    public static boolean setupPermissions() {
        try {
            if (Bukkit.getServer().getPluginManager().isPluginEnabled("Vault")) {
                RegisteredServiceProvider<net.milkbowl.vault.permission.Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
                if (permissionProvider != null) vaultPermissions = permissionProvider.getProvider();
                return vaultPermissions != null;
            }
        } catch (Exception ex) {
        }
        return false;
    }
}
