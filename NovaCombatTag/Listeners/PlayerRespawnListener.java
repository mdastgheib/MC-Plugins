package Nova.Tag.Listeners;

import Nova.Tag.Tag;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class PlayerRespawnListener implements Listener {

    Tag plugin;

    public PlayerRespawnListener(Tag plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e)
    {
        Player p = e.getPlayer();
        FileConfiguration config = Tag.getInstance().getConfig();

        List<String> loggers = config.getStringList("Nova.loggers");
        if(loggers.contains(p.getUniqueId().toString())) {
            p.teleport(new Location(Bukkit.getWorld(config.getString("Nova.punish.world")), config.getInt("Nova.punish.x"), config.getInt("Nova.punish.y"), config.getInt("Nova.punish.z")));
            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 50, 5));
            p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 50, 5));
            p.playSound(p.getLocation(), Sound.GHAST_SCREAM, 1.0F, 1.0F);
            for(String s : config.getStringList("Nova.onAfterCombatLogRejoinMessages")) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
            }
        }
        loggers.remove(p.getUniqueId().toString());
        config.set("Nova.loggers", loggers);
        this.plugin.saveConfig();
    }
}
