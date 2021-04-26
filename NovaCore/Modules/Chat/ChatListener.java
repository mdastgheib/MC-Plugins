package net.novaprison.Modules.Chat;

import net.minecraft.server.v1_8_R1.*;
import net.novaprison.Core;
import net.novaprison.Modules.ChatFilter.FilterManager;
import net.novaprison.Modules.SettingsManager;
import net.novaprison.Modules.Toggle.ToggleManager;
import net.novaprison.Utils.PacketUtils.NovaSounds;
import net.novaprison.Utils.PacketUtils.NovaTitle;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Set;

public class ChatListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncPlayerChatEvent e)
    {
        if(e.isCancelled())
            return;
        Player player = e.getPlayer();

        if(ChatManager.chat_muted) {
            if(player.hasPermission("nova.chat.mute_bypass")) {
                doChat(player, e.getMessage(), e.getRecipients());
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', SettingsManager.globalmute));

                PacketPlayOutNamedSoundEffect packetPlayOutNamedSoundEffect = new PacketPlayOutNamedSoundEffect("warning.chatismuted",
                        player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 1.0F, 1.0F);
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutNamedSoundEffect);

                String s1 = ChatColor.translateAlternateColorCodes('&', SettingsManager.globalmutetitle);
                String s2 = ChatColor.translateAlternateColorCodes('&', SettingsManager.globalmutesubtitle);
                IChatBaseComponent icbc1 = ChatSerializer.a("{\"text\": \"" + s1 + "\"}");
                IChatBaseComponent icbc2 = ChatSerializer.a("{\"text\": \"" + s2 + "\"}");

                PacketPlayOutTitle length = new PacketPlayOutTitle(10, 50, 5);
                PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE, icbc1);
                PacketPlayOutTitle subtitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, icbc2);

                PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;

                connection.sendPacket(length);
                connection.sendPacket(title);
                connection.sendPacket(subtitle);
            }
        } else {
            doChat(player, e.getMessage(), e.getRecipients());
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e)
    {
        Player player = e.getPlayer();

        ChatManager.update(player, false);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e)
    {
        Player player = e.getPlayer();

        ChatManager.update(player, true);
    }

    @EventHandler
    public void onKick(PlayerKickEvent e)
    {
        Player player = e.getPlayer();

        ChatManager.update(player, true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCmd(PlayerCommandPreprocessEvent e)
    {
        if(e.isCancelled()) {
            return;
        }
        Player player = e.getPlayer();
        String[] cmd = e.getMessage().toLowerCase().replace("/", "").split(" ");
        String command = cmd[0];
        String message = e.getMessage();

        boolean x = false;
        for(String s : Core.getInstance().getConfig().getStringList("Nova.CommaMoneyCmds")) {
            if(s.equalsIgnoreCase(command)) {
                x = true;
                break;
            }
        }
        if(x) {
            for (String s : cmd) {
                if(NumberUtils.isNumber(s)) {
                    try {
                        Long d = Long.parseLong(s);
                        if (d > 1000D) {
                            DecimalFormat format = new DecimalFormat("#,###");
                            String sx = format.format(d);
                            message = message.replace(s, sx);
                        }
                    } catch (Exception ex) {
                        // YOLO
                    }
                }
            }
            e.setMessage(message);
        }
    }

    private void doChat(Player player, String message, Set<Player> re)
    {
        ChatManager.update(player, false);

        if(!player.hasPermission("nova.chat.bypass_delay")) {
            if(!ChatManager.lastChat.containsKey(player.getUniqueId())) {
                ChatManager.lastChat.put(player.getUniqueId(), System.currentTimeMillis());
            } else {
                Long lastChat = ChatManager.lastChat.get(player.getUniqueId());
                if(!((System.currentTimeMillis() - lastChat) > SettingsManager.chatDelay)) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', SettingsManager.chatDelayMsg.replace("%delay%", "" + (SettingsManager.chatDelay / 1000))));
                    return;
                } else {
                    ChatManager.lastChat.remove(player.getUniqueId());
                    ChatManager.lastChat.put(player.getUniqueId(), System.currentTimeMillis());
                }
            }
        }

        String[] msgs = message.split(" ");
        ArrayList<String> names = new ArrayList<String>();
        for(String s : msgs)
        {
            if(NumberUtils.isNumber(s)) {
                try {
                    Long d = Long.parseLong(s);
                    if (d > 1000.0D) {
                        DecimalFormat format = new DecimalFormat("#,###");
                        String sx = format.format(d);
                        message = message.replace(s, "$" + sx);
                    }
                } catch (Exception ex) {
                    if (Bukkit.getPlayerExact(s) != null) {
                        Player p = Bukkit.getPlayerExact(s);
                        if (!names.contains(p.getName())) {
                            names.add(p.getName());
                        }
                    }
                }
            } else if (Bukkit.getPlayerExact(s) != null) {
                Player p = Bukkit.getPlayerExact(s);
                if (!names.contains(p.getName())) {
                    names.add(p.getName());
                }
            }
        }
        if(!names.isEmpty()) {
            if(ChatManager.lastMention.containsKey(player.getUniqueId())) {
                Long lastTime = ChatManager.lastMention.get(player.getUniqueId());
                if((System.currentTimeMillis() - lastTime) <= SettingsManager.mentionDelay) {
                    names.clear();
                } else {
                    ChatManager.lastMention.remove(player.getUniqueId());
                    ChatManager.lastMention.put(player.getUniqueId(), System.currentTimeMillis());
                }
            } else {
                ChatManager.lastMention.put(player.getUniqueId(), System.currentTimeMillis());
            }
        }
        for(String s : names) {
            Player p = Bukkit.getPlayer(s);

            Bukkit.getConsoleSender().sendMessage(s);
            StringBuilder builder = new StringBuilder();
            builder.append(YamlConfiguration.loadConfiguration(new File(Core.getInstance().getDataFolder() + File.separator + "chat.yml")).getString("Nova.MentionColor"));
            builder.append("@");
            builder.append(s);
            builder.append("&7&r");
            message = message.replace(s, (ChatColor.translateAlternateColorCodes('&', builder.toString())));

            String msg = "" + message;

            if (ChatColor.stripColor(msg).length() > 50) {
                msg = ChatColor.translateAlternateColorCodes('&', SettingsManager.mentionsubtitle.replace("%msg%", ChatColor.stripColor(message).substring(0, Math.min(ChatColor.stripColor(message).length(), 50)) + "..."));
            } else {
                msg = ChatColor.translateAlternateColorCodes('&', SettingsManager.mentionsubtitle.replace("%msg%", ChatColor.stripColor(message)));
            }

            NovaTitle.setTime(p, 10, 40, 5);
            NovaTitle.sendTitle(p, SettingsManager.mentiontitle.replace("%player%", p.getName()), msg);

            p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.2F, 1.0F);
        }

        if(!player.hasPermission("nova.chat.bypass_caps")) {
            boolean a = false;
            for (String string : message.split(" ")) {
                if (Bukkit.getPlayerExact(string) != null) {
                    break;
                }
                double uppercase = capitalCount(string);
                if(uppercase / string.length() * 100.0D > SettingsManager.capPercent) {
                    message = message.replace(string, string.toLowerCase());
                    if(!a) {
                        a = true;
                    }
                }
            }
            if(a) {
                NovaSounds.sendSound(player, "warning.caps");
                NovaTitle.setTime(player, 10, 40, 5);
                NovaTitle.sendTitle(player, SettingsManager.capTitle, SettingsManager.capSubtitle);
            }
        }

        String msg = ChatManager.getFormat();
        if(ChatManager.getStaffRank(player) != ChatManager.staffRank.None) {
            msg = msg.replaceAll("<staff>", Core.getInstance().getConfig().getString("Nova.Chat.Staff." + ChatManager.getStaffRank(player)));
            msg = msg.replaceAll("<donate> ", "");
            msg = msg.replaceAll("<donate>", "");
        } else {
            msg = msg.replaceAll("<staff>", "");
        }

        if(ChatManager.getDonatorRank(player) != ChatManager.donatorRank.None) {
            msg = msg.replaceAll("<donate>", Core.getInstance().getConfig().getString("Nova.Chat.Donator." + ChatManager.getDonatorRank(player)));
        } else {
            msg = msg.replaceAll("<donate>", "");
        }

        if(ChatManager.getMineRank(player) != ChatManager.mineRank.Error) {
            msg = msg.replaceAll("<mine>", Core.getInstance().getConfig().getString("Nova.Chat.Mine." + ChatManager.getMineRank(player)));
        } else {
            msg = msg.replaceAll("<mine>", "");
        }

        if(ChatManager.getPrestigeRank(player) != ChatManager.prestigeRank.None) {
            msg = msg.replaceAll("<prestige>", Core.getInstance().getConfig().getString("Nova.Chat.Prestige." + ChatManager.getPrestigeRank(player)));
        } else {
            msg = msg.replaceAll("<prestige>", "");
        }

        PermissionUser user = PermissionsEx.getUser(player);
        String prefix = user.getPrefix();
        String suffix = user.getSuffix();
        if(prefix != null || prefix != "") {
            msg = msg.replaceAll("<prefix>", prefix);
        } else {
            msg = msg.replaceAll("<prefix>", "");
        }

        if(suffix != null || suffix != "") {
            msg = msg.replaceAll("<suffix>", suffix);
        } else {
            msg = msg.replaceAll("<suffix>", "");
        }

        msg = msg.replace("<player>", player.getName());
        msg = msg.replace("<displayname>", player.getDisplayName());
        msg = ChatColor.translateAlternateColorCodes('&', msg);
        msg = msg.replace("<msg>", "");
        String fMsg = message;

        for(String s : FilterManager.words.keySet()) {
            fMsg = fMsg.replaceAll("(?i)" + s, FilterManager.words.get(s));
        }
        if(!fMsg.equals(message)) {
            if(!ToggleManager.swearfilter_toggle.get(player.getUniqueId())) {
                String s1 = ChatColor.translateAlternateColorCodes('&', SettingsManager.swear_message);
                String s2 = ChatColor.translateAlternateColorCodes('&', SettingsManager.swear_messagesub);

                IChatBaseComponent icbc1 = ChatSerializer.a("{\"text\": \"" + s1 + "\"}");
                IChatBaseComponent icbc2 = ChatSerializer.a("{\"text\": \"" + s2 + "\"}");

                PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
                PacketPlayOutTitle length = new PacketPlayOutTitle(10, 40, 5);
                PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE, icbc1);
                PacketPlayOutTitle subtitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, icbc2);
                connection.sendPacket(length);
                connection.sendPacket(title);
                connection.sendPacket(subtitle);

                PacketPlayOutNamedSoundEffect packetPlayOutNamedSoundEffect = new PacketPlayOutNamedSoundEffect("warning.swear",
                        player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 1.0F, 1.0F);
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutNamedSoundEffect);
            }
        }
        if(player.hasPermission("Nova.ChatColor")) {
            message = ChatColor.translateAlternateColorCodes('&', message);
            fMsg = ChatColor.translateAlternateColorCodes('&', fMsg);
        }
        String filteredMessage = msg + fMsg;
        msg = msg + message;

        for(Player p : re) {
            if(ToggleManager.swearfilter_toggle.get(p.getUniqueId())) {
                p.sendMessage(msg);
            } else {
                p.sendMessage(filteredMessage);
            }
        }

        if(!msg.equals(filteredMessage)) {
            Bukkit.getConsoleSender().sendMessage("[Swear] " + msg);
        } else {
            Bukkit.getConsoleSender().sendMessage(msg);
        }
    }

    private static int capitalCount(String s)
    {
        char ch;
        int upper = 0;
        for(int i = 0; i < s.length(); i++) {
            ch = s.charAt(i);
            if((Character.isAlphabetic(ch)) && (Character.isUpperCase(ch))) {
                upper++;
            }
        }
        return upper;
    }
}
