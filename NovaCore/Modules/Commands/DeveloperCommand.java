package net.novaprison.Modules.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeveloperCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args)
    {
        boolean isPlayer = (cs instanceof Player);
        if(cmd.getName().equalsIgnoreCase("developer")) {
            Player player = (Player) cs;
            if(!(player.isOp() || player.getUniqueId().toString().replace("-", "").equals("bf48a0472e5d4f58acc3adc04f9a4167"))) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&1&l&n==================="));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e  This servers developer is &aXx_iMurder_xX"));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&1&l&n==================="));
            } else {
                //dev cmds, probably just debug shit.
                return true;
            }
        }
        return false;
    }
}