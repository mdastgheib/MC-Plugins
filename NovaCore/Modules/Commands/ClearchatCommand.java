package net.novaprison.Modules.Commands;

import net.novaprison.Modules.SettingsManager;
import net.novaprison.Utils.PacketUtils.NovaSounds;
import net.novaprison.Utils.PacketUtils.NovaTitle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearchatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        boolean isPlayer = (cs instanceof Player);
        Player player;
        if (cmd.getName().equalsIgnoreCase("clearchat")) {
            if (isPlayer) {
                player = (Player) cs;
                if (!player.hasPermission("nova.clearchat")) {
                    return true;
                }
            }
            switch (args.length) {
                case 0: {
                    StringBuilder b = new StringBuilder();

                    //50x this length message should do, right?
                    for(int i = 0; i < 50; i++) {
                        b.append("                                  ");
                    }
                    String c = b.toString();

                    for(World world : Bukkit.getWorlds()) {
                        for(Player p : world.getPlayers()) {
                            if(!p.hasPermission("nova.chat.bypass_clear")) {
                                p.sendMessage(c);
                            }
                            NovaSounds.sendSound(p, "warning.chatclear", 1.0F, 1.0F, false);
                            NovaTitle.setTime(p, 10, 40, 20);
                            NovaTitle.sendTitle(p, SettingsManager.clearchattitle.replace("%player%", cs.getName()), SettingsManager.clearchatsubtitle.replace("%player%", cs.getName()));
                            for(String s : SettingsManager.clearchatmsg) {
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', s.replace("%player%", cs.getName())));
                            }
                        }
                    }
                }
                default: {

                }
            }
        }
        return false;
    }
}
