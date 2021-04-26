package Nova.Haste;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Task {

    private Plugin plugin;
    private Player player = null;
    private int duration;
    private int cooldown;
    private int power;
    private int time;

    public Task(Plugin plugin, Player player, int duration, int cooldown, int power) {
        this.plugin = plugin;
        this.player = player;
        this.duration = duration;
        this.power = power;
        this.cooldown = cooldown;
    }

    public Task startTask() {
        this.time = 0;
        new BukkitRunnable() {
            @Override
            public void run() {
                if(time == 0)
                {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, (duration * 25), power));
                }

                int timeRemaining =  duration - time;
                if(timeRemaining == 0)
                {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Haste.getStaticPlugin().getConfig().getString("Nova.Messages.Expired")
                            .replace("%prefix%", Haste.getStaticPlugin().getConfig().getString("Nova.Messages.Prefix"))
                            .replace("%cooldown%", "" + cooldown)));

                    Haste.cooldown.put(player.getUniqueId(), (System.currentTimeMillis() + cooldown * 1000L));
                    Haste.inUse.remove(player.getUniqueId());
                    player.removePotionEffect(PotionEffectType.FAST_DIGGING);
                    this.cancel();
                    return;
                }
                else if(timeRemaining == 1)
                {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Haste.getStaticPlugin().getConfig().getString("Nova.Messages.TimeRemaining")
                    .replace("%prefix%", Haste.getStaticPlugin().getConfig().getString("Nova.Messages.Prefix"))
                    .replace("%time%", "" + 1)));
                }
                else if(timeRemaining == 2)
                {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Haste.getStaticPlugin().getConfig().getString("Nova.Messages.TimeRemaining")
                    .replace("%prefix%", Haste.getStaticPlugin().getConfig().getString("Nova.Messages.Prefix"))
                    .replace("%time%", "" + 2)));
                }
                else if(timeRemaining == 3)
                {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Haste.getStaticPlugin().getConfig().getString("Nova.Messages.TimeRemaining")
                    .replace("%prefix%", Haste.getStaticPlugin().getConfig().getString("Nova.Messages.Prefix"))
                    .replace("%time%", "" + 3)));
                }
                else if(timeRemaining == 5)
                {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Haste.getStaticPlugin().getConfig().getString("Nova.Messages.TimeRemaining")
                    .replace("%prefix%", Haste.getStaticPlugin().getConfig().getString("Nova.Messages.Prefix"))
                    .replace("%time%", "" + 5)));
                }
                else if(timeRemaining == 10)
                {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Haste.getStaticPlugin().getConfig().getString("Nova.Messages.TimeRemaining")
                    .replace("%prefix%", Haste.getStaticPlugin().getConfig().getString("Nova.Messages.Prefix"))
                    .replace("%time%", "" + 10)));
                }
                else if(timeRemaining == 15)
                {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Haste.getStaticPlugin().getConfig().getString("Nova.Messages.TimeRemaining")
                    .replace("%prefix%", Haste.getStaticPlugin().getConfig().getString("Nova.Messages.Prefix"))
                    .replace("%time%", "" + 15)));
                }
                else if(timeRemaining == 30)
                {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Haste.getStaticPlugin().getConfig().getString("Nova.Messages.TimeRemaining")
                    .replace("%prefix%", Haste.getStaticPlugin().getConfig().getString("Nova.Messages.Prefix"))
                    .replace("%time%", "" + 30)));
                }
                else if(timeRemaining == 45)
                {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Haste.getStaticPlugin().getConfig().getString("Nova.Messages.TimeRemaining")
                            .replace("%prefix%", Haste.getStaticPlugin().getConfig().getString("Nova.Messages.Prefix"))
                            .replace("%time%", "" + 45)));
                }
                else if(timeRemaining == 60)
                {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Haste.getStaticPlugin().getConfig().getString("Nova.Messages.TimeRemaining")
                            .replace("%prefix%", Haste.getStaticPlugin().getConfig().getString("Nova.Messages.Prefix"))
                            .replace("%time%", "" + 60)));
                }

                player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 50, power));
                time ++;
            }
        }.runTaskTimer(this.plugin, 20L, 20L);
        return this;
    }

    public static Task start(Plugin plugin, Player player, int duration, int cooldown, int power)
    {
        return new Task(plugin, player, duration, cooldown, power).startTask();
    }
}
