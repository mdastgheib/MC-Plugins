package Nova.Tag.Listeners;

import Nova.Tag.Tag;
import Nova.Tag.Utils.ParticleEffect;
import Nova.Tag.Utils.RandomFireworkUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public class PlayerQuitListener implements Listener {

    Tag plugin;

    public PlayerQuitListener(Tag plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent e)
    {
        Player p = e.getPlayer();

        if(this.plugin.getTaggedPlayers().containsKey(p.getName()))
        {
            if(!this.plugin.getConfig().getString("Nova.CombatLoggedMsg").equals(""))
            {
               Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("Nova.CombatLoggedMsg").replaceAll("%player%", p.getName())));
            }
            p.setHealth(0.0D);

            ParticleEffect.LAVA.display(p.getLocation(), 0.7F, 0.9F, 0.7F, 0.3F, 30);
            ParticleEffect.FLAME.display(p.getLocation(), 0.7F, 0.9F, 0.7F, 0.15F, 50);
            ParticleEffect.SMOKE.display(p.getLocation(), 0.5F, 0.7F, 0.5F, 0.1F, 100);
            ParticleEffect.displayBlockCrack(p.getLocation(), 152, (byte) 0, 0.7F, 0.7F, 0.7F, 150);
            p.getWorld().playSound(p.getLocation(), Sound.LAVA_POP, 1.0F, 1.0F);
            p.getWorld().playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1.0F, 1.0F);

            RandomFireworkUtil.LaunchRandomFirework(p.getLocation());

            List<String> loggers = this.plugin.getConfig().getStringList("Nova.loggers");
            loggers.add(p.getUniqueId().toString());
            this.plugin.getConfig().set("Nova.loggers", loggers);
            this.plugin.saveConfig();

            this.plugin.getTaggedPlayers().remove(p.getName());
        }
    }
}
