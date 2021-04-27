package com.novaprison.meow;

import com.novaprison.meow.util.LegitMeowUtil;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            return;
        }
        if (event.getItem() != null) {
            if (event.getItem().getTypeId() == Meow.pluginInstance.getConfig().getInt("Meow.bombitem.ID")) {
                if (1 == 1) {
                    boolean a = false;
                    List<String> r = Meow.pluginInstance.getConfig().getStringList("Meow.regions");
                    for (String s : r) {
                        if (Meow.isWithinRegion(p.getLocation().add(0, -2, 0), s)) {
                            a = true;
                            break;
                        }
                    }
                    if (!a) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', Meow.pluginInstance.getConfig().getString("Meow.prefix") + " " + Meow.pluginInstance.getConfig().getString("Meow.regionmsg")));
                        return;
                    }
                    if (Config.cooldown.containsKey(p.getUniqueId())) {
                        if (System.currentTimeMillis() - Config.cooldown.get(p.getUniqueId()) > Meow.pluginInstance.getConfig().getLong("Meow.cooldown") * 1000L) {
                            Config.cooldown.remove(p.getUniqueId());
                        } else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Meow.pluginInstance.getConfig().getString("Meow.prefix") + " " + Meow.pluginInstance.getConfig().getString("Meow.cooldownmsg").replace("%time%", "" + Meow.pluginInstance.getConfig().getLong("Meow.cooldown"))));
                            return;
                        }
                    }
                    int ix = event.getItem().getDurability();
                    if (p.getGameMode().equals(GameMode.SURVIVAL) || p.getGameMode().equals(GameMode.ADVENTURE)) {
                        if (event.getItem().getAmount() <= 1) {
                            p.getInventory().setItemInHand(new ItemStack(Material.AIR));
                        } else {
                            p.getInventory().getItemInHand().setAmount(p.getInventory().getItemInHand().getAmount() - 1);
                        }
                    }
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', Meow.pluginInstance.getConfig().getString("Meow.prefix") + " " + Meow.pluginInstance.getConfig().getString("Meow.usedmsg").replace("%cooldown%", "" + Meow.pluginInstance.getConfig().getLong("Meow.cooldown"))));
                    Config.cooldown.put(p.getUniqueId(), System.currentTimeMillis());

                    LegitMeowUtil.throwItem(p, ix > 9 ? ix / 10 : Meow.pluginInstance.getConfig().getInt("Meow.radius"));
                }
            }
        }
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        if (Config.noPickup.contains(event.getItem().getUniqueId())) event.setCancelled(true);
    }

    @EventHandler
    public void onItemDespawn(ItemDespawnEvent event) {
        if (Config.noPickup.contains(event.getEntity().getUniqueId())) event.setCancelled(true);
    }

}
