package net.novaprison.Modules.AntiInvis;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffectType;

public class AntiInvisListener implements Listener {

    @EventHandler
    public void onDrink(PlayerInteractEvent event)
    {
        Action action = event.getAction();
        Player player = event.getPlayer();
        if ((action == Action.RIGHT_CLICK_AIR) || (action == Action.RIGHT_CLICK_BLOCK))
        {
            ItemStack it = player.getItemInHand();
            if ((it.getType() == Material.POTION) && (it.getDurability() != 0))
            {
                Potion potion = Potion.fromItemStack(it);
                PotionEffectType effecttype = potion.getType().getEffectType();
                if (effecttype == PotionEffectType.INVISIBILITY)
                {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + ChatColor.translateAlternateColorCodes('&', "&cYou are not allowed to do that!"));
                    player.setItemInHand(new ItemStack(Material.COOKIE, 1));
                }
            }
        }
    }

    @EventHandler
    public void onDispense(BlockDispenseEvent event)
    {
        ItemStack it = event.getItem();
        Material mat = it.getType();
        if (mat == Material.POTION)
        {
            Potion potion = Potion.fromItemStack(it);
            PotionEffectType effecttype = potion.getType().getEffectType();
            if (effecttype == PotionEffectType.INVISIBILITY) {
                event.setCancelled(true);
            }
        }
    }
}
