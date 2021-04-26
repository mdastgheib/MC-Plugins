package net.novaprison.Modules.Commands;

import net.minecraft.server.v1_8_R1.EnumParticle;
import net.minecraft.server.v1_8_R1.PacketPlayOutWorldParticles;
import net.novaprison.Modules.Blood.BloodManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class BloodCommand implements CommandExecutor {

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args)
    {
        boolean isPlayer = (cs instanceof Player);
        Player player;
        if (cmd.getName().equalsIgnoreCase("blood")) {
            if (isPlayer) {
                player = (Player) cs;
            } else {
                return true;
            }
            switch (args.length) {
                case 0: {
                    return true;
                }
                case 1: {
                    if(args[0].equalsIgnoreCase("hi")) {
                        Location loc = player.getLocation();
                        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.BLOCK_CRACK, true,
                                (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), 0.5F, 0.5F, 0.5F, 0.1F, 100, 35, 5);
                        (((CraftPlayer)player).getHandle().playerConnection).sendPacket(packet);
                        return true;
                    }
                    try {
                        BloodManager.Blood blood = BloodManager.Blood.valueOf(args[0].toLowerCase());
                        BloodManager.bloodData.remove(player.getUniqueId());
                        BloodManager.bloodData.put(player.getUniqueId(), blood);
                    } catch(Exception ex) {

                    }
                    return true;
                }
                default: {
                    return true;
                }
            }
        }
        return false;
    }
}