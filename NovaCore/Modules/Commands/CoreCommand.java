package net.novaprison.Modules.Commands;

import net.minecraft.server.v1_8_R1.MinecraftServer;
import net.novaprison.Modules.Chat.ChatManager;
import net.novaprison.Utils.Slack;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CoreCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args)
    {
        boolean isPlayer = (cs instanceof Player);
        if(cmd.getName().equalsIgnoreCase("core")) {
            if(args.length == 0) {
                return true;
            } else if(args.length == 1) {
                if(args[0].equalsIgnoreCase("debug")) {
                    for(UUID uuid : ChatManager.mineRanks.keySet()) {
                        cs.sendMessage(uuid.toString());
                        cs.sendMessage(ChatManager.mineRanks.get(uuid).toString());
                    }
                    for(UUID uuid : ChatManager.staffRanks.keySet()) {
                        cs.sendMessage(uuid.toString());
                        cs.sendMessage(ChatManager.staffRanks.get(uuid).toString());
                    }
                    for(UUID uuid : ChatManager.donatorRanks.keySet()) {
                        cs.sendMessage(uuid.toString());
                        cs.sendMessage(ChatManager.donatorRanks.get(uuid).toString());
                    }
                    for(UUID uuid : ChatManager.prestigeRanks.keySet()) {
                        cs.sendMessage(uuid.toString());
                        cs.sendMessage(ChatManager.prestigeRanks.get(uuid).toString());
                    }
                    return true;
                } else if(args[0].equalsIgnoreCase("rl")) {
                    ChatManager.update((Player)cs, false);
                    return true;
                } else if(args[0].equalsIgnoreCase("tps")) {
                    Slack.send("TPS Below 19.20 (tps: " + MinecraftServer.getServer().recentTps[0] + ")", "NovaCore", Slack.statusURL, "Hack");
                    return true;
                }
                return true;
            } else {
                return true;
            }
        }
        return false;
    }
}
