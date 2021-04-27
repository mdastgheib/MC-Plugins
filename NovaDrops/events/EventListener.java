package me.nova.novadrops.events;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import me.nova.novadrops.NovaDrops;
import me.nova.novadrops.util.HoloAPI;
import me.nova.novadrops.util.LocationUtil;
import me.nova.novadrops.util.MessageUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;

import java.util.Random;

public class EventListener implements Listener {
    private Random random = new Random();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!NovaDrops.playerDrops.contains(event.getPlayer().getUniqueId()))
            NovaDrops.playerDrops.add(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        MessageUtil.clearMessage(event.getPlayer());
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.LOW)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.isCancelled()) {
            int amount = 1;
            ItemStack itemInHand = event.getPlayer().getInventory().getItemInHand() != null ? event.getPlayer().getInventory().getItemInHand() : new ItemStack(Material.AIR);
            if (itemInHand != null && itemInHand.getType().toString().contains("AXE")) {
                Player player = event.getPlayer();
                Block destroyedBlock = event.getBlock();
                int damage = 1;
                int unbreak = itemInHand.getEnchantmentLevel(Enchantment.DURABILITY);
                if (unbreak > 1) {
                    Random ran = new Random();
                    int chance = ran.nextInt(100);
                    int percentage = (100 / (unbreak + 1));
                    if (percentage < 25) {
                        percentage = 30;
                    }
                    if (percentage > chance) {
                        damage = 0;
                    } else {
                        damage = 1;
                    }
                }
                if (itemInHand.getDurability() >= itemInHand.getType().getMaxDurability()) {
                    event.getPlayer().getInventory().setItemInHand(new ItemStack(Material.AIR));
                    event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ITEM_BREAK, 1.0f, 1.0f);
                }
                boolean hasSilktouch = itemInHand.containsEnchantment(Enchantment.SILK_TOUCH);
                if (event.getBlock().getType() == Material.IRON_ORE) {
                    if (NovaDrops.playerDrops.contains(event.getPlayer().getUniqueId()) && (NovaDrops.playerSmelt.contains(event.getPlayer().getUniqueId()))) {
                        if (!(canBreak(event.getBlock().getLocation(), event.getPlayer()))) return;
                        if (player.getInventory().firstEmpty() == -1) {
                            int is = 0;
                            ItemStack item = new ItemStack(hasSilktouch ? Material.IRON_ORE : Material.IRON_INGOT, amount);
                            for (ItemStack o : player.getInventory().getContents()) {
                                is++;
                                if (o == null) continue;
                                int l = o.getAmount();
                                if (o.getType().equals(item.getType()) && o.getAmount() < item.getMaxStackSize()) {
                                    player.getInventory().addItem(new ItemStack(hasSilktouch ? Material.IRON_ORE : Material.IRON_INGOT, this.getAmount(player, amount)));
                                    event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                    break;
                                }
                                if (is == 36 && l != 1 + 1) {
                                    if (MessageUtil.canSendMessage(player))
                                        HoloAPI.createHologram(player, LocationUtil.getLocationInfront(player), NovaDrops.getInstance().getConfig().getString("messages.hologram"));
                                    MessageUtil.sendMessage(player, NovaDrops.fullInv);

                                    destroyedBlock.getLocation().getWorld().dropItemNaturally(destroyedBlock.getLocation(), new ItemStack(hasSilktouch ? Material.IRON_ORE : Material.IRON_INGOT, amount));
                                    destroyedBlock.getWorld().getBlockAt(destroyedBlock.getLocation()).setType(Material.AIR);
                                    event.getBlock().getWorld().playEffect(event.getBlock().getLocation(), Effect.MOBSPAWNER_FLAMES, 30);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                }
                            }
                        } else {
                            player.getInventory().addItem(new ItemStack(hasSilktouch ? Material.IRON_ORE : Material.IRON_INGOT, this.getAmount(player, amount)));
                            event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                            event.getBlock().getWorld().playEffect(event.getBlock().getLocation(), Effect.MOBSPAWNER_FLAMES, 30);
                            itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                            event.getPlayer().setItemInHand(itemInHand);
                        }
                    } else if (NovaDrops.playerDrops.contains(event.getPlayer().getUniqueId())) {
                        if (!(canBreak(event.getBlock().getLocation(), event.getPlayer()))) return;
                        if (player.getInventory().firstEmpty() == -1) {
                            int is = 0;
                            ItemStack item = new ItemStack(Material.IRON_ORE, amount);
                            for (ItemStack o : player.getInventory().getContents()) {
                                is++;
                                if (o == null) continue;
                                int l = o.getAmount();
                                if (o.getType().equals(item.getType()) && o.getAmount() < item.getMaxStackSize()) {
                                    player.getInventory().addItem(new ItemStack(Material.IRON_ORE, this.getAmount(player, amount)));
                                    event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                    break;
                                }
                                if (is == 36 && l != 1 + 1) {
                                    if (MessageUtil.canSendMessage(player))
                                        HoloAPI.createHologram(player, LocationUtil.getLocationInfront(player), NovaDrops.getInstance().getConfig().getString("messages.hologram"));
                                    MessageUtil.sendMessage(player, NovaDrops.fullInv);

                                    destroyedBlock.getLocation().getWorld().dropItemNaturally(destroyedBlock.getLocation(), new ItemStack(Material.IRON_ORE, amount));
                                    destroyedBlock.getWorld().getBlockAt(destroyedBlock.getLocation()).setType(Material.AIR);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                }
                            }
                        } else {
                            player.getInventory().addItem(new ItemStack(Material.IRON_ORE, this.getAmount(player, amount)));
                            event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                            itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                            event.getPlayer().setItemInHand(itemInHand);
                        }
                    } else if (NovaDrops.playerSmelt.contains(event.getPlayer().getUniqueId())) {
                        if (!(canBreak(event.getBlock().getLocation(), event.getPlayer()))) return;
                        destroyedBlock.getLocation().getWorld().dropItemNaturally(destroyedBlock.getLocation(), new ItemStack(hasSilktouch ? Material.IRON_ORE : Material.IRON_INGOT, amount));
                        destroyedBlock.getWorld().getBlockAt(destroyedBlock.getLocation()).setType(Material.AIR);
                        event.getBlock().getWorld().playEffect(event.getBlock().getLocation(), Effect.MOBSPAWNER_FLAMES, 30);
                        itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                        event.getPlayer().setItemInHand(itemInHand);
                    }
                } else if (event.getBlock().getType() == Material.GOLD_ORE) {
                    if (NovaDrops.playerDrops.contains(event.getPlayer().getUniqueId()) && (NovaDrops.playerSmelt.contains(event.getPlayer().getUniqueId()))) {
                        if (!(canBreak(event.getBlock().getLocation(), event.getPlayer()))) return;
                        if (player.getInventory().firstEmpty() == -1) {
                            int is = 0;
                            ItemStack item = new ItemStack(hasSilktouch ? Material.GOLD_ORE : Material.GOLD_INGOT, amount);
                            for (ItemStack o : player.getInventory().getContents()) {
                                is++;
                                if (o == null) continue;
                                int l = o.getAmount();
                                if (o.getType().equals(item.getType()) && o.getAmount() < item.getMaxStackSize()) {
                                    player.getInventory().addItem(new ItemStack(hasSilktouch ? Material.GOLD_ORE : Material.GOLD_INGOT, this.getAmount(player, amount)));
                                    event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                    break;
                                }
                                if (is == 36 && l != 1 + 1) {
                                    if (MessageUtil.canSendMessage(player))
                                        HoloAPI.createHologram(player, LocationUtil.getLocationInfront(player), NovaDrops.getInstance().getConfig().getString("messages.hologram"));
                                    MessageUtil.sendMessage(player, NovaDrops.fullInv);

                                    destroyedBlock.getLocation().getWorld().dropItemNaturally(destroyedBlock.getLocation(), new ItemStack(hasSilktouch ? Material.GOLD_ORE : Material.GOLD_INGOT, amount));
                                    destroyedBlock.getWorld().getBlockAt(destroyedBlock.getLocation()).setType(Material.AIR);
                                    event.getBlock().getWorld().playEffect(event.getBlock().getLocation(), Effect.MOBSPAWNER_FLAMES, 30);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                }
                            }
                        } else {
                            player.getInventory().addItem(new ItemStack(hasSilktouch ? Material.GOLD_ORE : Material.GOLD_INGOT, this.getAmount(player, amount)));
                            event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                            event.getBlock().getWorld().playEffect(event.getBlock().getLocation(), Effect.MOBSPAWNER_FLAMES, 30);
                            itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                            event.getPlayer().setItemInHand(itemInHand);
                        }
                    } else if (NovaDrops.playerDrops.contains(event.getPlayer().getUniqueId())) {
                        if (!(canBreak(event.getBlock().getLocation(), event.getPlayer()))) return;
                        if (player.getInventory().firstEmpty() == -1) {
                            int is = 0;
                            ItemStack item = new ItemStack(Material.GOLD_ORE, amount);
                            for (ItemStack o : player.getInventory().getContents()) {
                                is++;
                                if (o == null) continue;
                                int l = o.getAmount();
                                if (o.getType().equals(item.getType()) && o.getAmount() < item.getMaxStackSize()) {
                                    player.getInventory().addItem(new ItemStack(Material.GOLD_ORE, this.getAmount(player, amount)));
                                    event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                    break;
                                }
                                if (is == 36 && l != 1 + 1) {
                                    if (MessageUtil.canSendMessage(player))
                                        HoloAPI.createHologram(player, LocationUtil.getLocationInfront(player), NovaDrops.getInstance().getConfig().getString("messages.hologram"));
                                    MessageUtil.sendMessage(player, NovaDrops.fullInv);

                                    destroyedBlock.getLocation().getWorld().dropItemNaturally(destroyedBlock.getLocation(), new ItemStack(Material.GOLD_ORE, amount));
                                    destroyedBlock.getWorld().getBlockAt(destroyedBlock.getLocation()).setType(Material.AIR);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                }
                            }
                        } else {
                            player.getInventory().addItem(new ItemStack(Material.GOLD_ORE, this.getAmount(player, amount)));
                            event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                            itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                            event.getPlayer().setItemInHand(itemInHand);
                        }
                    } else if (NovaDrops.playerSmelt.contains(event.getPlayer().getUniqueId())) {
                        if (!(canBreak(event.getBlock().getLocation(), event.getPlayer()))) return;
                        destroyedBlock.getLocation().getWorld().dropItemNaturally(destroyedBlock.getLocation(), new ItemStack(hasSilktouch ? Material.GOLD_ORE : Material.GOLD_INGOT, amount));
                        destroyedBlock.getWorld().getBlockAt(destroyedBlock.getLocation()).setType(Material.AIR);
                        event.getBlock().getWorld().playEffect(event.getBlock().getLocation(), Effect.MOBSPAWNER_FLAMES, 30);
                        itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                        event.getPlayer().setItemInHand(itemInHand);
                    }
                } else if (event.getBlock().getType() == Material.COBBLESTONE) {
                    if (NovaDrops.playerDrops.contains(event.getPlayer().getUniqueId()) && (NovaDrops.playerSmelt.contains(event.getPlayer().getUniqueId()))) {
                        if (!(canBreak(event.getBlock().getLocation(), event.getPlayer()))) return;
                        if (player.getInventory().firstEmpty() == -1) {
                            int is = 0;
                            ItemStack item = new ItemStack(Material.STONE, amount);
                            for (ItemStack o : player.getInventory().getContents()) {
                                is++;
                                if (o == null) continue;
                                int l = o.getAmount();
                                if (o.getType().equals(item.getType()) && o.getAmount() < item.getMaxStackSize()) {
                                    player.getInventory().addItem(new ItemStack(Material.STONE, amount));
                                    event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                    break;
                                }
                                if (is == 36 && l != 1 + 1) {
                                    if (MessageUtil.canSendMessage(player))
                                        HoloAPI.createHologram(player, LocationUtil.getLocationInfront(player), NovaDrops.getInstance().getConfig().getString("messages.hologram"));
                                    MessageUtil.sendMessage(player, NovaDrops.fullInv);

                                    destroyedBlock.getLocation().getWorld().dropItemNaturally(destroyedBlock.getLocation(), new ItemStack(Material.STONE, amount));
                                    destroyedBlock.getWorld().getBlockAt(destroyedBlock.getLocation()).setType(Material.AIR);
                                    event.getBlock().getWorld().playEffect(event.getBlock().getLocation(), Effect.MOBSPAWNER_FLAMES, 30);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                }
                            }
                        } else {
                            player.getInventory().addItem(new ItemStack(Material.COBBLESTONE, amount));
                            event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                            event.getBlock().getWorld().playEffect(event.getBlock().getLocation(), Effect.MOBSPAWNER_FLAMES, 30);
                            itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                            event.getPlayer().setItemInHand(itemInHand);
                        }
                    } else if (NovaDrops.playerDrops.contains(event.getPlayer().getUniqueId())) {
                        if (!(canBreak(event.getBlock().getLocation(), event.getPlayer()))) return;
                        if (player.getInventory().firstEmpty() == -1) {
                            int is = 0;
                            ItemStack item = new ItemStack(Material.COBBLESTONE, amount);
                            for (ItemStack o : player.getInventory().getContents()) {
                                is++;
                                if (o == null) continue;
                                int l = o.getAmount();
                                if (o.getType().equals(item.getType()) && o.getAmount() < item.getMaxStackSize()) {
                                    player.getInventory().addItem(new ItemStack(Material.COBBLESTONE, amount));
                                    event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                    break;
                                }
                                if (is == 36 && l != 1 + 1) {
                                    if (MessageUtil.canSendMessage(player))
                                        HoloAPI.createHologram(player, LocationUtil.getLocationInfront(player), NovaDrops.getInstance().getConfig().getString("messages.hologram"));
                                    MessageUtil.sendMessage(player, NovaDrops.fullInv);

                                    destroyedBlock.getLocation().getWorld().dropItemNaturally(destroyedBlock.getLocation(), new ItemStack(Material.COBBLESTONE, amount));
                                    destroyedBlock.getWorld().getBlockAt(destroyedBlock.getLocation()).setType(Material.AIR);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                }
                            }
                        } else {
                            player.getInventory().addItem(new ItemStack(Material.COBBLESTONE, amount));
                            event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                            itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                            event.getPlayer().setItemInHand(itemInHand);
                        }
                    } else if (NovaDrops.playerSmelt.contains(event.getPlayer().getUniqueId())) {
                        if (!(canBreak(event.getBlock().getLocation(), event.getPlayer()))) return;
                        event.getBlock().getWorld().playEffect(event.getBlock().getLocation(), Effect.MOBSPAWNER_FLAMES, 30);
                        itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                        event.getPlayer().setItemInHand(itemInHand);
                        player.getInventory().addItem(new ItemStack(Material.STONE, amount));
                        destroyedBlock.getWorld().getBlockAt(destroyedBlock.getLocation()).setType(Material.AIR);
                        itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                        event.getPlayer().setItemInHand(itemInHand);
                    }
                } else if (event.getBlock().getType() == Material.MELON_BLOCK) {
                    if (NovaDrops.playerDrops.contains(event.getPlayer().getUniqueId()) && (NovaDrops.playerSmelt.contains(event.getPlayer().getUniqueId()))) {
                        if (!(canBreak(event.getBlock().getLocation(), event.getPlayer()))) return;
                        if (player.getInventory().firstEmpty() == -1) {
                            int is = 0;
                            ItemStack item = new ItemStack(Material.MELON_BLOCK, amount);
                            for (ItemStack o : player.getInventory().getContents()) {
                                is++;
                                if (o == null) continue;
                                int l = o.getAmount();
                                if (o.getType().equals(item.getType()) && o.getAmount() < item.getMaxStackSize()) {
                                    player.getInventory().addItem(new ItemStack(Material.MELON_BLOCK, amount));
                                    event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                    break;
                                }
                                if (is == 36 && l != 1 + 1) {
                                    if (MessageUtil.canSendMessage(player))
                                        HoloAPI.createHologram(player, LocationUtil.getLocationInfront(player), NovaDrops.getInstance().getConfig().getString("messages.hologram"));
                                    MessageUtil.sendMessage(player, NovaDrops.fullInv);

                                    destroyedBlock.getLocation().getWorld().dropItemNaturally(destroyedBlock.getLocation(), new ItemStack(Material.MELON_BLOCK, amount));
                                    destroyedBlock.getWorld().getBlockAt(destroyedBlock.getLocation()).setType(Material.AIR);
                                    event.getBlock().getWorld().playEffect(event.getBlock().getLocation(), Effect.MOBSPAWNER_FLAMES, 30);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                }
                            }
                        } else {
                            player.getInventory().addItem(new ItemStack(Material.MELON_BLOCK, amount));
                            event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                            event.getBlock().getWorld().playEffect(event.getBlock().getLocation(), Effect.SMOKE, 30);
                            itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                            event.getPlayer().setItemInHand(itemInHand);
                        }
                    } else if (NovaDrops.playerDrops.contains(event.getPlayer().getUniqueId())) {
                        if (!(canBreak(event.getBlock().getLocation(), event.getPlayer()))) return;
                        if (player.getInventory().firstEmpty() == -1) {
                            int is = 0;
                            ItemStack item = new ItemStack(Material.MELON, amount + 3);
                            for (ItemStack o : player.getInventory().getContents()) {
                                is++;
                                if (o == null) continue;
                                int l = o.getAmount();
                                if (o.getType().equals(item.getType()) && o.getAmount() < item.getMaxStackSize()) {
                                    player.getInventory().addItem(new ItemStack(Material.MELON, amount + 3));
                                    event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                    break;
                                }
                                if (is == 36 && l != 1 + 1) {
                                    if (MessageUtil.canSendMessage(player))
                                        HoloAPI.createHologram(player, LocationUtil.getLocationInfront(player), NovaDrops.getInstance().getConfig().getString("messages.hologram"));
                                    MessageUtil.sendMessage(player, NovaDrops.fullInv);

                                    destroyedBlock.getLocation().getWorld().dropItemNaturally(destroyedBlock.getLocation(), new ItemStack(Material.MELON, amount + 3));
                                    destroyedBlock.getWorld().getBlockAt(destroyedBlock.getLocation()).setType(Material.AIR);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                }
                            }
                        } else {
                            player.getInventory().addItem(new ItemStack(Material.MELON, amount + 3));
                            event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                            itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                            event.getPlayer().setItemInHand(itemInHand);
                        }
                    } else if (NovaDrops.playerSmelt.contains(event.getPlayer().getUniqueId())) {
                        if (!(canBreak(event.getBlock().getLocation(), event.getPlayer()))) return;
                        destroyedBlock.getLocation().getWorld().dropItemNaturally(destroyedBlock.getLocation(), new ItemStack(Material.MELON_BLOCK, amount));
                        destroyedBlock.getWorld().getBlockAt(destroyedBlock.getLocation()).setType(Material.AIR);
                        event.getBlock().getWorld().playEffect(event.getBlock().getLocation(), Effect.SMOKE, 30);
                        itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                        event.getPlayer().setItemInHand(itemInHand);
                    }
                } else if (event.getBlock().getType() == Material.PUMPKIN) {
                    if (NovaDrops.playerDrops.contains(event.getPlayer().getUniqueId()) && (NovaDrops.playerSmelt.contains(event.getPlayer().getUniqueId()))) {
                        if (!(canBreak(event.getBlock().getLocation(), event.getPlayer()))) return;
                        if (player.getInventory().firstEmpty() == -1) {
                            int is = 0;
                            ItemStack item = new ItemStack(Material.PUMPKIN, amount);
                            for (ItemStack o : player.getInventory().getContents()) {
                                is++;
                                if (o == null) continue;
                                int l = o.getAmount();
                                if (o.getType().equals(item.getType()) && o.getAmount() < item.getMaxStackSize()) {
                                    player.getInventory().addItem(new ItemStack(Material.PUMPKIN, amount));
                                    event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                    break;
                                }
                                if (is == 36 && l != 1 + 1) {
                                    if (MessageUtil.canSendMessage(player))
                                        HoloAPI.createHologram(player, LocationUtil.getLocationInfront(player), NovaDrops.getInstance().getConfig().getString("messages.hologram"));
                                    MessageUtil.sendMessage(player, NovaDrops.fullInv);

                                    destroyedBlock.getLocation().getWorld().dropItemNaturally(destroyedBlock.getLocation(), new ItemStack(Material.PUMPKIN, amount));
                                    destroyedBlock.getWorld().getBlockAt(destroyedBlock.getLocation()).setType(Material.AIR);
                                    event.getBlock().getWorld().playEffect(event.getBlock().getLocation(), Effect.MOBSPAWNER_FLAMES, 30);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                }
                            }
                        } else {
                            player.getInventory().addItem(new ItemStack(Material.PUMPKIN, amount));
                            event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                            event.getBlock().getWorld().playEffect(event.getBlock().getLocation(), Effect.SMOKE, 30);
                            itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                            event.getPlayer().setItemInHand(itemInHand);
                        }
                    } else if (NovaDrops.playerDrops.contains(event.getPlayer().getUniqueId())) {
                        if (!(canBreak(event.getBlock().getLocation(), event.getPlayer()))) return;
                        if (player.getInventory().firstEmpty() == -1) {
                            int is = 0;
                            ItemStack item = new ItemStack(Material.PUMPKIN, amount);
                            for (ItemStack o : player.getInventory().getContents()) {
                                is++;
                                if (o == null) continue;
                                int l = o.getAmount();
                                if (o.getType().equals(item.getType()) && o.getAmount() < item.getMaxStackSize()) {
                                    player.getInventory().addItem(new ItemStack(Material.PUMPKIN, amount));
                                    event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                    break;
                                }
                                if (is == 36 && l != 1 + 1) {
                                    if (MessageUtil.canSendMessage(player))
                                        HoloAPI.createHologram(player, LocationUtil.getLocationInfront(player), NovaDrops.getInstance().getConfig().getString("messages.hologram"));
                                    MessageUtil.sendMessage(player, NovaDrops.fullInv);

                                    destroyedBlock.getLocation().getWorld().dropItemNaturally(destroyedBlock.getLocation(), new ItemStack(Material.PUMPKIN, amount));
                                    destroyedBlock.getWorld().getBlockAt(destroyedBlock.getLocation()).setType(Material.AIR);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                }
                            }
                        } else {
                            player.getInventory().addItem(new ItemStack(Material.PUMPKIN, amount));
                            event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                            itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                            event.getPlayer().setItemInHand(itemInHand);
                        }
                    } else if (NovaDrops.playerSmelt.contains(event.getPlayer().getUniqueId())) {
                        if (!(canBreak(event.getBlock().getLocation(), event.getPlayer()))) return;
                        destroyedBlock.getLocation().getWorld().dropItemNaturally(destroyedBlock.getLocation(), new ItemStack(Material.PUMPKIN, amount));
                        destroyedBlock.getWorld().getBlockAt(destroyedBlock.getLocation()).setType(Material.AIR);
                        event.getBlock().getWorld().playEffect(event.getBlock().getLocation(), Effect.SMOKE, 30);
                        itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                        event.getPlayer().setItemInHand(itemInHand);
                    }
                } else if (event.getBlock().getType() == Material.COAL_ORE) {
                    if (NovaDrops.playerDrops.contains(event.getPlayer().getUniqueId())) {
                        if (!(canBreak(event.getBlock().getLocation(), event.getPlayer()))) return;
                        if (itemInHand.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
                            if (NovaDrops.allowedWorlds.contains(event.getPlayer().getWorld().getName())) {
                                int level = itemInHand.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
                                int chance = level / (3 - random.nextInt(2));
                                ;
                                int drops = chance > 0 ? random.nextInt(chance) : 0;
                                amount = drops + 1;
                            }
                        } else {
                            amount = 1;
                        }
                        if (player.getInventory().firstEmpty() == -1) {
                            int is = 0;
                            ItemStack item = new ItemStack(hasSilktouch ? Material.COAL_ORE : Material.COAL, amount);
                            for (ItemStack o : player.getInventory().getContents()) {
                                is++;
                                if (o == null) continue;
                                int l = o.getAmount();
                                if (o.getType().equals(item.getType()) && o.getAmount() < item.getMaxStackSize()) {
                                    player.getInventory().addItem(new ItemStack(hasSilktouch ? Material.COAL_ORE : Material.COAL, this.getAmount(player, amount)));
                                    event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                    break;
                                }
                                if (is == 36 && l != 1 + 1) {
                                    if (MessageUtil.canSendMessage(player))
                                        HoloAPI.createHologram(player, LocationUtil.getLocationInfront(player), NovaDrops.getInstance().getConfig().getString("messages.hologram"));
                                    MessageUtil.sendMessage(player, NovaDrops.fullInv);

                                    destroyedBlock.getLocation().getWorld().dropItemNaturally(destroyedBlock.getLocation(), new ItemStack(hasSilktouch ? Material.COAL_ORE : Material.COAL, amount));
                                    destroyedBlock.getWorld().getBlockAt(destroyedBlock.getLocation()).setType(Material.AIR);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                }
                            }
                        } else {
                            player.getInventory().addItem(new ItemStack(hasSilktouch ? Material.COAL_ORE : Material.COAL, this.getAmount(player, amount)));
                            event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                            itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                            event.getPlayer().setItemInHand(itemInHand);
                        }
                    }
                } else if (event.getBlock().getType() == Material.EMERALD_ORE) {
                    if (NovaDrops.playerDrops.contains(event.getPlayer().getUniqueId())) {
                        if (!(canBreak(event.getBlock().getLocation(), event.getPlayer()))) return;
                        if (itemInHand.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
                            if (NovaDrops.allowedWorlds.contains(event.getPlayer().getWorld().getName())) {
                                int level = itemInHand.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
                                int chance = level / (3 - random.nextInt(2));
                                ;
                                int drops = chance > 0 ? random.nextInt(chance) : 0;
                                amount = drops + 1;
                            }
                        } else {
                            amount = 1;
                        }
                        if (player.getInventory().firstEmpty() == -1) {
                            int is = 0;
                            ItemStack item = new ItemStack(hasSilktouch ? Material.EMERALD_ORE : Material.EMERALD, amount);
                            for (ItemStack o : player.getInventory().getContents()) {
                                is++;
                                if (o == null) continue;
                                int l = o.getAmount();
                                if (o.getType().equals(item.getType()) && o.getAmount() < item.getMaxStackSize()) {
                                    player.getInventory().addItem(new ItemStack(hasSilktouch ? Material.EMERALD_ORE : Material.EMERALD, this.getAmount(player, amount)));
                                    event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                    break;
                                }
                                if (is == 36 && l != 1 + 1) {
                                    if (MessageUtil.canSendMessage(player))
                                        HoloAPI.createHologram(player, LocationUtil.getLocationInfront(player), NovaDrops.getInstance().getConfig().getString("messages.hologram"));
                                    MessageUtil.sendMessage(player, NovaDrops.fullInv);

                                    destroyedBlock.getLocation().getWorld().dropItemNaturally(destroyedBlock.getLocation(), new ItemStack(hasSilktouch ? Material.EMERALD_ORE : Material.EMERALD, amount));
                                    destroyedBlock.getWorld().getBlockAt(destroyedBlock.getLocation()).setType(Material.AIR);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                }
                            }
                        } else {
                            player.getInventory().addItem(new ItemStack(hasSilktouch ? Material.EMERALD_ORE : Material.EMERALD, this.getAmount(player, amount)));
                            event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                            itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                            event.getPlayer().setItemInHand(itemInHand);
                        }
                    }
                } else if (event.getBlock().getType() == Material.DIAMOND_ORE) {
                    if (NovaDrops.playerDrops.contains(event.getPlayer().getUniqueId())) {
                        if (!(canBreak(event.getBlock().getLocation(), event.getPlayer()))) return;
                        if (itemInHand.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
                            if (NovaDrops.allowedWorlds.contains(event.getPlayer().getWorld().getName())) {
                                int level = itemInHand.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
                                int chance = level / (3 - random.nextInt(2));
                                ;
                                int drops = chance > 0 ? random.nextInt(chance) : 0;
                                amount = drops + 1;
                            }
                        } else {
                            amount = 1;
                        }
                        if (player.getInventory().firstEmpty() == -1) {
                            int is = 0;
                            ItemStack item = new ItemStack(hasSilktouch ? Material.DIAMOND_ORE : Material.DIAMOND, amount);
                            for (ItemStack o : player.getInventory().getContents()) {
                                is++;
                                if (o == null) continue;
                                int l = o.getAmount();
                                if (o.getType().equals(item.getType()) && o.getAmount() < item.getMaxStackSize()) {
                                    player.getInventory().addItem(new ItemStack(hasSilktouch ? Material.DIAMOND_ORE : Material.DIAMOND, this.getAmount(player, amount)));
                                    event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                    break;
                                }
                                if (is == 36 && l != 1 + 1) {
                                    if (MessageUtil.canSendMessage(player))
                                        HoloAPI.createHologram(player, LocationUtil.getLocationInfront(player), NovaDrops.getInstance().getConfig().getString("messages.hologram"));
                                    MessageUtil.sendMessage(player, NovaDrops.fullInv);

                                    destroyedBlock.getLocation().getWorld().dropItemNaturally(destroyedBlock.getLocation(), new ItemStack(hasSilktouch ? Material.DIAMOND_ORE : Material.DIAMOND, amount));
                                    destroyedBlock.getWorld().getBlockAt(destroyedBlock.getLocation()).setType(Material.AIR);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                }
                            }
                        } else {
                            player.getInventory().addItem(new ItemStack(hasSilktouch ? Material.DIAMOND_ORE : Material.DIAMOND, this.getAmount(player, amount)));
                            event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                            itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                            event.getPlayer().setItemInHand(itemInHand);
                        }
                    }
                } else if (event.getBlock().getType() == Material.IRON_BLOCK) {
                    if (NovaDrops.playerDrops.contains(event.getPlayer().getUniqueId())) {
                        if (!(canBreak(event.getBlock().getLocation(), event.getPlayer()))) return;
                        if (itemInHand.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
                            if (NovaDrops.allowedWorlds.contains(event.getPlayer().getWorld().getName())) {
                                int level = itemInHand.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
                                int chance = level / (3 - random.nextInt(2));
                                ;
                                int drops = chance > 0 ? random.nextInt(chance) : 0;
                                amount = drops + 1;
                            }
                        } else {
                            amount = 1;
                        }
                        if (player.getInventory().firstEmpty() == -1) {
                            int is = 0;
                            ItemStack item = new ItemStack(Material.IRON_BLOCK, amount);
                            for (ItemStack o : player.getInventory().getContents()) {
                                is++;
                                if (o == null) continue;
                                int l = o.getAmount();
                                if (o.getType().equals(item.getType()) && o.getAmount() < item.getMaxStackSize()) {
                                    player.getInventory().addItem(new ItemStack(Material.IRON_BLOCK, this.getAmount(player, amount)));
                                    event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                    break;
                                }
                                if (is == 36 && l != 1 + 1) {
                                    if (MessageUtil.canSendMessage(player))
                                        HoloAPI.createHologram(player, LocationUtil.getLocationInfront(player), NovaDrops.getInstance().getConfig().getString("messages.hologram"));
                                    MessageUtil.sendMessage(player, NovaDrops.fullInv);

                                    destroyedBlock.getLocation().getWorld().dropItemNaturally(destroyedBlock.getLocation(), new ItemStack(Material.IRON_BLOCK, amount));
                                    destroyedBlock.getWorld().getBlockAt(destroyedBlock.getLocation()).setType(Material.AIR);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                }
                            }
                        } else {
                            player.getInventory().addItem(new ItemStack(Material.IRON_BLOCK, this.getAmount(player, amount)));
                            event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                            itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                            event.getPlayer().setItemInHand(itemInHand);
                        }
                    }
                } else if (event.getBlock().getType() == Material.GOLD_BLOCK) {
                    if (NovaDrops.playerDrops.contains(event.getPlayer().getUniqueId())) {
                        if (!(canBreak(event.getBlock().getLocation(), event.getPlayer()))) return;
                        if (itemInHand.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
                            if (NovaDrops.allowedWorlds.contains(event.getPlayer().getWorld().getName())) {
                                int level = itemInHand.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
                                int chance = level / (3 - random.nextInt(2));
                                ;
                                int drops = random.nextInt(chance + 1);
                                amount = drops + 1;
                            }
                        } else {
                            amount = 1;
                        }
                        if (player.getInventory().firstEmpty() == -1) {
                            int is = 0;
                            ItemStack item = new ItemStack(Material.GOLD_BLOCK, amount);
                            for (ItemStack o : player.getInventory().getContents()) {
                                is++;
                                if (o == null) continue;
                                int l = o.getAmount();
                                if (o.getType().equals(item.getType()) && o.getAmount() < item.getMaxStackSize()) {
                                    player.getInventory().addItem(new ItemStack(Material.GOLD_BLOCK, this.getAmount(player, amount)));
                                    event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                    break;
                                }
                                if (is == 36 && l != 1 + 1) {
                                    if (MessageUtil.canSendMessage(player))
                                        HoloAPI.createHologram(player, LocationUtil.getLocationInfront(player), NovaDrops.getInstance().getConfig().getString("messages.hologram"));
                                    MessageUtil.sendMessage(player, NovaDrops.fullInv);

                                    destroyedBlock.getLocation().getWorld().dropItemNaturally(destroyedBlock.getLocation(), new ItemStack(Material.GOLD_BLOCK, amount));
                                    destroyedBlock.getWorld().getBlockAt(destroyedBlock.getLocation()).setType(Material.AIR);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                }
                            }
                        } else {
                            player.getInventory().addItem(new ItemStack(Material.GOLD_BLOCK, this.getAmount(player, amount)));
                            event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                            itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                            event.getPlayer().setItemInHand(itemInHand);
                        }
                    }
                } else if (event.getBlock().getType() == Material.EMERALD_BLOCK) {
                    if (NovaDrops.playerDrops.contains(event.getPlayer().getUniqueId())) {
                        if (!(canBreak(event.getBlock().getLocation(), event.getPlayer()))) return;
                        if (itemInHand.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
                            if (NovaDrops.allowedWorlds.contains(event.getPlayer().getWorld().getName())) {
                                int level = itemInHand.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
                                int chance = level / (3 - random.nextInt(2));
                                int drops = chance > 0 ? random.nextInt(chance) : 0;
                                amount = drops + 1;
                            }
                        } else {
                            amount = 1;
                        }
                        if (player.getInventory().firstEmpty() == -1) {
                            int is = 0;
                            ItemStack item = new ItemStack(Material.EMERALD_BLOCK, amount);
                            for (ItemStack o : player.getInventory().getContents()) {
                                is++;
                                if (o == null) continue;
                                int l = o.getAmount();
                                if (o.getType().equals(item.getType()) && o.getAmount() < item.getMaxStackSize()) {
                                    player.getInventory().addItem(new ItemStack(Material.EMERALD_BLOCK, this.getAmount(player, amount)));
                                    event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                    break;
                                }
                                if (is == 36 && l != 1 + 1) {
                                    if (MessageUtil.canSendMessage(player))
                                        HoloAPI.createHologram(player, LocationUtil.getLocationInfront(player), NovaDrops.getInstance().getConfig().getString("messages.hologram"));
                                    MessageUtil.sendMessage(player, NovaDrops.fullInv);

                                    destroyedBlock.getLocation().getWorld().dropItemNaturally(destroyedBlock.getLocation(), new ItemStack(Material.EMERALD_BLOCK, amount));
                                    destroyedBlock.getWorld().getBlockAt(destroyedBlock.getLocation()).setType(Material.AIR);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                }
                            }
                        } else {
                            player.getInventory().addItem(new ItemStack(Material.EMERALD_BLOCK, this.getAmount(player, amount)));
                            event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                            itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                            event.getPlayer().setItemInHand(itemInHand);
                        }
                    }
                } else if (event.getBlock().getType() == Material.DIAMOND_BLOCK) {
                    if (NovaDrops.playerDrops.contains(event.getPlayer().getUniqueId())) {
                        if (!(canBreak(event.getBlock().getLocation(), event.getPlayer()))) return;
                        if (itemInHand.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
                            if (NovaDrops.allowedWorlds.contains(event.getPlayer().getWorld().getName())) {
                                int level = itemInHand.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
                                int chance = level / (3 - random.nextInt(2));
                                ;
                                int drops = chance > 0 ? random.nextInt(chance) : 0;
                                amount = drops + 1;
                            }
                        } else {
                            amount = 1;
                        }
                        if (player.getInventory().firstEmpty() == -1) {
                            int is = 0;
                            ItemStack item = new ItemStack(Material.DIAMOND_BLOCK, amount);
                            for (ItemStack o : player.getInventory().getContents()) {
                                is++;
                                if (o == null) continue;
                                int l = o.getAmount();
                                if (o.getType().equals(item.getType()) && o.getAmount() < item.getMaxStackSize()) {
                                    player.getInventory().addItem(new ItemStack(Material.DIAMOND_BLOCK, this.getAmount(player, amount)));
                                    event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                    break;
                                }
                                if (is == 36 && l != 1 + 1) {
                                    if (MessageUtil.canSendMessage(player))
                                        HoloAPI.createHologram(player, LocationUtil.getLocationInfront(player), NovaDrops.getInstance().getConfig().getString("messages.hologram"));
                                    MessageUtil.sendMessage(player, NovaDrops.fullInv);

                                    destroyedBlock.getLocation().getWorld().dropItemNaturally(destroyedBlock.getLocation(), new ItemStack(Material.DIAMOND_BLOCK, amount));
                                    destroyedBlock.getWorld().getBlockAt(destroyedBlock.getLocation()).setType(Material.AIR);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                }
                            }
                        } else {
                            player.getInventory().addItem(new ItemStack(Material.DIAMOND_BLOCK, this.getAmount(player, amount)));
                            event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                            itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                            event.getPlayer().setItemInHand(itemInHand);
                        }
                    }
                } else if (event.getBlock().getType() == Material.LAPIS_BLOCK) {
                    if (NovaDrops.playerDrops.contains(event.getPlayer().getUniqueId())) {
                        if (!(canBreak(event.getBlock().getLocation(), event.getPlayer()))) return;
                        if (itemInHand.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
                            if (NovaDrops.allowedWorlds.contains(event.getPlayer().getWorld().getName())) {
                                int level = itemInHand.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
                                int chance = level / (3 - random.nextInt(2));
                                ;
                                int drops = chance > 0 ? random.nextInt(chance) : 0;
                                amount = drops + 1;
                            }
                        } else {
                            amount = 1;
                        }
                        if (player.getInventory().firstEmpty() == -1) {
                            int is = 0;
                            ItemStack item = new ItemStack(Material.LAPIS_BLOCK, amount);
                            for (ItemStack o : player.getInventory().getContents()) {
                                is++;
                                if (o == null) continue;
                                int l = o.getAmount();
                                if (o.getType().equals(item.getType()) && o.getAmount() < item.getMaxStackSize()) {
                                    player.getInventory().addItem(new ItemStack(Material.LAPIS_BLOCK, this.getAmount(player, amount)));
                                    event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                    break;
                                }
                                if (is == 36 && l != 1 + 1) {
                                    if (MessageUtil.canSendMessage(player))
                                        HoloAPI.createHologram(player, LocationUtil.getLocationInfront(player), NovaDrops.getInstance().getConfig().getString("messages.hologram"));
                                    MessageUtil.sendMessage(player, NovaDrops.fullInv);

                                    destroyedBlock.getLocation().getWorld().dropItemNaturally(destroyedBlock.getLocation(), new ItemStack(Material.LAPIS_BLOCK, amount));
                                    destroyedBlock.getWorld().getBlockAt(destroyedBlock.getLocation()).setType(Material.AIR);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                }
                            }
                        } else {
                            player.getInventory().addItem(new ItemStack(Material.LAPIS_BLOCK, this.getAmount(player, amount)));
                            event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                            itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                            event.getPlayer().setItemInHand(itemInHand);
                        }
                    }
                } else if (event.getBlock().getType() == Material.REDSTONE_BLOCK) {
                    if (NovaDrops.playerDrops.contains(event.getPlayer().getUniqueId())) {
                        if (!(canBreak(event.getBlock().getLocation(), event.getPlayer()))) return;
                        if (itemInHand.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
                            if (NovaDrops.allowedWorlds.contains(event.getPlayer().getWorld().getName())) {
                                int level = itemInHand.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
                                int chance = level / (3 - random.nextInt(2));
                                ;
                                int drops = chance > 0 ? random.nextInt(chance) : 0;
                                amount = drops + 1;
                            }
                        } else {
                            amount = 1;
                        }
                        if (player.getInventory().firstEmpty() == -1) {
                            int is = 0;
                            ItemStack item = new ItemStack(Material.REDSTONE_BLOCK, amount);
                            for (ItemStack o : player.getInventory().getContents()) {
                                is++;
                                if (o == null) continue;
                                int l = o.getAmount();
                                if (o.getType().equals(item.getType()) && o.getAmount() < item.getMaxStackSize()) {
                                    player.getInventory().addItem(new ItemStack(Material.REDSTONE_BLOCK, this.getAmount(player, amount)));
                                    event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                    break;
                                }
                                if (is == 36 && l != 1 + 1) {
                                    if (MessageUtil.canSendMessage(player))
                                        HoloAPI.createHologram(player, LocationUtil.getLocationInfront(player), NovaDrops.getInstance().getConfig().getString("messages.hologram"));
                                    MessageUtil.sendMessage(player, NovaDrops.fullInv);

                                    destroyedBlock.getLocation().getWorld().dropItemNaturally(destroyedBlock.getLocation(), new ItemStack(Material.REDSTONE_BLOCK, amount));
                                    destroyedBlock.getWorld().getBlockAt(destroyedBlock.getLocation()).setType(Material.AIR);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                }
                            }
                        } else {
                            player.getInventory().addItem(new ItemStack(Material.REDSTONE_BLOCK, this.getAmount(player, amount)));
                            event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                            itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                            event.getPlayer().setItemInHand(itemInHand);
                        }
                    }
                } else if (event.getBlock().getType() == Material.COAL_BLOCK) {
                    if (NovaDrops.playerDrops.contains(event.getPlayer().getUniqueId())) {
                        if (!(canBreak(event.getBlock().getLocation(), event.getPlayer()))) return;
                        if (itemInHand.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
                            if (NovaDrops.allowedWorlds.contains(event.getPlayer().getWorld().getName())) {
                                int level = itemInHand.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
                                int chance = level / (3 - random.nextInt(2));
                                ;
                                int drops = chance > 0 ? random.nextInt(chance) : 0;
                                amount = drops + 1;
                            }
                        } else {
                            amount = 1;
                        }
                        if (player.getInventory().firstEmpty() == -1) {
                            int is = 0;
                            ItemStack item = new ItemStack(Material.COAL_BLOCK, amount);
                            for (ItemStack o : player.getInventory().getContents()) {
                                is++;
                                if (o == null) continue;
                                int l = o.getAmount();
                                if (o.getType().equals(item.getType()) && o.getAmount() < item.getMaxStackSize()) {
                                    player.getInventory().addItem(new ItemStack(Material.COAL_BLOCK, this.getAmount(player, amount)));
                                    event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                    break;
                                }
                                if (is == 36 && l != 1 + 1) {
                                    if (MessageUtil.canSendMessage(player))
                                        HoloAPI.createHologram(player, LocationUtil.getLocationInfront(player), NovaDrops.getInstance().getConfig().getString("messages.hologram"));
                                    MessageUtil.sendMessage(player, NovaDrops.fullInv);

                                    destroyedBlock.getLocation().getWorld().dropItemNaturally(destroyedBlock.getLocation(), new ItemStack(Material.COAL_BLOCK, amount));
                                    destroyedBlock.getWorld().getBlockAt(destroyedBlock.getLocation()).setType(Material.AIR);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                }
                            }
                        } else {
                            player.getInventory().addItem(new ItemStack(Material.COAL_BLOCK, this.getAmount(player, amount)));
                            event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                            itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                            event.getPlayer().setItemInHand(itemInHand);
                        }
                    }
                } else if (event.getBlock().getType() == Material.COCOA) {
                    if (NovaDrops.playerDrops.contains(event.getPlayer().getUniqueId())) {
                        if (!(canBreak(event.getBlock().getLocation(), event.getPlayer()))) return;
                        if (itemInHand.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
                            if (NovaDrops.allowedWorlds.contains(event.getPlayer().getWorld().getName())) {
                                int level = itemInHand.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
                                int chance = level / (3 - random.nextInt(2));
                                ;
                                int drops = chance > 0 ? random.nextInt(chance) : 0;
                                amount = drops + 1;
                            }
                        } else {
                            amount = 1;
                        }
                        if (player.getInventory().firstEmpty() == -1) {
                            int is = 0;
                            ItemStack item = new ItemStack(Material.INK_SACK, amount, (byte) 3);
                            for (ItemStack o : player.getInventory().getContents()) {
                                is++;
                                if (o == null) continue;
                                int l = o.getAmount();
                                if (o.getType().equals(item.getType()) && o.getAmount() < item.getMaxStackSize()) {
                                    player.getInventory().addItem(new ItemStack(Material.INK_SACK, this.getAmount(player, amount), (byte) 3));
                                    event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                    break;
                                }
                                if (is == 36 && l != 1 + 1) {
                                    if (MessageUtil.canSendMessage(player))
                                        HoloAPI.createHologram(player, LocationUtil.getLocationInfront(player), NovaDrops.getInstance().getConfig().getString("messages.hologram"));
                                    MessageUtil.sendMessage(player, NovaDrops.fullInv);

                                    destroyedBlock.getLocation().getWorld().dropItemNaturally(destroyedBlock.getLocation(), new ItemStack(Material.INK_SACK, amount, (byte) 3));
                                    destroyedBlock.getWorld().getBlockAt(destroyedBlock.getLocation()).setType(Material.AIR);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                }
                            }
                        } else {
                            player.getInventory().addItem(new ItemStack(Material.INK_SACK, this.getAmount(player, amount), (byte) 3));
                            event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                            itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                            event.getPlayer().setItemInHand(itemInHand);
                        }
                    }
                } else if (event.getBlock().getType() == Material.QUARTZ_ORE) {
                    if (NovaDrops.playerDrops.contains(event.getPlayer().getUniqueId())) {
                        if (!(canBreak(event.getBlock().getLocation(), event.getPlayer()))) return;
                        if (itemInHand.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
                            if (NovaDrops.allowedWorlds.contains(event.getPlayer().getWorld().getName())) {
                                int level = itemInHand.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
                                int chance = level / (3 - random.nextInt(2));
                                ;
                                int drops = chance > 0 ? random.nextInt(chance) : 0;
                                amount = drops + 1;
                            }
                        } else {
                            amount = 1;
                        }
                        if (player.getInventory().firstEmpty() == -1) {
                            int is = 0;
                            ItemStack item = new ItemStack(hasSilktouch ? Material.QUARTZ_ORE : Material.QUARTZ, amount);
                            for (ItemStack o : player.getInventory().getContents()) {
                                is++;
                                if (o == null) continue;
                                int l = o.getAmount();
                                if (o.getType().equals(item.getType()) && o.getAmount() < item.getMaxStackSize()) {
                                    player.getInventory().addItem(new ItemStack(hasSilktouch ? Material.QUARTZ_ORE : Material.QUARTZ, this.getAmount(player, amount)));
                                    event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                    break;
                                }
                                if (is == 36 && l != 1 + 1) {
                                    if (MessageUtil.canSendMessage(player))
                                        HoloAPI.createHologram(player, LocationUtil.getLocationInfront(player), NovaDrops.getInstance().getConfig().getString("messages.hologram"));
                                    MessageUtil.sendMessage(player, NovaDrops.fullInv);

                                    destroyedBlock.getLocation().getWorld().dropItemNaturally(destroyedBlock.getLocation(), new ItemStack(hasSilktouch ? Material.QUARTZ_ORE : Material.QUARTZ, amount));
                                    destroyedBlock.getWorld().getBlockAt(destroyedBlock.getLocation()).setType(Material.AIR);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                }
                            }
                        } else {
                            player.getInventory().addItem(new ItemStack(hasSilktouch ? Material.QUARTZ_ORE : Material.QUARTZ, this.getAmount(player, amount)));
                            event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                            itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                            event.getPlayer().setItemInHand(itemInHand);
                        }
                    }
                } else if (event.getBlock().getType() == Material.LAPIS_ORE) {
                    if (NovaDrops.playerDrops.contains(event.getPlayer().getUniqueId())) {
                        if (!(canBreak(event.getBlock().getLocation(), event.getPlayer()))) return;
                        if (itemInHand.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
                            if (NovaDrops.allowedWorlds.contains(event.getPlayer().getWorld().getName())) {
                                int level = itemInHand.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
                                int chance = level / (3 - random.nextInt(2));
                                ;
                                int drops = chance > 0 ? random.nextInt(chance) : 0;
                                amount = drops + 1;
                            }
                        } else {
                            amount = 1;
                        }
                        if (player.getInventory().firstEmpty() == -1) {
                            int is = 0;
                            ItemStack item = new ItemStack(hasSilktouch ? Material.LAPIS_ORE : Material.INK_SACK, amount, (short) 4);
                            for (ItemStack o : player.getInventory().getContents()) {
                                is++;
                                if (o == null) continue;
                                int l = o.getAmount();
                                if (o.getType().equals(item.getType()) && o.getAmount() < item.getMaxStackSize()) {
                                    player.getInventory().addItem(new ItemStack(hasSilktouch ? Material.LAPIS_ORE : Material.INK_SACK, this.getAmount(player, amount), (short) 4));
                                    event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                    break;
                                }
                                if (is == 36 && l != 1 + 1) {
                                    if (MessageUtil.canSendMessage(player))
                                        HoloAPI.createHologram(player, LocationUtil.getLocationInfront(player), NovaDrops.getInstance().getConfig().getString("messages.hologram"));
                                    MessageUtil.sendMessage(player, NovaDrops.fullInv);

                                    destroyedBlock.getLocation().getWorld().dropItemNaturally(destroyedBlock.getLocation(), new ItemStack(hasSilktouch ? Material.LAPIS_ORE : Material.INK_SACK, amount, (short) 4));
                                    destroyedBlock.getWorld().getBlockAt(destroyedBlock.getLocation()).setType(Material.AIR);
                                    itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                    event.getPlayer().setItemInHand(itemInHand);
                                }
                            }
                        } else {
                            player.getInventory().addItem(new ItemStack(hasSilktouch ? Material.LAPIS_ORE : Material.INK_SACK, this.getAmount(player, amount), (short) 4));
                            event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                            itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                            event.getPlayer().setItemInHand(itemInHand);
                        }
                    }
                } else if (NovaDrops.playerDrops.contains(event.getPlayer().getUniqueId())) {
                    if (!(canBreak(event.getBlock().getLocation(), event.getPlayer()))) return;
                    if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                        for (ItemStack item : event.getBlock().getDrops()) {
                            byte data = item.getData().getData();
                            ItemStack newItem = new ItemStack(item.getType(), amount, data);
                            if (newItem != null && newItem.getItemMeta() instanceof SkullMeta) {
                                if (event.getBlock().getState() instanceof Skull) {
                                    Skull blockSkull = (Skull) event.getBlock().getState();
                                    if (blockSkull.getSkullType() == SkullType.PLAYER && blockSkull.getOwner() != null) {
                                        SkullMeta skullMeta = (SkullMeta) newItem.getItemMeta();
                                        skullMeta.setOwner(blockSkull.getOwner());
                                        newItem.setItemMeta(skullMeta);
                                    }
                                }
                            }
                            if (player.getInventory().firstEmpty() == -1) {
                                int is = 0;
                                for (ItemStack o : player.getInventory().getContents()) {
                                    is++;
                                    if (o == null) continue;
                                    int l = o.getAmount();
                                    if (o.getType().equals(item.getType()) && o.getAmount() < item.getMaxStackSize()) {
                                        event.getPlayer().getInventory().addItem(newItem);
                                        event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                                        itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                        event.getPlayer().setItemInHand(itemInHand);
                                        break;
                                    }
                                    if (is == 36 && l != 1 + 1) {
                                        if (MessageUtil.canSendMessage(player))
                                            HoloAPI.createHologram(player, LocationUtil.getLocationInfront(player), NovaDrops.getInstance().getConfig().getString("messages.hologram"));
                                        MessageUtil.sendMessage(player, NovaDrops.fullInv);

                                        destroyedBlock.getLocation().getWorld().dropItemNaturally(destroyedBlock.getLocation(), newItem);
                                        destroyedBlock.getWorld().getBlockAt(destroyedBlock.getLocation()).setType(Material.AIR);
                                        itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                        event.getPlayer().setItemInHand(itemInHand);
                                    }
                                }
                            } else {
                                event.getPlayer().getInventory().addItem(newItem);
                                event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                                itemInHand.setDurability((short) (itemInHand.getDurability() + damage));
                                event.getPlayer().setItemInHand(itemInHand);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean canBreak(Location loc, Player p) {
        try {
            Plugin plugin = Bukkit.getPluginManager().getPlugin("WorldGuard");
            if (plugin == null || !(plugin instanceof WorldGuardPlugin)) return true;
            WorldGuardPlugin pl = (WorldGuardPlugin) plugin;
            return pl.canBuild(p, p.getWorld().getBlockAt((int) loc.getX(), (int) loc.getY(), (int) loc.getZ()));
        } catch (Exception e) {}
        return true;
    }

}