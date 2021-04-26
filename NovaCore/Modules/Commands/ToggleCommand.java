package net.novaprison.Modules.Commands;

import net.novaprison.Modules.Toggle.ToggleManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ToggleCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        boolean isPlayer = (cs instanceof Player);
        Player player;
        if (cmd.getName().equalsIgnoreCase("toggle")) {
            if (!isPlayer) {
                cs.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cConsole cannot manage toggles."));
                return true;
            }
            player = (Player) cs;
            UUID uuid = player.getUniqueId();
            switch (args.length) {
                case 0: {
                    ToggleManager.openMenu(player);
                    return true;
                }
                case 1: {
                    String a = args[0];
                    if(a.equalsIgnoreCase("swearing") || a.equalsIgnoreCase("swears") || a.equalsIgnoreCase("swear")) {
                        if(ToggleManager.swearfilter_toggle.containsKey(player.getUniqueId())) {
                            if(ToggleManager.swearfilter_toggle.get(uuid)) {
                                ToggleManager.swearfilter_toggle.remove(uuid);
                                ToggleManager.swearfilter_toggle.put(uuid, false);
                                player.sendMessage("Turned (seeing) swearing off!");
                            } else {
                                ToggleManager.swearfilter_toggle.remove(uuid);
                                ToggleManager.swearfilter_toggle.put(uuid, true);
                                player.sendMessage("Turned (seeing) swearing on!");
                            }
                        } else {
                            ToggleManager.swearfilter_toggle.put(player.getUniqueId(), true);
                            player.sendMessage("Turned (seeing) swearing on!");
                        }
                    } else if(a.equalsIgnoreCase("join") || a.equalsIgnoreCase("joinmessages")) {

                    } else if(a.equalsIgnoreCase("announcements") || a.equalsIgnoreCase("announce")) {
                        if(ToggleManager.announce_toggle.containsKey(player.getUniqueId())) {
                            if(ToggleManager.announce_toggle.get(uuid)) {
                                ToggleManager.announce_toggle.remove(uuid);
                                ToggleManager.announce_toggle.put(uuid, false);
                                player.sendMessage("Turned announcements off!");
                            } else {
                                ToggleManager.announce_toggle.remove(uuid);
                                ToggleManager.announce_toggle.put(uuid, true);
                                player.sendMessage("Turned announcements on!");
                            }
                        } else {
                            ToggleManager.announce_toggle.put(player.getUniqueId(), true);
                            player.sendMessage("Turned announcements on!");
                        }
                    } else {
                        ToggleManager.openMenu(player);
                    }
                    return true;
                }
                default: {
                    ToggleManager.openMenu(player);
                    return true;
                }
            }
        }
        return false;
    }
}