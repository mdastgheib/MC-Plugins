package net.novaprison.Modules.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Base command layout
 */
public class BaseCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args)
    {
        boolean isPlayer = (cs instanceof Player);
        Player player = null;
        if (cmd.getName().equalsIgnoreCase("base")) {
            if (isPlayer) {
                player = (Player) cs;
            }
            switch (args.length) {
                case 0: {

                }
                default: {

                }
            }
        }
        return false;
    }
}
