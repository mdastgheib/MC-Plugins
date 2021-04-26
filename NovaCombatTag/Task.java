package Nova.Tag;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Task {

    public Task start()
    {
        new BukkitRunnable() {
            @Override
            public void run() {
                List<String> toremove = new ArrayList<String>();
                for(String s : Tag.getTaggedPlayers().keySet()) {
                    if((System.currentTimeMillis() - Tag.getTaggedPlayers().get(s)) > Tag.time * 1000L) {
                        Player p = Bukkit.getPlayerExact(s);

                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', Tag.getInstance().getConfig().getString("Nova.Prefix") + " " + Tag.getInstance().getConfig().getString("Nova.UntaggedMsg")));
                        toremove.add(s);
                    }
                }

                for(String s : toremove) {
                    Tag.getTaggedPlayers().remove(s);
                }
            }
        }.runTaskTimer(Tag.getInstance(), 0L, 2L);
        return this;
    }

    public static Task startTask()
    {
        return new Task().start();
    }
}
