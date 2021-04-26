import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.UserDoesNotExistException;
import net.minecraft.server.v1_7_R4.PacketPlayOutBlockAction;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_7_R4.block.CraftBlock;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import org.spigotmc.ProtocolInjector;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void pickup(PlayerPickupItemEvent e)
    {
        if(e.isCancelled()) {
            return;
        }
        Player p = e.getPlayer();
        Item i = e.getItem();
        UUID itemUUID = e.getItem().getUniqueId();

        if(MagicBoxPlugin.noPickupItem(itemUUID) || MagicBoxPlugin.notInUseItems.contains(itemUUID)) {
            e.setCancelled(true);
            return;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void interact(PlayerInteractEvent e)
    {
        if(e.isCancelled()) {
            return;
        }
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if(e.getClickedBlock().getType() != Material.CHEST) {
            return;
        }
        Location l = e.getClickedBlock().getLocation();
        String loc = (l.getWorld().getName() + ":" + l.getX() + ":" + l.getY() + ":" + l.getZ());
        if(!MagicBoxPlugin.chestLocations.contains(loc)) {
            return;
        }
        if(MagicBoxPlugin.inUse.contains(loc)) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', MagicBoxPlugin.msg_inuse));
            return;
        }
        e.setCancelled(true);
        double money = 0D;
        try {
            money = Economy.getMoney(e.getPlayer().getName());
        } catch (UserDoesNotExistException ex) {
            ex.printStackTrace();
        }
        Long cost = MagicBoxPlugin.getInstance().getConfig().getLong("Cost");

        if(cost > money) {
            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', MagicBoxPlugin.msg_nomoney.replaceAll("%money%", cost + "")));
            e.setCancelled(true);
            return;
        }

        if(MagicBoxPlugin.cooldown.containsKey(e.getPlayer().getUniqueId())) {
            if(MagicBoxPlugin.cooldown.get(e.getPlayer().getUniqueId()) +
                    (MagicBoxPlugin.getInstance().getConfig().getInt("Cooldown") * 1000L) < System.currentTimeMillis()) {
                ArrayList<ItemStack> items = new ArrayList<ItemStack>();
                for(ItemStack i : MagicBoxPlugin.inv.getContents()) {
                    if(i != null) {
                        items.add(i);
                    }
                }

                Collections.shuffle(items, new Random(System.nanoTime()));

                ItemTask.start(e.getClickedBlock().getLocation().add(0.5D, 1.5D, 0.5D), items, 7, e.getPlayer(), loc);
                MagicBoxPlugin.cooldown.put(e.getPlayer().getUniqueId(), System.currentTimeMillis());
                try {
                    Economy.substract(e.getPlayer().getName(), BigDecimal.valueOf(Double.parseDouble(cost.toString())));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                MagicBoxPlugin.inUse.add(loc);
            } else {
                Long time = (MagicBoxPlugin.cooldown.get(e.getPlayer().getUniqueId()) +
                        (MagicBoxPlugin.getInstance().getConfig().getInt("Cooldown") * 1000L) - System.currentTimeMillis()) / 1000;
                e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', MagicBoxPlugin.msg_cooldown.replaceAll("%time%", MagicBoxPlugin.time(Integer.parseInt(time + "")))));
                return;
            }
        } else {
            ArrayList<ItemStack> items = new ArrayList<ItemStack>();
            for(ItemStack i : MagicBoxPlugin.inv.getContents()) {
                if(i != null) {
                    items.add(i);
                }
            }

            Collections.shuffle(items, new Random(System.nanoTime()));

            ItemTask.start(e.getClickedBlock().getLocation().add(0.5D, 1.5D, 0.5D), items, 7, e.getPlayer(), loc);
            MagicBoxPlugin.cooldown.put(e.getPlayer().getUniqueId(), System.currentTimeMillis());
            try {
                Economy.substract(e.getPlayer().getName(), BigDecimal.valueOf(Double.parseDouble(cost.toString())));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            MagicBoxPlugin.inUse.add(loc);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void despawn(ItemDespawnEvent e)
    {
        UUID uuid = e.getEntity().getUniqueId();
        if(MagicBoxPlugin.getNoPickupItems().contains(uuid)) {
            e.setCancelled(true);
            return;
        }
        if(MagicBoxPlugin.notInUseItems.contains(uuid)) {
            e.setCancelled(true);
            return;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent e)
    {
        if(e.isCancelled()) {
            return;
        }

        if(e.getBlock().getType() != Material.CHEST) {
            return;
        }

        Player p = e.getPlayer();
        Location l = e.getBlock().getLocation();
        List<String> locs = MagicBoxPlugin.getInstance().getConfig().getStringList("Locations");
        String loc = (l.getWorld().getName() + ":" + l.getX() + ":" + l.getY() + ":" + l.getZ());
        if(locs.contains(loc)) {
            e.setCancelled(true);
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lThis is a magic box! You may NOT break it without removing it as a magic box!"));
            return;
        }
    }
}
