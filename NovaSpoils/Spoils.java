package net.novaprison.spoils;

import nova.novaprison.InvGet;
import nova.novaprison.SLAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class Spoils extends JavaPlugin {

    public static Plugin instance;
    public static PluginManager pluginManager;

    public static HashMap<UUID, Long> cooldown;
    public static int cooldownTime;
    public static Inventory items;

    public static ItemStack clickItem;
    public static String invName;
    public static List<UUID> inSpoils = new ArrayList<UUID>();
    public static HashMap<UUID, List<ItemStack>> spoils = new HashMap<UUID, List<ItemStack>>();

    public static List<Item> spawnedItems = new ArrayList<Item>();
    public static List<UUID> antispam = new ArrayList<UUID>();

    public static List<String> hologram;

    @Override
    public void onEnable()
    {
        instance = this;
        pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new PlayerListener(), this);

        FileConfiguration c = instance.getConfig();
        List<String> h = new ArrayList<String>();
        c.addDefault("NovaSpoils.holograms", h);
        c.addDefault("NovaSpoils.cooldown", 86400);
        c.addDefault("NovaSpoils.InventoryName", "&a&lWonderful Spoils!");
        c.addDefault("NovaSpoils.minRandomItems", 3);
        c.addDefault("NovaSpoils.maxRandomItems", 5);
        c.addDefault("NovaSpoils.msg.spoils", "&e&lSpoils &7&l>>> &b&oEnjoy your spoils!");
        c.addDefault("NovaSpoils.msg.cooldown", "&e&lSpoils &7&l>>> &c&oClick to use rn!");

        c.addDefault("NovaSpoils.SpoilsItem.Type", Material.CHEST.toString().toUpperCase());
        c.addDefault("NovaSpoils.SpoilsItem.Name", ChatColor.translateAlternateColorCodes('&', "&b&l> &7&oSpoils &b&l<"));
        List<String> lorex = new ArrayList<String>();
        lorex.add(ChatColor.translateAlternateColorCodes('&', "&6&oRight click to use!"));
        c.addDefault("NovaSpoils.SpoilsItem.Lore", lorex);

        c.addDefault("NovaSpoils.Inventory.GetSpoilsItem.Type", Material.CHEST.toString().toUpperCase());
        c.addDefault("NovaSpoils.Inventory.GetSpoilsItem.Name", "&b&lClaim your spoils now!");
        List<String> lore2 = new ArrayList<String>();
        lore2.add(ChatColor.translateAlternateColorCodes('&', "&6&oclick to claim your spoils now!"));
        c.addDefault("NovaSpoils.Inventory.GetSpoilsItem.Lore", lore2);

        c.addDefault("NovaSpoils.Inventory.CooldownItem.Type", Material.TNT.toString().toUpperCase());
        c.addDefault("NovaSpoils.Inventory.Cooldown.Name", "&b&lCooldown thingie");
        List<String> lore3 = new ArrayList<String>();
        lore3.add(ChatColor.translateAlternateColorCodes('&', "&c&oYou cannot claim spoils for another %time%"));
        c.addDefault("NovaSpoils.Inventory.CooldownItem.Lore", lore3);
        c.options().copyDefaults(true);
        saveConfig();

        items = InvGet.getInventory();

        cooldownTime = c.getInt("NovaSpoils.cooldown");

        try {
            cooldown = SLAPI.load("plugins/NovaSpoils/cooldowns.bin");
        } catch (Exception e) {
            cooldown = new HashMap<UUID, Long>();
        }

        hologram = c.getStringList("NovaSpoils.holograms");

        ItemStack newclick = new ItemStack(Material.valueOf(c.getString("NovaSpoils.SpoilsItem.Type")));
        ItemMeta newmeta = newclick.getItemMeta();
        newmeta.addEnchant(Enchantment.DURABILITY, 10, true);
        newmeta.setDisplayName(c.getString("NovaSpoils.SpoilsItem.Name"));
        newmeta.setLore(c.getStringList("NovaSpoils.SpoilsItem.Lore"));
        newclick.setItemMeta(newmeta);

        clickItem = newclick.clone();
        invName = ChatColor.translateAlternateColorCodes('&', c.getString("NovaSpoils.InventoryName"));

        this.getServer().getScheduler().runTaskTimer(this, new Runnable() {
            @Override
            public void run() {
                HoloAPI.clearOldHolograms();
            }
        }, 10L, 10L);
    }

    @Override
    public void onDisable()
    {
        for(Item i : spawnedItems) {
            i.remove();
        }
        this.getServer().getScheduler().cancelTasks(this);
        HoloAPI.clearHolograms();
        try {
            SLAPI.save(cooldown, "plugins/NovaSpoils/cooldowns.bin");
            InvGet.saveItems(items);
        } catch (Exception e) {}
        instance = null;
    }

    public static Plugin getInstance()
    {
        return instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args)
    {
        if(cmd.getName().equalsIgnoreCase("spoils")) {
            if (!(cs instanceof Player)) {
                return true;
            }

            Player p = (Player) cs;

            if (!(p.hasPermission("novaranks.admin"))) {
                openInv(p);
            } else {
                if (args.length == 0) {
                    openInv(p);
                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("items")) {
                        p.openInventory(items);
                        return true;
                    } else if (args[0].equalsIgnoreCase("reload")) {
                        reloadConfig();
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aReloaded NovaPpoils config!"));
                        return true;
                    } else if(args[0].equalsIgnoreCase("saveinv") || args[0].equalsIgnoreCase("saveinventory")) {
                        try {
                            InvGet.saveItems(items);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return true;
                    } else if (args[0].equalsIgnoreCase("resetcooldown")) {
                        cooldown.remove(p.getUniqueId());
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aReset your /spoils cooldown!"));
                        return true;
                    } else {
                        usage(p);
                        return true;
                    }
                } else if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("resetcooldown")) {
                        if (args[1].startsWith("u:")) {
                            UUID uuid = UUID.fromString(args[1].replaceAll("u:", ""));
                            if (cooldown.containsKey(uuid)) {
                                cooldown.remove(uuid);
                                OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cRemoved " + player.getName() + "'s cooldown!"));
                                cooldown.remove(uuid);
                            } else {
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis UUID was not on cooldown!"));
                            }
                            return true;
                        }
                        Player px = Bukkit.getPlayer(args[1]);
                        if (px == null) {
                            OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
                            if (player != null) {
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cRemoved " + player.getName() + "'s cooldown!"));
                                cooldown.remove(player.getUniqueId());
                                return true;
                            }
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCould not find player!"));
                            return true;
                        }
                        if (!px.isOnline()) {
                            OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
                            if (player != null) {
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cRemoved " + player.getName() + "'s cooldown!"));
                                cooldown.remove(player.getUniqueId());
                                return true;
                            }
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCould not find player!"));
                            return true;
                        }
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cRemoved " + px.getName() + "'s cooldown!"));
                        cooldown.remove(px.getUniqueId());
                        return true;
                    }
                } else if (args.length == 3) {
                    if (args[0].equalsIgnoreCase("give")) {
                        try {
                            Integer i = Integer.parseInt(args[2]);
                            Player px = Bukkit.getPlayerExact(args[1]);
                            if (px != null) {
                                Player player = Bukkit.getPlayerExact(args[1]);
                                if (player != null) {
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aGave " + player.getName() + "'s " + i + " spoils!"));
                                    for(int iy = 0; iy < i; iy++) {
                                        HashMap<Integer, ItemStack> items = player.getInventory().addItem(Spoils.clickItem);
                                        if (!items.isEmpty()) {
                                            for (Integer ix : items.keySet()) {
                                                ItemStack itemStack = items.get(ix);
                                                player.getLocation().getWorld().dropItemNaturally(player.getLocation(), itemStack);
                                            }
                                        }
                                    }
                                    return true;
                                }
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCould not find player!"));
                                return true;
                            }
                        } catch(Exception ex) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + args[2] + " doesn't seem to be a number."));
                            return true;
                        }
                    }
                } else {
                    usage(p);
                    return true;
                }
            }
        }
        return false;
    }

    private static void usage(Player player)
    {
        String[] msgs = new String[]{ "&cUsage", "&c&o/spoils items", "&c&o/spoils resetcooldown [player]", "&c&o/spoils reload"};
        for(String string : msgs) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', string));
        }
    }

    public static String time(int seconds)
    {
        int hours = seconds / 3600;
        seconds = seconds - (hours * 3600);
        int minutes = seconds / 60;
        seconds = seconds - (minutes * 60);
        if(hours > 0) {
            return (hours + "H " + minutes + "M");
        } else if(minutes > 0) {
            return (minutes + "M");
        } else if(seconds > 0) {
            return (seconds + "S");
        } else if(seconds < 0) {
            return ("Some time in the past...");
        } else {
            return "now";
        }
    }

    public static void openInv(Player player)
    {
        Inventory i = Bukkit.createInventory(null, 9, invName);

        if(cooldown.containsKey(player.getUniqueId())) {
            Long time = cooldown.get(player.getUniqueId());
            Long cooldown = cooldownTime * 1000L;
            if ((time + cooldown) < System.currentTimeMillis()) {
                ItemStack item = new ItemStack(Material.valueOf(getInstance().getConfig().getString("NovaSpoils.Inventory.GetSpoilsItem.Type")));
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', getInstance().getConfig().getString("NovaSpoils.Inventory.GetSpoilsItem.Name")));
                List<String> lore = new ArrayList<String>();
                for(String s : getInstance().getConfig().getStringList("NovaSpoils.Inventory.GetSpoilsItem.Lore")) {
                    lore.add(s);
                }
                meta.setLore(lore);
                item.setItemMeta(meta);

                i.setItem(4, item);
            } else {
                int timeInSecondsLeft = Integer.parseInt("" + (time + (cooldownTime * 1000L) - System.currentTimeMillis()) / 1000);

                ItemStack item = new ItemStack(Material.valueOf(getInstance().getConfig().getString("NovaSpoils.Inventory.CooldownItem.Type")));
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', getInstance().getConfig().getString("NovaSpoils.Inventory.Cooldown.Name")).replace("%time%", time(timeInSecondsLeft)));
                List<String> lore = new ArrayList<String>();
                for(String s : getInstance().getConfig().getStringList("NovaSpoils.Inventory.CooldownItem.Lore")) {
                    lore.add(s.replace("%time%", time(timeInSecondsLeft)));
                }
                meta.setLore(lore);
                item.setItemMeta(meta);

                i.setItem(4, item);
            }
        } else {
            ItemStack item = new ItemStack(Material.valueOf(getInstance().getConfig().getString("NovaSpoils.Inventory.GetSpoilsItem.Type")));
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', getInstance().getConfig().getString("NovaSpoils.Inventory.GetSpoilsItem.Name")));
            List<String> lore = new ArrayList<String>();
            for(String s : getInstance().getConfig().getStringList("NovaSpoils.Inventory.GetSpoilsItem.Lore")) {
                lore.add(s);
            }
            meta.setLore(lore);
            item.setItemMeta(meta);

            i.setItem(4, item);
        }

        player.openInventory(i);
    }
}

