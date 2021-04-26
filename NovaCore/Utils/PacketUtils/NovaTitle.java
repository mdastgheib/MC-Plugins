package net.novaprison.Utils.PacketUtils;

import net.minecraft.server.v1_8_R1.*;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NovaTitle {

    public static void sendTitle(Player player, String title, String subtitle)
    {
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;

        IChatBaseComponent icbc1 = ChatSerializer.a("{\"text\": \"" + ChatColor.translateAlternateColorCodes('&', title) + "\"}");
        IChatBaseComponent icbc2 = ChatSerializer.a("{\"text\": \"" + ChatColor.translateAlternateColorCodes('&', subtitle) + "\"}");

        PacketPlayOutTitle t = new PacketPlayOutTitle(EnumTitleAction.TITLE, icbc1);
        PacketPlayOutTitle s = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, icbc2);

        connection.sendPacket(t);
        connection.sendPacket(s);
    }

    public static void setTime(Player player, int in, int time, int fade)
    {
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;

        PacketPlayOutTitle length = new PacketPlayOutTitle(in, time, fade);

        connection.sendPacket(length);
    }
}
