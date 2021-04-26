package net.novaprison.effects;
import net.novaprison.Utils.SLAPI;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Effects extends JavaPlugin implements Listener {

    private static Plugin instance;

    private static HashMap<String, List<String>> xblockA;
    private static HashMap<String, ItemStack> xblockB;

    private static HashMap<Block, List<PotionEffect>> blockA =  new HashMap<Block, List<PotionEffect>>();
    private static HashMap<Block, ItemStack> blockB = new HashMap<Block, ItemStack>();

    private static HashMap<UUID, ItemStack> temp = new HashMap<UUID, ItemStack>();
    private static HashMap<UUID, PotionEffect> temp2 = new HashMap<UUID, PotionEffect>();
    private static ArrayList<UUID> temp3 = new ArrayList<UUID>();

    @Override
    public void onEnable()
    {
        instance = this;
        instance.getServer().getPluginManager().registerEvents(this, this);

        try {
            xblockA = SLAPI.load("plugins/NovaEffects/data-A.bin");
        } catch(Exception ex) {
            xblockA = new HashMap<String, List<String>>();
        }
        for(String s : xblockA.keySet()) {
            Location l = deserialize(s);
            l.getChunk().load();

            List<PotionEffect> p = new ArrayList<PotionEffect>();
            for (String g : xblockA.get(s)) {
                p.add(deserializePotion(g));
            }
            blockA.put(deserialize(s).getBlock(), p);
        }

        try {
            xblockB = SLAPI.load("plugins/NovaEffects/data-B.bin");
        } catch(Exception ex) {
            xblockB = new HashMap<String, ItemStack>();
        }
        for (String s : xblockB.keySet()) {
            Location l = deserialize(s);
            l.getChunk().load();
            blockB.put(deserialize(s).getBlock(), xblockB.get(deserialize(s)));
        }

        /*
        for(final Block b : blockB.keySet()) {
            b.getLocation().getChunk().load();

            final Item item = b.getWorld().dropItem(b.getLocation().add(+0.5D, 2D, +0.5D), blockB.get(b));

            new BukkitRunnable() {
                @Override
                public void run() {
                    item.teleport(b.getLocation().add(+0.5D, 2D, +0.5D));
                }
            }.runTaskLater(this, 20L);
        }
        */

        xblockA.clear();
        xblockB.clear();
    }

    @Override
    public void onDisable()
    {
        /*
        for(Block b : blockB.keySet()) {
            for(Entity e : b.getWorld().getEntities()) {
                if(e.getLocation().distance(b.getLocation().add(+0.5D, 2D, +0.5D)) < 1D) {
                    e.remove();
                }
            }
        }
        */

        for(Block b : blockA.keySet()) {
            List<String> s = new ArrayList<String>();
            for(PotionEffect p : blockA.get(b)) {
                s.add(serializePotion(p));
            }
            xblockA.put(serialize(b.getLocation()), s);
        }
        try {
            SLAPI.save(xblockA, "plugins/NovaEffects/data-A.bin");
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        for(Block b : blockB.keySet()) {
            xblockB.put(serialize(b.getLocation()), blockB.get(b));
        }
        try {
            SLAPI.save(xblockB, "plugins/NovaEffects/data-B.bin");
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        instance = null;
    }

    public static Plugin getInstance()
    {
        return instance;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e)
    {
        Block below = e.getPlayer().getLocation().add(0,-1D,0).getBlock();
        if(!blockA.containsKey(below)) {
            return;
        }
        for(PotionEffect f : blockA.get(below)) {
            if(e.getPlayer().hasPotionEffect(f.getType())) {
                e.getPlayer().removePotionEffect(f.getType());
            }
        }
        e.getPlayer().addPotionEffects(blockA.get(below));
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e)
    {
        if(blockB.containsValue(e.getItem().getItemStack())) {
            e.setCancelled(true);
            e.getItem().setPickupDelay(Integer.MAX_VALUE);
        } else if(e.getItem().getItemStack().getItemMeta().getDisplayName().contains("*^57G6gV%6&DB^DuguGYBuy7g")) {
            e.setCancelled(true);
            e.getItem().setPickupDelay(Integer.MAX_VALUE);
        }
    }

    @EventHandler
    public void onHopperPickup(InventoryPickupItemEvent e)
    {
        if(blockB.containsValue(e.getItem().getItemStack())) {
            e.setCancelled(true);
            e.getItem().setPickupDelay(Integer.MAX_VALUE);
        } else if(e.getItem().getItemStack().getItemMeta().getDisplayName().contains("*^57G6gV%6&DB^DuguGYBuy7g")) {
            e.setCancelled(true);
            e.getItem().setPickupDelay(Integer.MAX_VALUE);
        }
    }

    @EventHandler
    public void onDespawn(ItemDespawnEvent e)
    {
        if(blockB.containsValue(e.getEntity().getItemStack())) {
            e.setCancelled(true);
            e.getEntity().setPickupDelay(Integer.MAX_VALUE);
        } else if(e.getEntity().getItemStack().hasItemMeta()) {
            if (e.getEntity().getItemStack().getItemMeta().getDisplayName().contains("*^57G6gV%6&DB^DuguGYBuy7g")) {
                e.setCancelled(true);
                e.getEntity().setPickupDelay(Integer.MAX_VALUE);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e)
    {
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if(!e.getPlayer().hasPermission("novaeffect.admin")) {
            return;
        }
        if(temp3.contains(e.getPlayer().getUniqueId())) {
            temp3.remove(e.getPlayer().getUniqueId());
            try {
                if (blockA.containsKey(e.getClickedBlock())) {
                    blockA.remove(e.getClickedBlock());
                    Item i = null;
                    for(World world : Bukkit.getWorlds()) {
                        for(Entity entity : world.getEntities()) {
                            blockB.get(e.getClickedBlock());
                            if(entity.getLocation().distance(e.getClickedBlock().getLocation().add(+0.5D, 2D, +0.5D)) < 0.5D) {
                                i = (Item) entity;
                                break;
                            }
                        }
                    }
                    if(i != null) {
                        i.remove();
                    }
                    blockB.remove(e.getClickedBlock());
                } else {
                    e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis was not a potion effect block!"));
                }
            } catch (Exception ex) {
                //lol
            }
            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aRemoved potion effect from this block!"));
            return;
        }
        if(temp2.containsKey(e.getPlayer().getUniqueId())) {
            PotionEffect f = temp2.get(e.getPlayer().getUniqueId());
            Block b = e.getClickedBlock();

            if(blockA.containsKey(b)) {
                blockA.get(b).add(f);
            } else {
                blockA.put(b, new ArrayList<PotionEffect>());
                blockA.get(b).add(f);
            }
            temp2.remove(e.getPlayer().getUniqueId());
            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aAdded potion effect to this block!"));
            return;
        } else if(temp.containsKey(e.getPlayer().getUniqueId())) {
            ItemStack i = temp.get(e.getPlayer().getUniqueId());
            final Block b = e.getClickedBlock();
            if(!blockA.containsKey(b)) {
                temp.remove(e.getPlayer().getUniqueId());
                e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis was not a potion effect block!"));
                return;
            }
            if(blockB.containsKey(b)) {
                blockB.remove(b);
            }
            ItemStack itemst = i.clone();
            ItemMeta meta = itemst.getItemMeta();
            meta.setDisplayName("" + new Random().nextInt(Integer.MAX_VALUE));
            itemst.setItemMeta(meta);
            final Item item = b.getWorld().dropItem(b.getLocation().add(+0.5D, 2D, +0.5D), itemst);

            new BukkitRunnable() {
                @Override
                public void run() {
                    item.teleport(b.getLocation().add(+0.5D, 2D, +0.5D));
                }
            }.runTaskLater(this, 20L);

            blockB.put(b, item.getItemStack());
            temp.remove(e.getPlayer().getUniqueId());
            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSpawned item on potion block"));
        }
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args)
    {
        boolean isPlayer = (cs instanceof Player);
        Player player;
        if(cmd.getName().equalsIgnoreCase("novaeffect")) {
            if(!isPlayer) {
                cs.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou must be a player to do this!"));
                return true;
            }
            player = (Player) cs;

            if(!player.hasPermission("novaeffects.admin")) {
                cs.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou do not have permission to do this."));
                return true;
            }

            switch(args.length) {
                case 0: {
                    usage(player);
                    return true;
                }
                case 1: {
                    if (args[0].equalsIgnoreCase("hi")) {
                        for(World world : Bukkit.getWorlds()) {
                            for(Entity e : world.getEntities()) {
                                if(e instanceof Item) {
                                    System.out.println("---------");
                                    System.out.println(((Item) e).getItemStack().getType().toString());
                                    System.out.println(e.getUniqueId().toString());
                                    System.out.println("---------");
                                }
                            }
                        }
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("remove")) {
                        if (!temp3.contains(player.getUniqueId())) {
                            temp3.add(player.getUniqueId());
                        }
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aClick a potion effect block to remove it's effect!"));
                        return true;
                    }
                    try {
                        String[] x = null;
                        if (args[0].contains(":")) {
                            x = args[0].split(":");
                        }

                        if(x != null && x.length == 2) {
                            ItemStack itemst = new ItemStack(Material.getMaterial(Integer.parseInt(x[0])));
                            itemst.setDurability((short) Integer.parseInt(x[1]));
                            ItemMeta meta = itemst.getItemMeta();
                            meta.setDisplayName("" + new Random().nextInt(Integer.MAX_VALUE));
                            itemst.setItemMeta(meta);

                            temp.put(player.getUniqueId(), itemst);
                        } else {
                            ItemStack itemst = new ItemStack(Material.getMaterial(Integer.parseInt(args[0])));
                            ItemMeta meta = itemst.getItemMeta();
                            meta.setDisplayName("*^57G6gV%6&DB^DuguGYBuy7g " + new Random().nextInt(Integer.MAX_VALUE));
                            itemst.setItemMeta(meta);

                            temp.put(player.getUniqueId(), itemst);
                        }
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aClick a block to put down the item!"));
                        return true;
                    } catch (NumberFormatException ex) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + args[0] + " is not a number!"));
                    }
                    return true;
                }
                case 2: {
                    if(temp2.containsKey(player.getUniqueId())) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou've already got a potion effect ready to apply to a block"));
                        return true;
                    }
                    try {
                        PotionEffectType t = PotionEffectType.getByName(args[0].toUpperCase());
                        String[] a = args[1].split(":");
                        int time = Integer.parseInt(a[0]);
                        int power = Integer.parseInt(a[1]);
                        PotionEffect p = new PotionEffect(t, time*20, power-1);

                        temp2.put(player.getUniqueId(), p);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aNow click a block to apply the potion effect!"));
                    } catch(Exception ex) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cError -"));
                        usage(player);
                    }
                    return true;
                }
                default: {
                    usage(player);
                    return true;
                }
            }
        }

        return false;
    }

    public void usage(Player p)
    {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c/novaeffect [potion effect] [time:power]"));
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cEg. /novaeffect speed 5:2"));
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c/novaeffect [item ID]"));
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c/novaeffect remove"));
    }

    public String serialize(Location loc) {
        return loc.getWorld().getName() + ":" + loc.getX() + ":" + loc.getY() + ":" + loc.getZ();
    }

    public Location deserialize(String s) {
        String[] sx = s.split(":");

        World w = Bukkit.getServer().getWorld(sx[0]);
        return new Location(w, Double.parseDouble(sx[1]), Double.parseDouble(sx[2]), Double.parseDouble(sx[3]));
    }

    public String serializePotion(PotionEffect p) {
        return p.getType().getName() + ":" + p.getDuration() + ":" + p.getAmplifier();
    }

    public PotionEffect deserializePotion(String s) {
        String[] sx = s.split(":");

        return new PotionEffect(PotionEffectType.getByName(sx[0].toUpperCase()), Integer.parseInt(sx[1]), Integer.parseInt(sx[2]));
    }
}
