package net.novaprison.Modules.Commands;

import net.novaprison.Core;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;

public class AnnouncementCommand implements CommandExecutor {

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args)
    {
        boolean isPlayer = (cs instanceof Player);
        Player player = null;
        if (cmd.getName().equalsIgnoreCase("announcement")) {
            if (isPlayer) {
                player = (Player) cs;
                if(!player.hasPermission("nova.announcecmd")) {
                    return true;
                }
            }
            switch (args.length) {
                case 2: {
                    if(args[0].equalsIgnoreCase("send") || args[0].equalsIgnoreCase("force") ||
                            args[0].equalsIgnoreCase("run")) {
                        try {
                            Integer i = Integer.parseInt(args[1]);

                            FileConfiguration c = YamlConfiguration.loadConfiguration(new File(Core.getInstance().getDataFolder() + File.separator + "announcements.yml"));
                            if(!c.isSet("Announcements." + i)) {
                                //Message not set
                                return true;
                            }

                            List<String> header = c.getStringList("Header");
                            List<String> footer = c.getStringList("Footer");

                            List<String> msgs = c.getStringList("Announcements." + i);
                            for(World world : Bukkit.getWorlds()) {
                                for(Player p : world.getPlayers()) {
                                    //if(ToggleManager.announce_toggle.get(player.getUniqueId())) {
                                        for(String s : header) {
                                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
                                        }
                                        for(String s : msgs) {
                                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
                                        }
                                        for(String s : footer) {
                                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
                                        }
                                    //}
                                }
                            }
                        } catch(Exception ex) {
                            //Usage
                        }
                    }
                }
                default: {
                    //Usage
                }
            }
        }
        return false;
    }
}
