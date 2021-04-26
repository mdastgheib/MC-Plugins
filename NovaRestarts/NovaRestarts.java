
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class NovaRestarts extends JavaPlugin {

    private static Plugin instance;

    @Override
    public void onEnable()
    {
        instance = this;

        FileConfiguration c = instance.getConfig();

        c.addDefault("Delay", 5);
        List<String> cmds = new ArrayList<String>();
        cmds.add("this is a command, i swear");
        c.addDefault("Cmds", cmds);

        c.options().copyDefaults(true);
        saveConfig();

        final List<String> cmdsToRun = c.getStringList("Cmds");

        new BukkitRunnable() {

            @Override
            public void run() {
                for(String s : cmdsToRun) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s);
                }
            }
        }.runTaskLater(this, c.getInt("Delay") * 20L);
    }

    @Override
    public void onDisable()
    {
        instance = null;
    }
}
