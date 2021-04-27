package me.nova.novadrops.commands;

import me.nova.novadrops.NovaDrops;
import me.nova.novadrops.util.MessageUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class DropsCommand implements CommandExecutor {
    private NovaDrops plugin;

    public DropsCommand(NovaDrops plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (commandLabel.equalsIgnoreCase("drops")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("This command can only be run by a player!");
                return true;
            }
            Player p = (Player) sender;
            if (p.hasPermission("novadrops.drops")) {
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("on")) {
                        if (!NovaDrops.playerDrops.contains(p.getUniqueId())) {
                            NovaDrops.playerDrops.add(p.getUniqueId());
                            p.sendMessage(MessageUtil.replace(this.plugin.getConfig().getString("messages.dropson")));
                        } else {
                            p.sendMessage(MessageUtil.replace(this.plugin.getConfig().getString("messages.dropsison")));
                        }
                    } else if (args[0].equalsIgnoreCase("off")) {
                        if (NovaDrops.playerDrops.contains(p.getUniqueId())) {
                            NovaDrops.playerDrops.remove(p.getUniqueId());
                            p.sendMessage(MessageUtil.replace(this.plugin.getConfig().getString("messages.dropsoff")));
                        } else {
                            p.sendMessage(MessageUtil.replace(this.plugin.getConfig().getString("messages.dropsisoff")));
                        }
                    } else {
                        List<String> msgs = this.plugin.getConfig().getStringList("messages.help-drops");
                        for (String msg : msgs) {
                            p.sendMessage(MessageUtil.replace(msg));
                        }
                    }
                } else {
                    List<String> msgs = this.plugin.getConfig().getStringList("messages.help-drops");
                    for (String msg : msgs) {
                        p.sendMessage(MessageUtil.replace(msg));
                    }
                }
            } else {
                p.sendMessage(MessageUtil.replace(this.plugin.getConfig().getString("messages.nopermission")));
            }
            return true;
        }
        return false;
    }
}