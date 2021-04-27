package com.novaprison.meow.commands;

import com.novaprison.meow.Meow;
import com.novaprison.meow.util.LegitMeowUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CmdMinebomb implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("minebomb")) {
            if (!(cs instanceof Player) && !(cs instanceof ConsoleCommandSender)) {
                
                return true;
            }
            if (!cs.hasPermission("minebomb.admin")) {
                cs.sendMessage(ChatColor.translateAlternateColorCodes('&', Meow.pluginInstance.getConfig().getString("Meow.prefix") + " " + Meow.pluginInstance.getConfig().getString("Meow.nopermmsg")));
                
                return true;
            }

            if (args.length == 0) {
                getUsage(cs, cmd);
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    Meow.pluginInstance.reloadConfig();
                    cs.sendMessage(ChatColor.translateAlternateColorCodes('&', Meow.pluginInstance.getConfig().getString("Meow.prefix") + " " + Meow.pluginInstance.getConfig().getString("Meow.reloadconfigmsg")));
                } else {
                    getUsage(cs, cmd);
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("addregion")) {
                    List<String> r = Meow.pluginInstance.getConfig().getStringList("Meow.regions");
                    r.add(args[1]);
                    Meow.pluginInstance.getConfig().set("Meow.regions", r);
                    Meow.pluginInstance.saveConfig();

                    cs.sendMessage(ChatColor.translateAlternateColorCodes('&', Meow.pluginInstance.getConfig().getString("Meow.prefix") + " " + Meow.pluginInstance.getConfig().getString("Meow.addregionmsg").replace("%region%", args[1])));
                } else if (args[0].equalsIgnoreCase("removeregion")) {
                    List<String> r = Meow.pluginInstance.getConfig().getStringList("Meow.regions");
                    r.remove(args[1]);
                    Meow.pluginInstance.getConfig().set("Meow.regions", r);
                    Meow.pluginInstance.saveConfig();

                    cs.sendMessage(ChatColor.translateAlternateColorCodes('&', Meow.pluginInstance.getConfig().getString("Meow.prefix") + " " + Meow.pluginInstance.getConfig().getString("Meow.removeregionmsg").replace("%region%", args[1])));
                } else {
                    getUsage(cs, cmd);
                }
            } else if (args.length == 4) {
                if (args[0].equalsIgnoreCase("give")) {
                    int amount = 0;
                    int power = 0;
                    try {
                        amount = Integer.parseInt(args[2]);
                        power = Integer.parseInt(args[3]);
                    } catch (NumberFormatException ex) {
                        getUsageGive(cs);
                        return true;
                    }
                    Player targetPlayer = Bukkit.getPlayer(args[1]);
                    if (targetPlayer != null) {
                        LegitMeowUtil.giveMinebomb(amount, power, targetPlayer);
                    } else if (args[1].equals("*") || args[1].equals("**")) {
                        for (World w : Bukkit.getWorlds()) {
                            List<Player> worldPlayers = w.getPlayers();
                            LegitMeowUtil.giveMinebomb(amount, power, worldPlayers.toArray(new Player[worldPlayers.size()]));
                        }
                    } else {
                        getUsageGive(cs);
                    }
                } else {
                    getUsage(cs, cmd);
                }
            } else {
                getUsage(cs, cmd);
            }
            return true;
        }
        return false;
    }

    private static void getUsage(CommandSender sender, Command cmd) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /" + cmd.getName() + " addregion [region name]"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /" + cmd.getName() + " removeregion [region name]"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage:  /" + cmd.getName() + " give [player / *] [amount] [power]"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /" + cmd.getName() + " reload"));
    }

    private static void getUsageGive(CommandSender cs) {
        cs.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage:  /mb give [player / *] [amount] [power]"));
    }
}
