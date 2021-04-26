package net.novaprison.Modules.PumpkinHelmets;

import net.minecraft.server.v1_8_R1.*;
import net.novaprison.Core;
import net.novaprison.Events.ServerSecondEvent;
import net.novaprison.Modules.SettingsManager;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class PumpkinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e)
    {
        final Player player = e.getPlayer();
        new BukkitRunnable() {
            @Override
                public void run() {
                try {
                    if (player.getEquipment().getHelmet().getType() != Material.PUMPKIN)
                        player.getEquipment().setHelmet(PumpkinManager.helmet);
                } catch (NullPointerException ex) {
                    player.getEquipment().setHelmet(PumpkinManager.helmet);
                }
            }
        }.runTaskLater(Core.getInstance(), 5l);
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent e)
    {
        if(e.getInventory().getType().equals(InventoryType.CHEST)) {
            List<ItemStack> itemsToRemove = new ArrayList<ItemStack>();
            for(ItemStack i : e.getInventory().getContents()) {
                if(i != null) {
                    if (i.isSimilar(PumpkinManager.helmet)) {
                        itemsToRemove.add(i);
                    }
                }
            }
            for(ItemStack i : itemsToRemove) {
                e.getInventory().remove(i);
            }
        }
        if(e.getInventory().getType().equals(InventoryType.ENDER_CHEST)) {
            List<ItemStack> itemsToRemove = new ArrayList<ItemStack>();
            for(ItemStack i : e.getInventory().getContents()) {
                if(i != null) {
                    if (i.isSimilar(PumpkinManager.helmet)) {
                        itemsToRemove.add(i);
                    }
                }
            }
            for(ItemStack i : itemsToRemove) {
                e.getInventory().remove(i);
            }
        }
        if(e.getInventory().getType().equals(InventoryType.HOPPER)) {
            List<ItemStack> itemsToRemove = new ArrayList<ItemStack>();
            for(ItemStack i : e.getInventory().getContents()) {
                if(i != null) {
                    if (i.isSimilar(PumpkinManager.helmet)) {
                        itemsToRemove.add(i);
                    }
                }
            }
            for(ItemStack i : itemsToRemove) {
                e.getInventory().remove(i);
            }
        }
        if(e.getInventory().getType().equals(InventoryType.FURNACE)) {
            List<ItemStack> itemsToRemove = new ArrayList<ItemStack>();
            for(ItemStack i : e.getInventory().getContents()) {
                if(i != null) {
                    if (i.isSimilar(PumpkinManager.helmet)) {
                        itemsToRemove.add(i);
                    }
                }
            }
            for(ItemStack i : itemsToRemove) {
                e.getInventory().remove(i);
            }
        }
    }

    @EventHandler
    public void onScroll(PlayerItemHeldEvent e)
    {
        if(e.getPlayer().getGameMode() != GameMode.CREATIVE) {
            if (e.getPlayer().getItemInHand().isSimilar(PumpkinManager.helmet)) {
                e.getPlayer().setItemInHand(null);
            }
        }
    }

    @EventHandler
    public void onDrop(ItemSpawnEvent e)
    {
        if(e.getEntity().getItemStack().isSimilar(PumpkinManager.helmet)) {
            e.getEntity().remove();
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e)
    {
        if(e.getItem().getItemStack().isSimilar(PumpkinManager.helmet)) {
            e.getItem().remove();
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e)
    {
        Player player = e.getPlayer();
        player.getEquipment().setHelmet(PumpkinManager.helmet);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlace(BlockPlaceEvent e)
    {
        Player player = e.getPlayer();
        if(player.getGameMode() != GameMode.CREATIVE) {
            if (player.getItemInHand().isSimilar(PumpkinManager.helmet)) {
                player.setItemInHand(null);
                player.updateInventory();
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e)
    {
        if (e.getItemDrop().getItemStack().isSimilar(PumpkinManager.helmet)) {
            e.getItemDrop().remove();
            e.getPlayer().getEquipment().setHelmet(PumpkinManager.helmet);
            e.getPlayer().updateInventory();
        }
    }

    @EventHandler
    public void onSecond(ServerSecondEvent e)
    {
        PumpkinManager.counter++;
        if(PumpkinManager.counter == 2) {
            boolean color = false;
            if(PumpkinManager.color == 1) {
                PumpkinManager.color = Integer.valueOf(0);
                color = true;
            } else {
                PumpkinManager.color = 1;
            }
            PumpkinManager.counter = 0;
            for(World world : Bukkit.getWorlds()) {
                for(Player player : world.getPlayers()) {
                    if(player.getGameMode() != GameMode.CREATIVE) {
                        boolean dmg = true;
                        if(player.getEquipment().getHelmet() != null) {
                            if(player.getEquipment().getHelmet().getType().equals(Material.PUMPKIN)) {
                                dmg = false;
                            }
                        }

                        if (dmg) {
                            if(PumpkinManager.players.containsKey(player.getUniqueId())) {
                                int i = PumpkinManager.players.get(player.getUniqueId());
                                PumpkinManager.players.remove(player.getUniqueId());
                                PumpkinManager.players.put(player.getUniqueId(), i - 1);
                            } else {
                                PumpkinManager.players.put(player.getUniqueId(), 0);
                            }
                            player.removePotionEffect(PotionEffectType.WITHER);
                            player.removePotionEffect(PotionEffectType.POISON);
                            player.removePotionEffect(PotionEffectType.BLINDNESS);
                            player.removePotionEffect(PotionEffectType.SLOW);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 4, 0));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20 * 4, 0));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20 * 4, 0));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 4, 0));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 4, 1));
                            if(PumpkinManager.suffocating.contains(player.getUniqueId())) {
                                    String s1 = ChatColor.translateAlternateColorCodes('&', SettingsManager.suffocating_message_1);
                                    String s2 = ChatColor.translateAlternateColorCodes('&', SettingsManager.suffocating_message_1subtitle);
                                    if(color) {
                                        s1 = ChatColor.translateAlternateColorCodes('&', SettingsManager.suffocating_message_2);
                                        s2 = ChatColor.translateAlternateColorCodes('&', SettingsManager.suffocating_message_2subtitle);
                                    }
                                    IChatBaseComponent icbc1 = ChatSerializer.a("{\"text\": \"" + s1 + "\"}");
                                    IChatBaseComponent icbc2 = ChatSerializer.a("{\"text\": \"" + s2 + "\"}");

                                    PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
                                    PacketPlayOutTitle length = new PacketPlayOutTitle(10, 50, 5);
                                    PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE, icbc1);
                                    PacketPlayOutTitle subtitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, icbc2);
                                    connection.sendPacket(length);
                                    connection.sendPacket(title);
                                    connection.sendPacket(subtitle);
                                } else {
                                PumpkinManager.suffocating.add(player.getUniqueId());

                                String s1 = ChatColor.translateAlternateColorCodes('&', SettingsManager.beginSuffocating);
                                String s2 = ChatColor.translateAlternateColorCodes('&', SettingsManager.beginSuffocatingSubtitle);
                                IChatBaseComponent icbc1 = ChatSerializer.a("{\"text\": \"" + s1 + "\"}");
                                IChatBaseComponent icbc2 = ChatSerializer.a("{\"text\": \"" + s2 + "\"}");

                                PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
                                PacketPlayOutTitle length = new PacketPlayOutTitle(10, 50, 5);
                                PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE, icbc1);
                                PacketPlayOutTitle subtitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, icbc2);
                                connection.sendPacket(length);
                                connection.sendPacket(title);
                                connection.sendPacket(subtitle);
                            }
                            if(PumpkinManager.players.get(player.getUniqueId()) == 0) {
                                PumpkinManager.players.remove(player.getUniqueId());
                                PumpkinManager.players.put(player.getUniqueId(), 3);
                                PacketPlayOutNamedSoundEffect packetPlayOutNamedSoundEffect = new PacketPlayOutNamedSoundEffect("warning.oxygen",
                                        player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 1.0F, 1.0F);
                                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutNamedSoundEffect);
                            }
                        } else {
                            PumpkinManager.suffocating.remove(player.getUniqueId());
                            PumpkinManager.players.remove(player.getUniqueId());
                        }
                    } else {
                        PumpkinManager.suffocating.remove(player.getUniqueId());
                        PumpkinManager.players.remove(player.getUniqueId());
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent e)
    {
        Player player = e.getEntity();
        if(PumpkinManager.suffocating.contains(player.getUniqueId())) {
            PumpkinManager.suffocating.remove(player.getUniqueId());
        }
        List<ItemStack> itemsToRemove = new ArrayList<ItemStack>();
        for(ItemStack i : e.getDrops()) {
            if(i.getType().equals(Material.PUMPKIN)) {
                itemsToRemove.add(i);
            }
        }
        for(ItemStack i : itemsToRemove) {
            e.getDrops().remove(i);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e)
    {
        Player player = e.getPlayer();
        if(PumpkinManager.suffocating.contains(player.getUniqueId())) {
            PumpkinManager.suffocating.remove(player.getUniqueId());
        }
    }

    @EventHandler
    public void onKick(PlayerKickEvent e)
    {
        Player player = e.getPlayer();
        if(PumpkinManager.suffocating.contains(player.getUniqueId())) {
            PumpkinManager.suffocating.remove(player.getUniqueId());
        }
    }
}
