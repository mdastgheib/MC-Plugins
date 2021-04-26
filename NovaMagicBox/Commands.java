import net.minecraft.server.v1_7_R4.PacketPlayOutBlockAction;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.block.CraftBlock;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;

public class Commands implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args)
    {
        if(command.getName().equalsIgnoreCase("magicbox")) {
            if (!(sender instanceof Player)) {
                return true;
            }
            Player p = (Player) sender;

            if(!p.hasPermission("magicbox.admin")) {
                return true;
            }

            if (args.length == 0) {

                usage(p);
                return true;
            } else if (args.length == 1) {
                if(args[0].equalsIgnoreCase("items")) {
                    p.openInventory(MagicBoxPlugin.inv);
                    return true;
                } else if(args[0].equalsIgnoreCase("removecooldown")) {
                    MagicBoxPlugin.cooldown.remove(p.getUniqueId());
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aRemoved your Magic Box cooldown!"));
                    return true;
                } else if(args[0].equalsIgnoreCase("addchest")) {
                    Block block = p.getTargetBlock(null, 100);
                    if(block.getType() != Material.CHEST) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNot a check ur looking at lol"));
                        return true;
                    } else {
                        Location l = block.getLocation();
                        List<String> locs = MagicBoxPlugin.getInstance().getConfig().getStringList("Locations");
                        String loc = (l.getWorld().getName() + ":" + l.getX() + ":" + l.getY() + ":" + l.getZ());

                        if(locs.contains(loc)) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis chest is already added."));
                        } else {
                            locs.add(loc);
                            MagicBoxPlugin.getInstance().getConfig().set("Locations", locs);
                            MagicBoxPlugin.getInstance().saveConfig();
                            MagicBoxPlugin.reloadLocations();

                            World world = Bukkit.getWorld(loc.split(":")[0]);
                            String[] strings = loc.split(":");
                            Location locx = new Location(world, Double.parseDouble(strings[1]), Double.parseDouble(strings[2]), Double.parseDouble(strings[3]));
                            locx.add(0.5D, 0D, 0.5D);

                            ItemStack it = new ItemStack(Material.GOLD_NUGGET);
                            ItemMeta itemMeta = it.getItemMeta();
                            itemMeta.setDisplayName(new Random(System.nanoTime()) + "");
                            it.setItemMeta(itemMeta);
                            Item i = world.dropItem(locx.add(0,1,0), it);
                            i.setVelocity(new org.bukkit.util.Vector(0D, 0D, 0D));

                            MagicBoxPlugin.notInUseItems.add(i.getUniqueId());

                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aThis chest is now a Magic Box!"));
                        }
                        return true;
                    }
                } else if(args[0].equalsIgnoreCase("removechest")) {
                    Block block = p.getTargetBlock(null, 100);
                    if(block.getType() != Material.CHEST) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNot a check ur looking at lol"));
                        return true;
                    } else {
                        Location l = block.getLocation();
                        List<String> locs = MagicBoxPlugin.getInstance().getConfig().getStringList("Locations");
                        String loc = (l.getWorld().getName() + ":" + l.getX() + ":" + l.getY() + ":" + l.getZ());

                        if(locs.contains(loc)) {
                            locs.remove(loc);
                            MagicBoxPlugin.getInstance().getConfig().set("Locations", locs);
                            MagicBoxPlugin.getInstance().saveConfig();
                            MagicBoxPlugin.reloadLocations();

                            for(Entity entity : l.getWorld().getEntities()) {
                                if(entity instanceof Item) {
                                    if(entity.getLocation().distanceSquared(l) < 1D) {
                                        if(MagicBoxPlugin.notInUseItems.contains(entity.getUniqueId())) {
                                            MagicBoxPlugin.notInUseItems.remove(entity.getUniqueId());
                                            entity.remove();
                                        }
                                    }
                                }
                            }

                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis chest is no longer a Magic Box!"));

                        } else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis chest isn't already a Magic Box."));
                        }
                        return true;
                    }
                } else if(args[0].equalsIgnoreCase("reload")) {
                    MagicBoxPlugin.getInstance().reloadConfig();
                    MagicBoxPlugin.reloadConfigMsgs();
                    return true;
                } else if(args[0].equalsIgnoreCase("go")) {
                    Block b = p.getTargetBlock(null, 100);
                    Location l = b.getLocation();

                    try {
                        Method method = CraftBlock.class.getDeclaredMethod("getNMSBlock");
                        method.setAccessible(true);
                        net.minecraft.server.v1_7_R4.Block block = (net.minecraft.server.v1_7_R4.Block) method.invoke(b);

                        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutBlockAction((int) l.getX(), (int) l.getY(), (int) l.getZ(), block, 1, 1));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return true;
                } else {
                    usage(p);
                    return true;
                }
            } else {
                usage(p);
                return true;
            }
        }
        return false;
    }

    private void usage(Player p)
    {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c/magicbox items"));
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c/magicbox addchest"));
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c/magicbox removechest"));
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c/magicbox removecooldown"));
    }
}
