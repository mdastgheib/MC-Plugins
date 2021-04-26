import com.earth2me.essentials.Essentials;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.*;

public class MagicBoxPlugin extends JavaPlugin {

    private static Plugin instance;
    private static List<UUID> noPickupItems = new ArrayList<UUID>();

    public static List<String> inUse = new ArrayList<String>();

    public static List<UUID> notInUseItems = new ArrayList<UUID>();

    public static Inventory inv;

    public static List<String> chestLocations = new ArrayList<String>();

    public static Essentials e = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");

    public static HashMap<UUID, Long> cooldown = new HashMap<UUID, Long>();

    public static String msg_cooldown;
    public static String msg_nomoney;
    public static String msg_inuse;

    @Override
    public void onEnable()
    {
        instance = this;
        inv = InvGet.getInventory();

        try {
            cooldown = SLAPI.load("plugins/NovaMagicBox/cooldowns.bin");
        } catch (Exception e) {
            cooldown = new HashMap<UUID, Long>();
            e.printStackTrace();
        }

        getCommand("magicbox").setExecutor(new Commands());

        FileConfiguration c = instance.getConfig();

        c.addDefault("Cost", Long.MAX_VALUE);
        c.addDefault("Cooldown", 86400);
        List<String> cl = new ArrayList<String>();
        cl.add("world:6969:6969:6969");
        c.addDefault("Locations", cl);
        c.addDefault("Messages.NoMoney", "&cNot enough money! Need $%money%!");
        c.addDefault("Messages.Cooldown", "&cYou may not use this for %time%!");
        c.addDefault("Messages.BoxInUse", "&cThis magic box is currently in-use!");

        c.options().copyDefaults(true);
        saveConfig();

        for(String s : c.getStringList("Locations")) {
            chestLocations.add(s);

            World world = Bukkit.getWorld(s.split(":")[0]);
            String[] strings = s.split(":");
            Location loc = new Location(world, Double.parseDouble(strings[1]), Double.parseDouble(strings[2]), Double.parseDouble(strings[3]));
            loc.add(0.5D, 0D, 0.5D);

            ItemStack it = new ItemStack(Material.GOLD_NUGGET);
            ItemMeta itemMeta = it.getItemMeta();
            itemMeta.setDisplayName(new Random(System.nanoTime()) + "");
            it.setItemMeta(itemMeta);
            Item i = world.dropItem(loc.add(0,1,0), it);
            i.setVelocity(new org.bukkit.util.Vector(0D, 0D, 0D));

            notInUseItems.add(i.getUniqueId());
        }

        registerEvents(instance, new PlayerListener());

        msg_cooldown = c.getString("Messages.Cooldown");
        msg_nomoney = c.getString("Messages.NoMoney");
        msg_inuse = c.getString("Messages.BoxInUse");
    }

    public static void reloadLocations()
    {
        chestLocations.clear();
        for(String s : instance.getConfig().getStringList("Locations")) {
            chestLocations.add(s);
        }
    }

    public static void reloadConfigMsgs()
    {
        FileConfiguration c = instance.getConfig();
        msg_cooldown = c.getString("Messages.Cooldown");
        msg_nomoney = c.getString("Messages.NoMoney");
        msg_inuse = c.getString("Messages.BoxInUse");
    }

    @Override
    public void onDisable()
    {
        try {
            InvGet.saveVault(inv);
        } catch(IOException ex) {
            // WUT
        }

        try {
            SLAPI.save(cooldown, "plugins/NovaMagicBox/cooldowns.bin");
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(World world : Bukkit.getWorlds()) {
            for(Entity entity : world.getEntities()) {
                if(notInUseItems.contains(entity.getUniqueId())) {
                    entity.remove();
                }
            }
        }

        instance = null;
    }

    private void registerEvents(Plugin plugin, Listener... listeners)
    {
        PluginManager pm = Bukkit.getPluginManager();
        for(Listener listener : listeners) {
            pm.registerEvents(listener, plugin);
        }
    }

    public static Plugin getInstance()
    {
        return instance;
    }

    public static boolean noPickupItem(UUID itemUUID)
    {
        return noPickupItems.contains(itemUUID);
    }

    public static List<UUID> getNoPickupItems()
    {
        return noPickupItems;
    }

    public static String time(int seconds)
    {
        int hours = seconds / 3600;
        seconds = seconds - (hours * 3600);
        int minutes = seconds / 60;
        seconds = seconds - (minutes * 60);
        return (hours + "H " + minutes + "M " + seconds + "S");
    }
}
