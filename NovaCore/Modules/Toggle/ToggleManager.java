package net.novaprison.Modules.Toggle;

import net.novaprison.Core;
import net.novaprison.Utils.SLAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ToggleManager {

    public static String invName;

    public static void setupToggles()
    {
        File file = new File(Core.getInstance().getDataFolder() + File.separator + "togglemenu.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        invName = config.getString("ToggleInv.name");

        try {
            swearfilter_toggle = SLAPI.load("plugins/NovaCore/toggles/swearfilter.bin");
        } catch (Exception e) {
            swearfilter_toggle = new HashMap<UUID, Boolean>();
        }
        try {
            blood_toggle = SLAPI.load("plugins/NovaCore/toggles/blood.bin");
        } catch (Exception e) {
            blood_toggle = new HashMap<UUID, Boolean>();
        }
        try {
            announce_toggle = SLAPI.load("plugins/NovaCore/toggles/announce.bin");
        } catch (Exception e) {
            announce_toggle = new HashMap<UUID, Boolean>();
        }
    }

    public static void save()
    {
        try {
            SLAPI.save(swearfilter_toggle, "plugins/NovaCore/toggles/swearfilter.bin");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            SLAPI.save(blood_toggle, "plugins/NovaCore/toggles/blood.bin");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            SLAPI.save(announce_toggle, "plugins/NovaCore/toggles/announce.bin");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openMenu(Player player)
    {
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(
                Core.getInstance().getDataFolder() + File.separator + "togglemenu.yml"));
        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', config.getString("ToggleInv.name")));
        invName = ChatColor.translateAlternateColorCodes('&', config.getString("ToggleInv.name"));
        for(int i=1;i<=54;i++) {
            if(config.isSet("ToggleInv." + i + ".isSet")) {
                if(config.getString("ToggleInv." + i + ".type").equalsIgnoreCase("info")) {
                    ItemStack item = new ItemStack(config.getInt("ToggleInv." + i + ".itemID"));
                    item.setDurability((short) config.getInt("ToggleInv." + i + ".itemData"));
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getString("ToggleInv." + i + ".itemName")));
                    ArrayList<String> lore = new ArrayList<String>();
                    for(String string : config.getStringList("ToggleInv." + i + ".lore")) {
                        lore.add(ChatColor.translateAlternateColorCodes('&', string));
                    }
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    inv.setItem(i-1, item);
                } else if(config.getString("ToggleInv." + i + ".type").equalsIgnoreCase("toggle")) {
                    try {
                        String toggle = config.getString("ToggleInv." + i + ".toggle");
                        if (toggle.equalsIgnoreCase("swear")) {
                            if (ToggleManager.swearfilter_toggle.get(player.getUniqueId())) {
                                ItemStack item = new ItemStack(config.getInt("ToggleInv." + i + ".on.itemID"));
                                item.setDurability((short) config.getInt("ToggleInv." + i + ".on.itemData"));
                                ItemMeta meta = item.getItemMeta();
                                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getString("ToggleInv." + i + ".on.itemName")));
                                ArrayList<String> lore = new ArrayList<String>();
                                for (String string : config.getStringList("ToggleInv." + i + ".on.lore")) {
                                    lore.add(ChatColor.translateAlternateColorCodes('&', string));
                                }
                                meta.setLore(lore);
                                item.setItemMeta(meta);
                                inv.setItem(i - 1, item);
                            } else {
                                ItemStack item = new ItemStack(config.getInt("ToggleInv." + i + ".off.itemID"));
                                item.setDurability((short) config.getInt("ToggleInv." + i + ".off.itemData"));
                                ItemMeta meta = item.getItemMeta();
                                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getString("ToggleInv." + i + ".off.itemName")));
                                ArrayList<String> lore = new ArrayList<String>();
                                for (String string : config.getStringList("ToggleInv." + i + ".off.lore")) {
                                    lore.add(ChatColor.translateAlternateColorCodes('&', string));
                                }
                                meta.setLore(lore);
                                item.setItemMeta(meta);
                                inv.setItem(i - 1, item);
                            }
                        } else if (toggle.equalsIgnoreCase("blood")) {
                            if (ToggleManager.blood_toggle.get(player.getUniqueId())) {
                                ItemStack item = new ItemStack(config.getInt("ToggleInv." + i + ".on.itemID"));
                                item.setDurability((short) config.getInt("ToggleInv." + i + ".on.itemData"));
                                ItemMeta meta = item.getItemMeta();
                                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getString("ToggleInv." + i + ".on.itemName")));
                                ArrayList<String> lore = new ArrayList<String>();
                                for (String string : config.getStringList("ToggleInv." + i + ".on.lore")) {
                                    lore.add(ChatColor.translateAlternateColorCodes('&', string));
                                }
                                meta.setLore(lore);
                                item.setItemMeta(meta);
                                inv.setItem(i - 1, item);
                            } else {
                                ItemStack item = new ItemStack(config.getInt("ToggleInv." + i + ".off.itemID"));
                                item.setDurability((short) config.getInt("ToggleInv." + i + ".off.itemData"));
                                ItemMeta meta = item.getItemMeta();
                                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getString("ToggleInv." + i + ".off.itemName")));
                                ArrayList<String> lore = new ArrayList<String>();
                                for (String string : config.getStringList("ToggleInv." + i + ".off.lore")) {
                                    lore.add(ChatColor.translateAlternateColorCodes('&', string));
                                }
                                meta.setLore(lore);
                                item.setItemMeta(meta);
                                inv.setItem(i - 1, item);
                            }
                        } else if (toggle.equalsIgnoreCase("announce")) {
                            if (ToggleManager.announce_toggle.get(player.getUniqueId())) {
                                ItemStack item = new ItemStack(config.getInt("ToggleInv." + i + ".on.itemID"));
                                item.setDurability((short) config.getInt("ToggleInv." + i + ".on.itemData"));
                                ItemMeta meta = item.getItemMeta();
                                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getString("ToggleInv." + i + ".on.itemName")));
                                ArrayList<String> lore = new ArrayList<String>();
                                for (String string : config.getStringList("ToggleInv." + i + ".on.lore")) {
                                    lore.add(ChatColor.translateAlternateColorCodes('&', string));
                                }
                                meta.setLore(lore);
                                item.setItemMeta(meta);
                                inv.setItem(i - 1, item);
                            } else {
                                ItemStack item = new ItemStack(config.getInt("ToggleInv." + i + ".off.itemID"));
                                item.setDurability((short) config.getInt("ToggleInv." + i + ".off.itemData"));
                                ItemMeta meta = item.getItemMeta();
                                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getString("ToggleInv." + i + ".off.itemName")));
                                ArrayList<String> lore = new ArrayList<String>();
                                for (String string : config.getStringList("ToggleInv." + i + ".off.lore")) {
                                    lore.add(ChatColor.translateAlternateColorCodes('&', string));
                                }
                                meta.setLore(lore);
                                item.setItemMeta(meta);
                                inv.setItem(i - 1, item);
                            }
                        }
                    } catch(NullPointerException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        player.openInventory(inv);
    }

    public static HashMap<UUID, Boolean> swearfilter_toggle;
    public static HashMap<UUID, Boolean> blood_toggle;
    public static HashMap<UUID, Boolean> announce_toggle;
}
