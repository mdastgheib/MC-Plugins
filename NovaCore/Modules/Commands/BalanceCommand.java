package net.novaprison.Modules.Commands;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import net.novaprison.Core;
import net.novaprison.Modules.SettingsManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class BalanceCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args)
    {
        boolean isPlayer = (cs instanceof Player);
        Player player = null;
        if (cmd.getName().equalsIgnoreCase("balance")) {
            if (isPlayer) {
                player = (Player) cs;
            }
            switch (args.length) {
                case 0: {
                    if(isPlayer) {
                        Essentials essentials = (Essentials) Core.getInstance().getServer().getPluginManager().getPlugin("Essentials");
                        User user = essentials.getUser(player);
                        Double money = user.getMoney().doubleValue();
                        DecimalFormat format = new DecimalFormat("#,###.00");
                        String sx = format.format(money);
                        if(money == 0.0) {
                            sx = "0";
                        }
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', SettingsManager.balance.replace("%money%", sx)));
                    } else {
                        cs.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou are poor. $0 bro."));
                    }
                    break;
                }
                case 1: {
                    if(Bukkit.getPlayerExact(args[0]) != null) {
                        Player other = Bukkit.getPlayerExact(args[0]);

                        Essentials essentials = (Essentials) Core.getInstance().getServer().getPluginManager().getPlugin("Essentials");
                        User user = essentials.getUser(other);
                        Double money = user.getMoney().doubleValue();
                        DecimalFormat format = new DecimalFormat("#,###.00");
                        String sx = format.format(money);
                        if(money == 0.0) {
                            sx = "0";
                        }
                        cs.sendMessage(ChatColor.translateAlternateColorCodes('&', SettingsManager.otherbalance.replace("%money%", sx).replace("%other%", other.getDisplayName())));
                    } else {
                        cs.sendMessage(ChatColor.translateAlternateColorCodes('&', SettingsManager.otherbalance.replace("%other%", SettingsManager.othernotfound.replace("%name%", args[0]))));
                    }
                    break;
                }
                default: {
                    if(isPlayer) {
                        Essentials essentials = (Essentials) Core.getInstance().getServer().getPluginManager().getPlugin("Essentials");
                        User user = essentials.getUser(player);
                        Double money = user.getMoney().doubleValue();
                        DecimalFormat format = new DecimalFormat("#,###.00");
                        String sx = format.format(money);
                        if(money == 0.0) {
                            sx = "0";
                        }
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', SettingsManager.balance.replace("%money%", sx)));
                    } else {
                        cs.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou are poor. $0 bro."));
                    }
                    break;
                }
            }
        }
        return true;
    }
}
