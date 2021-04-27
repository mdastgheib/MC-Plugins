package net.novaprison.spoils;

import net.novaprison.Events.ServerSecondEvent;
import net.novaprison.Utils.PacketUtils.NovaSounds;
import net.novaprison.Utils.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInvClick(InventoryClickEvent e)
    {
        if(e.isCancelled()) {
            return;
        }
        try {
            if (e.getClickedInventory().getName().equals(Spoils.invName)) {
                e.setCancelled(true);
                if (e.getCurrentItem() != null) {
                    if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', Spoils.getInstance().getConfig().getString("NovaSpoils.Inventory.GetSpoilsItem.Name")))) {
                        Spoils.cooldown.put(e.getWhoClicked().getUniqueId(), System.currentTimeMillis());
                        HashMap<Integer, ItemStack> items = Bukkit.getPlayer(e.getWhoClicked().getUniqueId()).getInventory().addItem(Spoils.clickItem);
                        if (!items.isEmpty()) {
                            for (Integer i : items.keySet()) {
                                ItemStack itemStack = items.get(i);
                                e.getWhoClicked().getLocation().getWorld().dropItemNaturally(e.getWhoClicked().getLocation(), itemStack);
                            }
                        }
                        e.getWhoClicked().closeInventory();
                    }
                }
            }
        } catch(Exception ex) {
            //ignored
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e)
    {
        if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if(e.getPlayer().getItemInHand() != null) {
                if(e.getPlayer().getItemInHand().isSimilar(Spoils.clickItem)) {
                    if(Spoils.antispam.contains(e.getPlayer().getUniqueId())) {
                        e.setCancelled(true);
                        return;
                    }
                    if(e.getPlayer().getItemInHand().getAmount() == 1) {
                        e.getPlayer().setItemInHand(null);
                    } else {
                        e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount() - 1);
                    }
                    e.setCancelled(true);
                    Spoils.antispam.add(e.getPlayer().getUniqueId());

                    Inventory i = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', "&1&lSpoils!"));
                    Random r = new Random();
                    int min = Spoils.getInstance().getConfig().getInt("NovaSpoils.minRandomItems");
                    int max = Spoils.getInstance().getConfig().getInt("NovaSpoils.maxRandomItems");
                    int amount = r.nextInt((max - min) + 1) + min;
                    if(!Spoils.spoils.containsKey(e.getPlayer().getUniqueId())) {
                        Spoils.spoils.put(e.getPlayer().getUniqueId(), new ArrayList<ItemStack>());
                    }
                    ArrayList<ItemStack> itemStackArrayList = new ArrayList<ItemStack>();
                    for(ItemStack ix : Spoils.items.getContents()) {
                        if(ix != null) {
                            itemStackArrayList.add(ix);
                        }
                    }
                    for(int ix = 0; ix < amount; ix++) {
                        int rn = r.nextInt(itemStackArrayList.size() - 1);
                        i.addItem(itemStackArrayList.get(rn));
                        Spoils.spoils.get(e.getPlayer().getUniqueId()).add(itemStackArrayList.get(rn));
                    }
                    Spoils.inSpoils.add(e.getPlayer().getUniqueId());
                    e.getPlayer().openInventory(i);
                }
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e)
    {
        if(Spoils.inSpoils.contains(e.getPlayer().getUniqueId())) {
            if (e.getInventory().getName().equals(ChatColor.translateAlternateColorCodes('&', "&1&lSpoils!"))) {
                for (ItemStack i : Spoils.spoils.get(e.getPlayer().getUniqueId())) {
                    for(int x = 0; x < 10; x++) {
                        ItemStack c = i.clone();
                        ItemMeta m = c.getItemMeta();
                        m.setDisplayName("" + x * 3787);
                        c.setItemMeta(m);
                        Item item = e.getPlayer().getWorld().dropItemNaturally(e.getPlayer().getLocation(), c);
                        item.setPickupDelay(Integer.MAX_VALUE);
                        Spoils.spawnedItems.add(item);
                    }
                }
                Spoils.spoils.remove(e.getPlayer().getUniqueId());

                ArrayList<ItemStack> itemStackArrayList = new ArrayList<ItemStack>();
                for(ItemStack ix : e.getInventory().getContents()) {
                    if(ix != null) {
                        itemStackArrayList.add(ix);
                    }
                }
                for(ItemStack i : itemStackArrayList) {
                    HashMap<Integer, ItemStack> hi = e.getPlayer().getInventory().addItem(i);
                    if(!hi.isEmpty()) {
                        for(Integer in : hi.keySet()) {
                            e.getPlayer().getWorld().dropItemNaturally(e.getPlayer().getLocation(), hi.get(in));
                        }
                    }
                }
                Spoils.inSpoils.remove(e.getPlayer().getUniqueId());
                Spoils.antispam.remove(e.getPlayer().getUniqueId());
                NovaSounds.sendSound(Bukkit.getPlayer(e.getPlayer().getUniqueId()), "warning.spoils");
                RandomFireworkUtil.LaunchRandomFirework(e.getPlayer().getLocation());
                List<String> x = new ArrayList<String>();
                for(String s : Spoils.hologram) {
                    x.add(s.replace("%player%", e.getPlayer().getName()));
                }
                for(Player players : e.getPlayer().getWorld().getPlayers()) {
                    HoloAPI.createHologram(players, e.getPlayer().getLocation().add(0,2,0), x);
                }
                ParticleEffect effect = new ParticleEffect(ParticleEffect.ParticleType.VILLAGER_HAPPY, 0.05, 30, 2D);
                effect.sendToLocation(e.getPlayer().getLocation());
                ParticleEffect effect2 = new ParticleEffect(ParticleEffect.ParticleType.LAVA, 0.05, 10, 2D);
                effect2.sendToLocation(e.getPlayer().getLocation());
                ParticleEffect effect3 = new ParticleEffect(ParticleEffect.ParticleType.PORTAL, 0.05, 30, 2D);
                effect3.sendToLocation(e.getPlayer().getLocation());
                ParticleEffect effect4 = new ParticleEffect(ParticleEffect.ParticleType.FLAME, 0.05, 30, 2D);
                effect4.sendToLocation(e.getPlayer().getLocation());
                e.getPlayer().getLocation().getWorld().playSound(e.getPlayer().getLocation(), Sound.NOTE_PLING, 1.5F, 1.5F);
            }
        }
    }

    @EventHandler
    public void onServerSecond(ServerSecondEvent e)
    {
        List<Item> itemsToRemove = new ArrayList<Item>();
        for(Item i : Spoils.spawnedItems) {
            if(!i.isDead()) {
                if(i.getTicksLived() > 60) {
                    i.remove();
                    itemsToRemove.add(i);
                }
            }
        }
        for(Item i : itemsToRemove) {
            if(i.getTicksLived() > 40) {
                Spoils.spawnedItems.remove(i);
            }
        }
    }

    @EventHandler
    public void onItemPickupInv(InventoryPickupItemEvent e)
    {
        Item i = e.getItem();
        if(Spoils.spawnedItems.contains(i)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerPickup(PlayerPickupItemEvent e)
    {
        Item i = e.getItem();
        if(Spoils.spawnedItems.contains(i)) {
            e.setCancelled(true);
        }
    }
}
