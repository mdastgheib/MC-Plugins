package com.nova.novascoreboards;

import com.nova.novascoreboards.utils.DebugManager;
import com.nova.novascoreboards.utils.TPSManager;
import com.nova.novascoreboards.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static com.nova.novascoreboards.NovaScoreboards.getInstance;

public class CommandListener implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equals("novascoreboards")) {
            if (args.length > 0) {
                String strCommand = args[0];
                if (strCommand.equalsIgnoreCase("on")) {
                    if (args.length == 1) {
                        if (sender instanceof Player) {
                            Player player = (Player) sender;
                            if (getInstance().scoreboardIgnorers.contains(player.getUniqueId())) {
                                getInstance().scoreboardIgnorers.remove(player.getUniqueId());
                                sender.sendMessage(ChatColor.GOLD + "You enabled the scoreboard.");

                                player.setScoreboard(getInstance().setScoreboard(player));
                            } else {
                                sender.sendMessage(ChatColor.RED + "You already have scoreboards toggled on!");
                            }
                        } else {
                            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use that command.");
                        }
                    } else {
                        sender.sendMessage(ChatColor.GOLD + getInstance().getDescription().getFullName() + "NovaUniverse");
                    }
                } else if (strCommand.equalsIgnoreCase("off")) {
                    if (args.length == 1) {
                        if (sender instanceof Player) {
                            Player player = (Player) sender;
                            if (!getInstance().scoreboardIgnorers.contains(player.getUniqueId())) {
                                getInstance().scoreboardIgnorers.add(player.getUniqueId());
                                player.setScoreboard(player.getServer().getScoreboardManager().getNewScoreboard());
                                sender.sendMessage(ChatColor.GOLD + "You disabled the scoreboard.");
                            } else {
                                sender.sendMessage(ChatColor.RED + "You already have scoreboards toggled off!");
                            }
                        } else {
                            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use that command.");
                        }
                    } else {
                        sender.sendMessage(ChatColor.GOLD + getInstance().getDescription().getFullName() + "NovaUniverse");
                    }
                } else if (strCommand.equalsIgnoreCase("reload")) {
                    if (args.length == 1) {
                        if (sender.isOp()) {
                            getInstance().getServer().getScheduler().cancelTasks(getInstance());
                            getInstance().reloadConfig();
                            getInstance().reloadStatsConfig();
                            getInstance().loadConfiguration();
                            getInstance().getServer().getScheduler().runTaskTimer(getInstance(), new TPSManager(), 40L, 1L);
                            getInstance().startTask();

                            sender.sendMessage(ChatColor.GOLD + "NovaScoreboards reloaded.");
                        } else {
                            sender.sendMessage(ChatColor.DARK_RED + "You do not have access to that command.");
                        }
                    } else {
                        sender.sendMessage(ChatColor.GOLD + getInstance().getDescription().getFullName() + "NovaUniverse");
                    }
                } else if (strCommand.equalsIgnoreCase("add")) {
                    if (sender.isOp()) {
                        if (args.length > 1) {
                            StringBuilder keyBuilder = new StringBuilder();
                            for (int i = 1; i < args.length; i++) {
                                if (i == args.length - 1) keyBuilder.append(args[i]);
                                else keyBuilder.append(args[i]).append(" ");
                            }
                            String strKey = keyBuilder.toString();
                            if (strKey.isEmpty()) {
                                sender.sendMessage(ChatColor.RED + "You cannot have an empty scoreboard key!");
                            } else {
                                List<String> currentKeys = getInstance().getSettings().getScoreboardKeys();
                                currentKeys.add(ChatColor.translateAlternateColorCodes('&', strKey));
                                getInstance().getConfig().set("Scoreboard keys", currentKeys);
                                getInstance().saveConfig();
                                getInstance().getSettings().setScoreboardKeys(currentKeys);

                                sender.sendMessage(ChatColor.GOLD + "Successfully added '" + ChatColor.translateAlternateColorCodes('&', strKey) + ChatColor.RESET.toString() + ChatColor.GOLD + "' to the scoreboard keys.");
                            }
                        } else {
                            sender.sendMessage(ChatColor.GOLD + getInstance().getDescription().getFullName() + "NovaUniverse");
                        }
                    } else {
                        sender.sendMessage(ChatColor.DARK_RED + "You do not have access to that command.");
                    }
                } else if (strCommand.equalsIgnoreCase("remove")) {
                    if (sender.isOp()) {
                        if (args.length == 2) {
                            if (Utils.isInteger(args[1])) {
                                int removeIndex = Integer.parseInt(args[1]);
                                List<String> currentKeys = getInstance().getSettings().getScoreboardKeys();
                                if (removeIndex >= 0 && removeIndex < currentKeys.size()) {
                                    try {
                                        String strKey = currentKeys.get(removeIndex);
                                        currentKeys.remove(removeIndex);
                                        getInstance().getConfig().set("Scoreboard keys", currentKeys);
                                        getInstance().saveConfig();
                                        getInstance().getSettings().setScoreboardKeys(currentKeys);

                                        sender.sendMessage(ChatColor.GOLD + "Successfully removed '" + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', strKey) + ChatColor.RESET.toString() + ChatColor.GOLD + "'.");
                                    } catch (Exception ex) {
                                        sender.sendMessage(ChatColor.RED + "Invalid scoreboard index: " + removeIndex);
                                    }
                                } else {
                                    sender.sendMessage(ChatColor.RED + "Invalid scoreboard index: " + removeIndex);
                                }
                            } else {
                                sender.sendMessage(ChatColor.RED + "Please type a valid integer for the Scoreboard index.");
                            }
                        } else {
                            sender.sendMessage(ChatColor.GOLD + getInstance().getDescription().getFullName() + "NovaUniverse");
                        }
                    } else {
                        sender.sendMessage(ChatColor.DARK_RED + "You do not have access to that command.");
                    }
                } else if (strCommand.equalsIgnoreCase("debug")) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        if (DebugManager.toggleDebugger(player.getUniqueId())) {
                            player.sendMessage(ChatColor.GOLD + "You are now debugging " + NovaScoreboards.getInstance().getDescription().getFullName() + "!");
                        } else {
                            player.sendMessage(ChatColor.GOLD + "You are no longer debugging " + NovaScoreboards.getInstance().getDescription().getName());
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "You must be a player to use that command.");
                    }
                } else {
                    sender.sendMessage(ChatColor.GOLD + getInstance().getDescription().getFullName() + "NovaUniverse");
                }
            } else {
                sender.sendMessage(ChatColor.GOLD + getInstance().getDescription().getFullName() + "NovaUniverse");
            }
            return true;
        }
        return false;
    }
}
