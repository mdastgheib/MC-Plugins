package net.novaprison.Modules.Toggle;

import net.novaprison.Core;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import java.io.File;
import java.util.UUID;

public class ToggleListener implements Listener {

    @EventHandler
    public void onInvClick(InventoryClickEvent e)
    {
        Player player = Bukkit.getPlayer(e.getWhoClicked().getUniqueId());
        UUID uuid = player.getUniqueId();
        if(ChatColor.stripColor(e.getInventory().getName()).equalsIgnoreCase(ChatColor.stripColor(ToggleManager.invName))) {
            e.setCancelled(true);
            if(!e.getClickedInventory().getType().equals(InventoryType.CHEST)) {
                return;
            }
            int slot = e.getSlot() + 1;
            File file = new File(Core.getInstance().getDataFolder() + File.separator + "togglemenu.yml");
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);

            if(config.isSet("ToggleInv." + slot + ".isSet")) {
                if (config.getString("ToggleInv." + slot + ".type").equalsIgnoreCase("toggle")) {
                    String toggle = config.getString("ToggleInv." + slot + ".toggle");
                    if (toggle.equalsIgnoreCase("swear")) {
                        if (ToggleManager.swearfilter_toggle.get(uuid)) {
                            ToggleManager.swearfilter_toggle.remove(uuid);
                            ToggleManager.swearfilter_toggle.put(uuid, false);
                        } else {
                            ToggleManager.swearfilter_toggle.remove(uuid);
                            ToggleManager.swearfilter_toggle.put(uuid, true);
                        }
                        player.closeInventory();
                        ToggleManager.openMenu(player);
                    } else if (toggle.equalsIgnoreCase("blood")) {
                        if (ToggleManager.blood_toggle.get(uuid)) {
                            ToggleManager.blood_toggle.remove(uuid);
                            ToggleManager.blood_toggle.put(uuid, false);
                        } else {
                            ToggleManager.blood_toggle.remove(uuid);
                            ToggleManager.blood_toggle.put(uuid, true);
                        }
                        player.closeInventory();
                        ToggleManager.openMenu(player);
                    } else if (toggle.equalsIgnoreCase("announce")) {
                        if (ToggleManager.announce_toggle.get(uuid)) {
                            ToggleManager.announce_toggle.remove(uuid);
                            ToggleManager.announce_toggle.put(uuid, false);
                        } else {
                            ToggleManager.announce_toggle.remove(uuid);
                            ToggleManager.announce_toggle.put(uuid, true);
                        }
                        player.closeInventory();
                        ToggleManager.openMenu(player);
                    }
                }
            }
        }
    }
}
