package net.novaprison.Utils.PacketUtils;

import net.minecraft.server.v1_8_R1.PacketPlayOutNamedSoundEffect;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NovaSounds {

    public static void sendSound(Player player, String sound, Float a, Float b, Boolean global)
    {
        if(global) {
            for(Player p : player.getLocation().getWorld().getPlayers()) {
                PacketPlayOutNamedSoundEffect packetPlayOutNamedSoundEffect = new PacketPlayOutNamedSoundEffect(sound,
                        player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), a, b);
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packetPlayOutNamedSoundEffect);
            }
        } else {
            PacketPlayOutNamedSoundEffect packetPlayOutNamedSoundEffect = new PacketPlayOutNamedSoundEffect(sound,
                    player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), a, b);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutNamedSoundEffect);
        }
    }
    public static void sendSound(Player player, String sound, Float a, Float b) {
        PacketPlayOutNamedSoundEffect packetPlayOutNamedSoundEffect = new PacketPlayOutNamedSoundEffect(sound,
                player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), a, b);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutNamedSoundEffect);
    }
    public static void sendSound(Player player, String sound) {
        PacketPlayOutNamedSoundEffect packetPlayOutNamedSoundEffect = new PacketPlayOutNamedSoundEffect(sound,
                player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 1.0F, 1.0F);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutNamedSoundEffect);
    }
}
