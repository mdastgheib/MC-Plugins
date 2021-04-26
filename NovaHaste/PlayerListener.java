package Nova.Haste;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class PlayerListener implements Listener {

    private Haste haste;

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void playerInteract(PlayerInteractEvent e)
    {
        Player p = e.getPlayer();
        Action a = e.getAction();

        if(Haste.toggle.get(p.getUniqueId()) == false) {
            return;
        }

        if(a.equals(Action.RIGHT_CLICK_BLOCK)) {
            if(e.getClickedBlock().getType().equals(Material.CHEST)) {
                return;
            } else if(e.getClickedBlock().getType().equals(Material.TRAPPED_CHEST)) {
                return;
            } else if(e.getClickedBlock().getType().equals(Material.ENDER_CHEST)) {
                return;
            } else if(e.getClickedBlock().getType().equals(Material.WOODEN_DOOR)) {
                return;
            } else if(e.getClickedBlock().getType().equals(Material.WOOD_DOOR)) {
                return;
            } else if(e.getClickedBlock().getType().equals(Material.STONE_BUTTON)) {
                return;
            } else if(e.getClickedBlock().getType().equals(Material.WOOD_BUTTON)) {
                return;
            } else if(e.getClickedBlock().getType().equals(Material.LEVER)) {
                return;
            }
        }

        if(a.equals(Action.RIGHT_CLICK_AIR) || a.equals(Action.RIGHT_CLICK_BLOCK))
        {
            List<String> hastetools = Haste.getStaticPlugin().getConfig().getStringList("Nova.HasteTools");
            if(hastetools.contains(p.getItemInHand().getType().toString()))
            {
                List<String> worlds = Haste.getStaticPlugin().getConfig().getStringList("Nova.Worlds");
                if(!worlds.contains(p.getWorld().getName()))
                {
                    return;
                }

                if(Haste.inUse.contains(p.getUniqueId())) return;

                if(Haste.cooldown.containsKey(p.getUniqueId()))
                {
                    if(Haste.cooldown.get(p.getUniqueId()) > System.currentTimeMillis())
                    {
                        int remaining = (int) (Haste.cooldown.get(p.getUniqueId()) - System.currentTimeMillis()) / 1000;

                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', Haste.getStaticPlugin().getConfig().getString("Nova.Messages.StillInCooldown")
                        .replace("%prefix%", Haste.getStaticPlugin().getConfig().getString("Nova.Messages.Prefix"))
                        .replace("%time%","" + remaining)));
                    }
                    else
                    {
                        Haste.cooldown.remove(p.getUniqueId());


                        int d = Haste.getStaticPlugin().getConfig().getInt("Nova.MaxDuration");
                        int dur = 0;
                        for (int x = d; x < (d + 1) && x > 0; x--)
                        {
                            if(p.hasPermission("novahaste.duration." + x + ""))
                            {
                                dur = x;
                                break;
                            }
                        }

                        int c = Haste.getStaticPlugin().getConfig().getInt("Nova.MaxCooldown");
                        int cool = 0;
                        for (int x = c; x < (c + 1) && x > 0; x--)
                        {
                            if(p.hasPermission("novahaste.cooldown." + x + ""))
                            {
                                cool = x;
                                break;
                            }
                        }

                        int po = Haste.getStaticPlugin().getConfig().getInt("Nova.MaxPower");
                        int pow = 0;
                        for (int x = po; x < (po + 1) && x > 0; x--)
                        {
                            if(p.hasPermission("novahaste.power." + x + ""))
                            {
                                pow = x;
                                break;
                            }
                        }

                        if(dur != 0 && cool != 0 && pow != 0)
                        {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Haste.getStaticPlugin().getConfig().getString("Nova.Messages.Activated")
                                    .replace("%prefix%", Haste.getStaticPlugin().getConfig().getString("Nova.Messages.Prefix"))
                                    .replace("%power%", "" + pow)
                                    .replace("%duration%", "" + dur)
                                    .replace("%cooldown%", "" + cool)));

                            Haste.inUse.add(p.getUniqueId());
                            p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, (dur * 25), pow));
                            Task.start(Haste.getStaticPlugin(), p, dur, cool, pow);
                        }
                    }
                }
                else
                {
                    int d = Haste.getStaticPlugin().getConfig().getInt("Nova.MaxDuration");
                    int dur = 0;
                    for (int x = d; x < (d + 1) && x > 0; x--)
                    {
                        if(p.hasPermission("novahaste.duration." + x + ""))
                        {
                            dur = x;
                            break;
                        }
                    }

                    int c = Haste.getStaticPlugin().getConfig().getInt("Nova.MaxCooldown");
                    int cool = 0;
                    for (int x = c; x < (c + 1) && x > 0; x--)
                    {
                        if(p.hasPermission("novahaste.cooldown." + x + ""))
                        {
                            cool = x;
                            break;
                        }
                    }

                    int po = Haste.getStaticPlugin().getConfig().getInt("Nova.MaxPower");
                    int pow = 0;
                    for (int x = po; x < (po + 1) && x > 0; x--)
                    {
                        if(p.hasPermission("novahaste.power." + x + ""))
                        {
                            pow = x;
                            break;
                        }
                    }

                    if(dur != 0 && cool != 0 && pow != 0)
                    {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', Haste.getStaticPlugin().getConfig().getString("Nova.Messages.Activated")
                            .replace("%prefix%", Haste.getStaticPlugin().getConfig().getString("Nova.Messages.Prefix"))
                            .replace("%power%", "" + pow)
                            .replace("%duration%", "" + dur)
                            .replace("%cooldown%", "" + cool)));

                        Haste.inUse.add(p.getUniqueId());
                        p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, (dur * 25), pow));
                        Task.start(Haste.getStaticPlugin(), p, dur, cool, pow);
                    }
                }
            }
        }
    }

    @EventHandler
    public void join(PlayerJoinEvent e)
    {
        Player p = e.getPlayer();
        if(Haste.getStaticPlugin().getConfig().getStringList("Players").contains(p.getUniqueId().toString())) {
            Haste.toggle.put(p.getUniqueId(), Haste.getStaticPlugin().getConfig().getBoolean("Player." + p.getUniqueId().toString()));
            Haste.getStaticPlugin().saveConfig();
        } else {
            Haste.getStaticPlugin().getConfig().getStringList("Players").add(p.getUniqueId().toString());
            Haste.getStaticPlugin().getConfig().set("Player." + p.getUniqueId().toString(), true);
            Haste.getStaticPlugin().saveConfig();

            Haste.toggle.put(p.getUniqueId(), Haste.getStaticPlugin().getConfig().getBoolean("Player." + p.getUniqueId().toString()));
        }
    }

    @EventHandler
    public void quit(PlayerQuitEvent e)
    {
        Player p = e.getPlayer();
        if(Haste.toggle.containsKey(p.getUniqueId())) {
            Haste.toggle.remove(p.getUniqueId());
        }
    }

    @EventHandler
    public void kick(PlayerKickEvent e)
    {
        Player p = e.getPlayer();
        if(Haste.toggle.containsKey(p.getUniqueId())) {
            Haste.toggle.remove(p.getUniqueId());
        }
    }
}
