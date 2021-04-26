package Nova.Tag.Listeners;

import Nova.Tag.Tag;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntityListener implements Listener {

    Tag plugin;

    public EntityDamageByEntityListener(Tag plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e)
    {
        if(e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            if(!this.plugin.getTaggedPlayers().containsKey(((Player) e.getDamager()).getName())) {
                this.plugin.getTaggedPlayers().remove(((Player) e.getDamager()).getName());
                this.plugin.getTaggedPlayers().put(((Player) e.getDamager()).getName(), System.currentTimeMillis());
                ((Player) e.getDamager()).sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("Nova.Prefix") + " " + this.plugin.getConfig().getString("Nova.TaggedMsg")));
            } else {
                this.plugin.getTaggedPlayers().remove(((Player) e.getDamager()).getName());
                this.plugin.getTaggedPlayers().put(((Player) e.getDamager()).getName(), System.currentTimeMillis());
            }

            if(!this.plugin.getTaggedPlayers().containsKey(((Player) e.getEntity()).getName())) {
                this.plugin.getTaggedPlayers().remove(((Player) e.getEntity()).getName());
                this.plugin.getTaggedPlayers().put(((Player) e.getEntity()).getName(), System.currentTimeMillis());
                ((Player) e.getEntity()).sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("Nova.Prefix") + " " + this.plugin.getConfig().getString("Nova.TaggedMsg")));
            } else {
                this.plugin.getTaggedPlayers().remove(((Player) e.getEntity()).getName());
                this.plugin.getTaggedPlayers().put(((Player) e.getEntity()).getName(), System.currentTimeMillis());
            }
        } else if(e.getDamager() instanceof Projectile && e.getEntity() instanceof Player) {
            if(!this.plugin.getTaggedPlayers().containsKey(((Player) ((Projectile) e.getDamager()).getShooter()).getName())) {
                this.plugin.getTaggedPlayers().remove(((Player) ((Projectile) e.getDamager()).getShooter()).getName());
                this.plugin.getTaggedPlayers().put(((Player) ((Projectile) e.getDamager()).getShooter()).getName(), System.currentTimeMillis());
                ((Player) ((Projectile) e.getDamager()).getShooter()).sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("Nova.Prefix") + " " + this.plugin.getConfig().getString("Nova.TaggedMsg")));
            } else {
                this.plugin.getTaggedPlayers().remove(((Player) ((Projectile) e.getDamager()).getShooter()).getName());
                this.plugin.getTaggedPlayers().put(((Player) ((Projectile) e.getDamager()).getShooter()).getName(), System.currentTimeMillis());
            }

            if(!this.plugin.getTaggedPlayers().containsKey(((Player) e.getEntity()).getName())) {
                this.plugin.getTaggedPlayers().remove(((Player) e.getEntity()).getName());
                this.plugin.getTaggedPlayers().put(((Player) e.getEntity()).getName(), System.currentTimeMillis());
                ((Player) e.getEntity()).sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("Nova.Prefix") + " " + this.plugin.getConfig().getString("Nova.TaggedMsg")));
            } else {
                this.plugin.getTaggedPlayers().remove(((Player) e.getEntity()).getName());
                this.plugin.getTaggedPlayers().put(((Player) e.getEntity()).getName(), System.currentTimeMillis());
            }
        }
        if(e.getDamager() instanceof Player) {
            if(e.getEntity() instanceof Player) {
                plugin.removeUnfairness((Player) e.getDamager(), (Player) e.getEntity());
            }
        }
        else if(e.getDamager() instanceof Projectile) {
            if(e.getEntity() instanceof Player) {
                plugin.removeUnfairness((Player) ((Projectile) e.getDamager()).getShooter(), (Player) e.getEntity());
            }
        }
    }

    @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=false)
    public void onEntityDamageByEntityEventLow(EntityDamageByEntityEvent e)
    {
        if(e.getEntity() instanceof Player) {
            if(!this.plugin.getTaggedPlayers().containsKey(((Player) e.getEntity()).getName())) {
                return;
            }
        } else {
            return;
        }
        if(e.getDamager() instanceof Player) {
            if(!this.plugin.getTaggedPlayers().containsKey(((Player) e.getDamager()).getName())) {
                return;
            }
        } else if(e.getDamager() instanceof Projectile) {
            if(!this.plugin.getTaggedPlayers().containsKey(((Player) ((Projectile) e.getDamager()).getShooter()).getName())) {
                return;
            }
        } else {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=false)
    public void onEntityDamageByEntityEventHighest(EntityDamageByEntityEvent e)
    {
        if(e.getEntity() instanceof Player) {
            if(!this.plugin.getTaggedPlayers().containsKey(((Player) e.getEntity()).getName())) {
                return;
            }
        }
        if(e.getDamager() instanceof Player) {
            if(!this.plugin.getTaggedPlayers().containsKey(((Player) e.getDamager()).getName())) {
                return;
            }
        }

        if(e.getDamager() instanceof Player || e.getDamager() instanceof Projectile) {
            if(e.getEntity() instanceof Player) {
                if(e.getDamager() instanceof Player) {
                    Player dam = (Player) e.getDamager();
                    Player tar = (Player) e.getEntity();

                    if (this.plugin.getTaggedPlayers().containsKey(dam.getName())) {
                        if (this.plugin.getTaggedPlayers().containsKey(tar.getName())) {
                            e.setCancelled(false);
                        }
                    }
                    return;
                }
            }
            if(e.getEntity() instanceof Player) {
                if (((Projectile) e.getDamager()).getShooter() instanceof Player) {
                    Player dam = (Player) ((Projectile) e.getDamager()).getShooter();
                    Player tar = (Player) e.getEntity();

                    if (this.plugin.getTaggedPlayers().containsKey(dam.getName())) {
                        if (this.plugin.getTaggedPlayers().containsKey(tar.getName())) {
                            e.setCancelled(false);
                        }
                    }
                    return;
                }
            }
        }
    }
}
