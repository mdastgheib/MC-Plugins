package me.nova.novadrops.commands;

import me.nova.novadrops.NovaDrops;
import me.nova.novadrops.util.DebugManager;
import me.nova.novadrops.util.MessageUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class NovaDropsCommand implements CommandExecutor {
    private NovaDrops plugin;

    public NovaDropsCommand(NovaDrops plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (commandLabel.equalsIgnoreCase("novadrops")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("This command can only be run by a player!");
                return true;
            }
            Player p = (Player) sender;
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    if (p.hasPermission("novadrops.admin")) {
                        this.plugin.reloadConfig();
                        this.plugin.loadConfig();

                        p.sendMessage(MessageUtil.replace(this.plugin.getConfig().getString("messages.reloadedconfig")));
                    } else {
                        p.sendMessage(MessageUtil.replace(plugin.getConfig().getString("messages.nopermission")));
                    }
                } else if (args[0].equalsIgnoreCase("debug")) {
                    if (p.hasPermission("novadrops.admin") || this.plugin.getDescription().getAuthors().contains(p.getName())) {
                        if (DebugManager.toggleDebugger(p.getUniqueId()))
                            p.sendMessage(ChatColor.AQUA + "You are now debugging NovaDrops.");
                        else p.sendMessage(ChatColor.AQUA + "You are no longer debugging NovaDrops.");
                    } else {
                        p.sendMessage(MessageUtil.replace(plugin.getConfig().getString("messages.nopermission")));
                    }
                } else {
                    if (p.hasPermission("novadrops.admin")) {
                        List<String> msgs = plugin.getConfig().getStringList("messages.help");
                        for (String msg : msgs) {
                            p.sendMessage(MessageUtil.replace(msg));
                        }
                    } else {
                        p.sendMessage(MessageUtil.replace(plugin.getConfig().getString("messages.nopermission")));
                    }
                }
            } else {
                if (p.hasPermission("novadrops.admin")) {
                    List<String> msgs = plugin.getConfig().getStringList("messages.help");
                    for (String msg : msgs) {
                        p.sendMessage(MessageUtil.replace(msg));
                    }
                } else {
                    p.sendMessage(MessageUtil.replace(plugin.getConfig().getString("messages.nopermission")));
                }
            }
            return true;
        }
        return false;
    }
}
