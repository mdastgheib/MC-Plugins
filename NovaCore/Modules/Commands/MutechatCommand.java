package net.novaprison.Modules.Commands;

import net.minecraft.server.v1_8_R1.*;
import net.novaprison.Modules.Chat.ChatManager;
import net.novaprison.Modules.SettingsManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class MutechatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        boolean isPlayer = (cs instanceof Player);
        Player player;
        if (cmd.getName().equalsIgnoreCase("mutechat")) {
            if(isPlayer) {
                player = (Player) cs;
                if(!player.hasPermission("nova.mutechat")) {
                    return true;
                }
            }
            switch (args.length) {
                case 0: {
                    if(ChatManager.chat_muted) {
                        // turn chat mute on , turn off
                        ChatManager.chat_muted = false;
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', SettingsManager.globalmuteoff.replace("%player%", cs.getName())));

                        String s1 = ChatColor.translateAlternateColorCodes('&', SettingsManager.globalunmutetitle);
                        String s2 = ChatColor.translateAlternateColorCodes('&', SettingsManager.globalunmutesubtitle);
                        IChatBaseComponent icbc1 = ChatSerializer.a("{\"text\": \"" + s1 + "\"}");
                        IChatBaseComponent icbc2 = ChatSerializer.a("{\"text\": \"" + s2 + "\"}");

                        PacketPlayOutTitle length = new PacketPlayOutTitle(10, 50, 5);
                        PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE, icbc1);
                        PacketPlayOutTitle subtitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, icbc2);
                        for(World world : Bukkit.getWorlds()) {
                            for(Player playerx : world.getPlayers()) {
                                PlayerConnection connection = ((CraftPlayer) playerx).getHandle().playerConnection;

                                connection.sendPacket(length);
                                connection.sendPacket(title);
                                connection.sendPacket(subtitle);

                                PacketPlayOutNamedSoundEffect packetPlayOutNamedSoundEffect = new PacketPlayOutNamedSoundEffect("warning.chatunmute",
                                        playerx.getLocation().getX(), playerx.getLocation().getY(), playerx.getLocation().getZ(), 1.0F, 1.0F);
                                ((CraftPlayer) playerx).getHandle().playerConnection.sendPacket(packetPlayOutNamedSoundEffect);
                            }
                        }
                    } else {
                        // turn chat mute off , turn on
                        ChatManager.chat_muted = true;
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', SettingsManager.globalmuteon.replace("%player%", cs.getName())));

                        String s1 = ChatColor.translateAlternateColorCodes('&', SettingsManager.globalmutetitle);
                        String s2 = ChatColor.translateAlternateColorCodes('&', SettingsManager.globalmutesubtitle);
                        IChatBaseComponent icbc1 = ChatSerializer.a("{\"text\": \"" + s1 + "\"}");
                        IChatBaseComponent icbc2 = ChatSerializer.a("{\"text\": \"" + s2 + "\"}");

                        PacketPlayOutTitle length = new PacketPlayOutTitle(10, 50, 5);
                        PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE, icbc1);
                        PacketPlayOutTitle subtitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, icbc2);
                        for(World world : Bukkit.getWorlds()) {
                            for(Player playerx : world.getPlayers()) {
                                PlayerConnection connection = ((CraftPlayer) playerx).getHandle().playerConnection;

                                connection.sendPacket(length);
                                connection.sendPacket(title);
                                connection.sendPacket(subtitle);

                                PacketPlayOutNamedSoundEffect packetPlayOutNamedSoundEffect = new PacketPlayOutNamedSoundEffect("warning.chatmute",
                                        playerx.getLocation().getX(), playerx.getLocation().getY(), playerx.getLocation().getZ(), 1.0F, 1.0F);
                                ((CraftPlayer) playerx).getHandle().playerConnection.sendPacket(packetPlayOutNamedSoundEffect);
                            }
                        }
                    }
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
        cs.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c/mutechat"));
    }
}
