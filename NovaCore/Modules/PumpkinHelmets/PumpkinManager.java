package net.novaprison.Modules.PumpkinHelmets;

import net.novaprison.Core;
import net.novaprison.Modules.SettingsManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PumpkinManager {

    public static ItemStack helmet = new ItemStack(Material.PUMPKIN);

    public static void setupHelmet()
    {
        ItemMeta itemMeta = helmet.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', SettingsManager.helmet_name));
        itemMeta.setLore(SettingsManager.helmet_lore);
        helmet.setItemMeta(itemMeta);
    }

    public static int counter = 0;
    public static int color = 0;
    public static ArrayList<UUID> suffocating = new ArrayList<UUID>();
    public static HashMap<UUID, Integer> players = new HashMap<UUID, Integer>();

    /*
        Core and Config variables
     */
    private static Core getCore()
    {
        return (Core) Core.getInstance();
    }
    private static FileConfiguration getConfig()
    {
        return getCore().getConfig();
    }

    /*
        Destroy static variables
     */
    public static void destroy(Core core)
    {
        helmet = null;
        counter = -1;
        suffocating = null;
        players = null;
    }
}
