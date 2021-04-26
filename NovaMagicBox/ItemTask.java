import net.minecraft.server.v1_7_R4.PacketPlayOutBlockAction;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_7_R4.block.CraftBlock;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class ItemTask {

    private Location location;
    private int step = 0;
    private ArrayList<ItemStack> itemStacks;
    private int delay = 0;
    private Item lastItem = null;
    private ItemStack rewardItem = null;
    private Item item = null;
    private Player player;
    private String loc;

    public ItemTask(Location location, ArrayList<ItemStack> itemStacks, int delayInTicks, Player p, String loc)
    {
        this.location = location;
        this.itemStacks = itemStacks;
        this.step = 0;
        this.delay = delayInTicks;
        this.player = p;
        this.loc = loc;
    }

    public ItemTask start()
    {
        for(Entity entity : location.getWorld().getEntities()) {
            if(entity instanceof Item) {
                if(entity.getLocation().distanceSquared(location) < 2D) {
                    if(MagicBoxPlugin.notInUseItems.contains(entity.getUniqueId())) {
                        MagicBoxPlugin.notInUseItems.remove(entity.getUniqueId());
                        entity.remove();
                    }
                }
            }
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                if(itemStacks.isEmpty()) {
                    HashMap<Integer, ItemStack> items = player.getInventory().addItem(rewardItem);
                    for(Integer i : items.keySet()) {
                        player.getWorld().dropItemNaturally(player.getLocation(), items.get(i));
                    }
                    lastItem.getWorld().playSound(lastItem.getLocation(), Sound.NOTE_PIANO, 1.3F, 1.3F);
                    ParticleEffect.FLAME.display(lastItem.getLocation(), 0.3F, 0.3F, 0.3F, 0.1F, 10);
                    ParticleEffect.SMOKE.display(lastItem.getLocation(), 0.3F, 0.3F, 0.3F, 0.1F,10);
                    final UUID uuid = item.getUniqueId();
                    final Location locx = lastItem.getLocation();
                    item.setItemStack(rewardItem);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            MagicBoxPlugin.getNoPickupItems().remove(item.getUniqueId());
                            item.remove();
                            ParticleEffect.FLAME.display(locx, 0.3F, 0.3F, 0.3F, 0.1F, 10);
                            ParticleEffect.SMOKE.display(locx, 0.3F, 0.3F, 0.3F, 0.1F,10);
                            MagicBoxPlugin.inUse.remove(loc);

                            World world = Bukkit.getWorld(loc.split(":")[0]);
                            String[] strings = loc.split(":");
                            Location loc = new Location(world, Double.parseDouble(strings[1]), Double.parseDouble(strings[2]), Double.parseDouble(strings[3]));
                            loc.add(0.5D, 0D, 0.5D);

                            ItemStack it = new ItemStack(Material.GOLD_NUGGET);
                            ItemMeta itemMeta = it.getItemMeta();
                            itemMeta.setDisplayName(new Random(System.nanoTime()) + "");
                            it.setItemMeta(itemMeta);
                            Item i = world.dropItem(loc.add(0,1,0), it);
                            i.setVelocity(new org.bukkit.util.Vector(0D, 0D, 0D));

                            MagicBoxPlugin.notInUseItems.add(i.getUniqueId());

                            Block b = player.getTargetBlock(null, 100);
                            Location l = location.clone();

                            for(Player p : l.getWorld().getPlayers()) {
                                try {
                                    Method method = CraftBlock.class.getDeclaredMethod("getNMSBlock");
                                    method.setAccessible(true);
                                    net.minecraft.server.v1_7_R4.Block block = (net.minecraft.server.v1_7_R4.Block) method.invoke(b);

                                    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutBlockAction((int) l.getX(), (int) l.getY(), (int) l.getZ(), block, 1, 0));
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }.runTaskLater(MagicBoxPlugin.getInstance(), 100L);
                    this.cancel();
                    return;
                }
                if(step == 0) {
                    Random r = new Random();
                    int i = r.nextInt(itemStacks.size());
                    ItemStack reward = itemStacks.get(i);
                    itemStacks.remove(i);

                    rewardItem = reward;

                    try {
                        item = location.getWorld().dropItem(location, itemStacks.get(0));
                        MagicBoxPlugin.getNoPickupItems().add(item.getUniqueId());
                        item.setVelocity(new Vector(0D, 0D, 0D));
                    } catch (NullPointerException ex) {

                    }

                    Block b = player.getTargetBlock(null, 100);
                    Location l = b.getLocation();
                    location = l;

                    for(Player p : l.getWorld().getPlayers()) {
                        try {
                            Method method = CraftBlock.class.getDeclaredMethod("getNMSBlock");
                            method.setAccessible(true);
                            net.minecraft.server.v1_7_R4.Block block = (net.minecraft.server.v1_7_R4.Block) method.invoke(b);

                            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutBlockAction((int) l.getX(), (int) l.getY(), (int) l.getZ(), block, 1, 1));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                step++;

                ItemStack i = itemStacks.get(0).clone();
                Random r = new Random();
                i.getItemMeta().setDisplayName(r.nextInt(9999) + "");
                item.setItemStack(i);
                lastItem = item;
                itemStacks.remove(0);

                ParticleEffect.FLAME.display(lastItem.getLocation(), 0.1F, 0.1F, 0.1F, 0.03F, 5);
                ParticleEffect.SMOKE.display(lastItem.getLocation(), 0.1F, 0.1F, 0.1F, 0.03F, 5);
                lastItem.getWorld().playSound(lastItem.getLocation(), Sound.NOTE_PLING, 1.0F, 1.0F);
            }
        }.runTaskTimer(MagicBoxPlugin.getInstance(), 0L, delay);
        return this;
    }

    public static ItemTask start(Location location, ArrayList<ItemStack> itemStacks, int delay, Player player, String loc)
    {
        return new ItemTask(location, itemStacks, delay, player, loc).start();
    }
}
