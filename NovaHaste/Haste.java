package Nova.Haste;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Haste extends JavaPlugin {

    private Plugin instance;
    private static Plugin sinstance;
    public static HashMap<UUID, Long> cooldown = new HashMap<UUID, Long>();
    public static ArrayList<UUID> inUse = new ArrayList<UUID>();

    public static HashMap<UUID, Boolean> toggle = new HashMap<UUID, Boolean>();

    @Override
    public void onEnable()
    {
        instance = this;
        sinstance = this;

        Config.setupConfig();

        getCommand("novahaste").setExecutor(new Command());

        registerEvents(this, new PlayerListener());
    }

    @Override
    public void onDisable()
    {
        sinstance = null;
        cooldown.clear();
        inUse.clear();
        toggle.clear();
    }

    private void registerEvents(Plugin plugin, Listener... listeners)
    {
        PluginManager pm = Bukkit.getPluginManager();
        for(Listener listener : listeners)
        {
            pm.registerEvents(listener, plugin);
        }
    }

    public Plugin getPlugin()
    {
        return instance;
    }

    public static Plugin getStaticPlugin()
    {
        return sinstance;
    }

    public HashMap<UUID, Long> getCooldown()
    {
        return cooldown;
    }

    public ArrayList<UUID> getInUse()
    {
        return inUse;
    }
}
