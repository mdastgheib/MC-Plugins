package net.novaprison.Modules.Commands;

import net.novaprison.Modules.ChatFilter.FilterManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FilterCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args)
    {
        boolean isPlayer = (cs instanceof Player);
        Player player;
        if(cmd.getName().equalsIgnoreCase("chatfilter")) {
            if(isPlayer) {
                player = (Player) cs;
                if(!player.hasPermission("novafilter.admin")) {
                    return true;
                }
            }
            switch (args.length) {
                case 0: {
                    usage(cs);
                    return true;
                }
                case 1: {
                    if(args[0].equalsIgnoreCase("list")) {
                        cs.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&l = Filtered words ="));
                        StringBuilder s = new StringBuilder();
                        for(String k : FilterManager.words.keySet()) {
                            String k2 = FilterManager.words.get(k);
                            s.append(ChatColor.translateAlternateColorCodes('&',
                                    "&9&l(&c" + k + "&7 : " + "&a" + k2 + "&9&l)&7, "));
                        }
                        cs.sendMessage(s.toString());
                        return true;
                    }
                    usage(cs);
                    return true;
                }
                case 2: {
                    if(args[0].equalsIgnoreCase("add")) {
                        if(FilterManager.words.containsKey(args[1].toLowerCase())) {
                            cs.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + args[1].toLowerCase() + " &cis already" +
                                    " added to the filter!"));
                            return true;
                        }
                        StringBuilder s = new StringBuilder();
                        for(int i = 0; i < args[1].length();i++) {
                            s.append("*");
                        }
                        FilterManager.addToFilter(args[1].toLowerCase(), s.toString());
                        cs.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + args[1].toLowerCase() + " &cwas added" +
                                " to the filter!"));
                        return true;
                    } else if(args[0].equalsIgnoreCase("remove")) {
                        if(!FilterManager.words.containsKey(args[1].toLowerCase())) {
                            cs.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + args[1].toLowerCase() + " &cis not" +
                                    " in the filter!"));
                            return true;
                        }

                        FilterManager.removeFromFilter(args[1].toLowerCase());
                        cs.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + args[1].toLowerCase() + " &cwas removed" +
                                " to the filter!"));
                        return true;
                    }
                    usage(cs);
                    return true;
                }
                case 3: {
                    if(args[0].equalsIgnoreCase("add")) {
                        if(FilterManager.words.containsKey(args[1].toLowerCase())) {
                            cs.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + args[1].toLowerCase() + " &cis already" +
                                    " added to the filter!"));
                            return true;
                        }

                        FilterManager.addToFilter(args[1].toLowerCase(), args[2]);
                        cs.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + args[1] + " &cwas added" +
                                " to the filter with the replacement &a" + args[2] + "&c!"));
                        return true;
                    }
                    usage(cs);
                    return true;
                }
                default: {
                    usage(cs);
                    return true;
                }
            }
        }
        return false;
    }

    private void usage(CommandSender cs)
    {
        cs.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c/filter list"));
        cs.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c/filter add [word] (replacement)"));
        cs.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c/filter remove [word]"));
    }
}
