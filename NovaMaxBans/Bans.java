import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Bans extends JavaPlugin {

    private static Plugin instance;

    @Override
    public void onEnable()
    {
        instance = this;

        registerEvents(instance, new PlayerListener());

        FileConfiguration c = instance.getConfig();
    }

    @Override
    public void onDisable()
    {
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
}
