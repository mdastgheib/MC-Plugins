package me.nova.novarepair;
import me.nova.novarepair.utils.HoloAPI;
import me.nova.novarepair.utils.LocationUtil;
import org.bukkit.GameMode;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListener implements Listener {
    private NovaRepair plugin = null;

    public EventListener(NovaRepair pluginInstance) {
        this.plugin = pluginInstance;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        try {
            if (event.isCancelled()) return;
            if (event.getAction() == Action.LEFT_CLICK_BLOCK && event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                if (event.getClickedBlock() != null && event.getItem() != null && event.getItem().getType().toString().toLowerCase().endsWith("pickaxe")) {
                    if (!this.getPlugin().isIgnorer(event.getPlayer())) {
                        if (event.getItem().getType().getMaxDurability() - event.getItem().getDurability() <= this.getPlugin().getRepairDurability()) {
                            event.setCancelled(true);
                            event.getPlayer().updateInventory();
                            if (this.getPlugin().repairo.contains(event.getPlayer().getUniqueId())) {
                                CommandListener.repairPickaxe(event.getPlayer(), false);
                                if (event.getPlayer().getItemInHand() == null || (event.getPlayer().getItemInHand().getType().getMaxDurability() - event.getPlayer().getItemInHand().getDurability() > this.getPlugin().getRepairDurability())) {
                                    return;
                                }
                            }
                            if (this.getPlugin().messageDelays.containsKey(event.getPlayer().getUniqueId())) {
                                long messageTime = this.getPlugin().messageDelays.get(event.getPlayer().getUniqueId());
                                if (System.currentTimeMillis() - messageTime > this.getPlugin().getMessageDelay() * 1000)
                                    this.getPlugin().messageDelays.remove(event.getPlayer().getUniqueId());
                            }
                            if (!this.getPlugin().messageDelays.containsKey(event.getPlayer().getUniqueId())) {
                                if (this.getPlugin().canWarn(1))
                                    Lang.sendMessage(event.getPlayer(), Lang.REPAIR_REQUIRED, String.format("%.1f", this.getPlugin().getCost(event.getItem().containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS) ? event.getItem().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) : 0) - NovaRepair.getEconomy().getBalance(event.getPlayer())));
                                if (this.getPlugin().canWarn(2))
                                    HoloAPI.createHologram(event.getPlayer(), LocationUtil.getLocationInfront(event.getPlayer()), this.getPlugin().getHologramMessages());
                                if (this.getPlugin().canWarn(1) || this.getPlugin().canWarn(2))
                                    this.getPlugin().messageDelays.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        try {
            this.getPlugin().messageDelays.remove(event.getPlayer().getUniqueId());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected NovaRepair getPlugin() {
        return this.plugin;
    }
}
