package com.novaprison.meow.util;

import com.novaprison.meow.Config;
import com.novaprison.meow.Meow;
import com.novaprison.meow.Task;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Random;

public class LegitMeowUtil {
    private static final Random random = new Random();

    public static void throwItem(Player p, int r) {
        if (p == null) return;
        Item dropped = p.getLocation().getWorld().dropItem(p.getLocation(), new ItemStack(Meow.pluginInstance.getConfig().getInt("Meow.bombitem.ID"), 1));
        dropped.setVelocity(p.getLocation().clone().add(0, 1.5D, 0).getDirection().normalize());
        Config.noPickup.add(dropped.getUniqueId());
        Task.startBomb(dropped, p, r);
    }

    public static void magic(Player p, Location loc, int radius) {
        if (p == null || loc == null || loc.getWorld() == null) return;
        if (radius < 0) radius = 0;
        for (int y = -radius; y <= radius; y++) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    Location nLoc = new Location(loc.getWorld(), (loc.getBlockX() + x), (loc.getBlockY() + y), (loc.getBlockZ() + z));
                    if (nLoc.distance(loc) < radius) {
                        Block block = loc.getWorld().getBlockAt(nLoc);

                        boolean a = false;
                        List<String> r = Meow.pluginInstance.getConfig().getStringList("Meow.regions");
                        for (String s : r) {
                            if (Meow.isWithinRegion(block, s)) {
                                a = true;
                                break;
                            }
                        }
                        if (Meow.getWorldGuard().canBuild(p, nLoc) && a) {
                            if (block.getType() != Material.AIR && block.getType() != Material.BEDROCK) {
                                ItemStack i = new ItemStack(block.getType(), randInt(Meow.pluginInstance.getConfig().getInt("Meow.minDrops"), Meow.pluginInstance.getConfig().getInt("Meow.maxDrops")), block.getData());
                                if (!p.getInventory().addItem(i).isEmpty())
                                    p.getLocation().getWorld().dropItem(block.getLocation(), i);

                                block.setType(Material.AIR);
                                ParticleEffect.CLOUD.display(block.getLocation().add(0.5D, 0.5D, 0.5D), 0.5F, 0.5F, 0.5F, 0.1F, 5);
                                ParticleEffect.FIREWORKS_SPARK.display(block.getLocation().add(0.5D, 0.5D, 0.5D), 0.5F, 0.5F, 0.5F, 0.1F, 3);
                                ParticleEffect.SMOKE.display(block.getLocation().add(0.5D, 0.5D, 0.5D), 0.5F, 0.5F, 0.5F, 0.1F, 3);
                            }
                        }
                    }
                }
            }
        }
    }

    public static int randInt(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

    public static void giveMinebomb(int a, int p, Player... pl) {
        ItemStack i = new ItemStack(Meow.pluginInstance.getConfig().getInt("Meow.bombitem.ID"), a, (short) (10 * p));
        ItemMeta im = i.getItemMeta();
        if (im != null) {
            im.setDisplayName(ChatColor.translateAlternateColorCodes('&', Meow.pluginInstance.getConfig().getString("Meow.bombitem.name").replace("%power%", "" + p)));
            i.setItemMeta(im);
        }
        for (Player player : pl) {
            player.getInventory().addItem(i);
        }
    }
}
