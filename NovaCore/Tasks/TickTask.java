package net.novaprison.Tasks;

import net.novaprison.Events.ServerSecondEvent;
import net.novaprison.Events.ServerTickEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TickTask {

    private Plugin plugin = null;
    private int ticks = 0;
    private long startTime = 0L;

    public TickTask (Plugin p)
    {
        this.plugin = p;
        this.ticks = 0;
    }

    public TickTask start()
    {
        this.startTime = System.currentTimeMillis();

        new BukkitRunnable() {
            @Override
            public void run() {
                ServerTickEvent serverTickEvent = new ServerTickEvent(plugin);
                Bukkit.getServer().getPluginManager().callEvent(serverTickEvent);

                if (ticks % 20 == 0) {
                    ServerSecondEvent serverSecondEvent = new ServerSecondEvent(plugin);
                    Bukkit.getServer().getPluginManager().callEvent(serverSecondEvent);
                }

                ticks++;
            }
        }.runTaskTimerAsynchronously(this.plugin, 0L, 1L);
        return this;
    }

    public static TickTask startTask(Plugin p) {
        return new TickTask(p).start();
    }
}