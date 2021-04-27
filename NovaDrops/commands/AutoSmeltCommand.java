package me.nova.novadrops.commands;

import me.nova.novadrops.NovaDrops;
import me.nova.novadrops.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class AutoSmeltCommand implements CommandExecutor {
    private NovaDrops plugin;

    public AutoSmeltCommand(NovaDrops plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (commandLabel.equalsIgnoreCase("autosmelt")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("This command can only be run by a player!");
                return true;
            }
            Player p = (Player) sender;
            if (p.hasPermission("novadrops.autosmelt")) {
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("on")) {
                        if (!NovaDrops.playerSmelt.contains(p.getUniqueId())) {
                            NovaDrops.playerSmelt.add(p.getUniqueId());
                            p.sendMessage(MessageUtil.replace(this.plugin.getConfig().getString("messages.smelton")));
                        } else {
                            p.sendMessage(MessageUtil.replace(this.plugin.getConfig().getString("messages.smeltison")));
                        }
                    } else if (args[0].equalsIgnoreCase("off")) {
                        if (NovaDrops.playerSmelt.contains(p.getUniqueId())) {
                            NovaDrops.playerSmelt.remove(p.getUniqueId());
                            p.sendMessage(MessageUtil.replace(plugin.getConfig().getString("messages.smeltoff")));
                        } else {
                            p.sendMessage(MessageUtil.replace(this.plugin.getConfig().getString("messages.smeltisoff")));
                        }
                    }
                } else {
                    List<String> msgs = this.plugin.getConfig().getStringList("messages.help-smelt");
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