import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlayerListener implements Listener {

    List<String> cmds = new ArrayList<String>();

    @EventHandler
    public void cmd(PlayerCommandPreprocessEvent e)
    {
        Player p = e.getPlayer();
        String msg = e.getMessage();

        String[] args = msg.split(" ");
        String cmd = args[0].toLowerCase().replace("/", "").replace("maxbans:", "");

        if(cmds.isEmpty()) {
            cmds.add("ban");
            cmds.add("mbban");
            cmds.add("tempban");
            cmds.add("mbtempban");
            cmds.add("mute");
            cmds.add("mbmute");
            cmds.add("kick");
            cmds.add("mbkick");
            cmds.add("warn");
            cmds.add("mbwarn");
            cmds.add("tempmute");
            cmds.add("mbtempmute");
        }

        if(cmd.equals("ban") || cmd.equals("mbban")) {
            if(!p.hasPermission("maxbans.ban")) {
                return;
            }
        }

        if(cmd.equals("tempban") || cmd.equals("mbtempban")) {
            if(!p.hasPermission("maxbans.tempban")) {
                return;
            }
        }

        if(cmd.equals("kick") || cmd.equals("mbkick")) {
            if(!p.hasPermission("maxbans.kick")) {
                return;
            }
        }

        if(cmd.equals("mute") || cmd.equals("mbmute")) {
            if(!p.hasPermission("maxbans.mute")) {
                return;
            }
        }

        if(cmd.equals("tempmute") || cmd.equals("mbtempmute")) {
            if(!p.hasPermission("maxbans.tempmute")) {
                return;
            }
        }

        if(cmd.equals("warn") || cmd.equals("mbwarn")) {
            if(!p.hasPermission("maxbans.warn")) {
                return;
            }
        }

        if(!cmds.contains(cmd)) {
            return;
        }

        if(!(args.length > 1)) {
            return;
        }

        String targetName = args[1];
        OfflinePlayer target = Bukkit.getPlayer(targetName);

        String reason = msg.replace(args[0], "").replace(args[1], "");

        if(target.isOnline()) {
            if(((Player)target).hasPermission("")) {
                if(p.hasPermission("novamaxban.overexempt")) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cOverriding users punishment exemption!"));
                    return;
                }
                if(target.getName().equals(p.getName())) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou may not punish yourself."));
                    e.setCancelled(true);
                    return;
                }
                if(cmd.equals("ban") || cmds.equals("mbban")) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou may not ban this player."));
                    notify(p.getName(), target.getName(), "ban", reason);
                } else if(cmd.equals("tempban") || cmd.equals("mbtempban")) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou may not temp-ban this player."));
                    notify(p.getName(), target.getName(), "temp-ban", reason);
                } else if(cmd.equals("kick") || cmd.equals("mbkick")) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou may not kick this player."));
                    notify(p.getName(), target.getName(), "kick", reason);
                } else if(cmd.equals("warn") || cmd.equals("mbwarn")) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou may not warn this player."));
                    notify(p.getName(), target.getName(), "warn", reason);
                } else if(cmd.equals("mute") || cmd.equals("mbmute")) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou may not mute this player."));
                    notify(p.getName(), target.getName(), "mute", reason);
                } else if(cmd.equals("tempmute") || cmd.equals("mbtempmute")) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou may not temp-mute this player."));
                    notify(p.getName(), target.getName(), "temp-mute", reason);
                } else {
                    return;
                }
                e.setCancelled(true);
                return;
            }
        }

        List<String> uuids = Bans.getInstance().getConfig().getStringList("This");

        if(uuids.contains(target.getUniqueId().toString())) {
            if(p.hasPermission("novamaxban.overexempt")) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cOverriding users punishment exemption!"));
                return;
            }
            if(target.getName().equals(p.getName())) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou may not punish yourself."));
                e.setCancelled(true);
                return;
            }
            if(cmd.equals("ban") || cmds.equals("mbban")) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou may not ban this player."));
                notify(p.getName(), target.getName(), "ban", reason);
            } else if(cmd.equals("tempban") || cmd.equals("mbtempban")) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou may not temp-ban this player."));
                notify(p.getName(), target.getName(), "temp-ban", reason);
            } else if(cmd.equals("kick") || cmd.equals("mbkick")) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou may not kick this player."));
                notify(p.getName(), target.getName(), "kick", reason);
            } else if(cmd.equals("warn") || cmd.equals("mbwarn")) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou may not warn this player."));
                notify(p.getName(), target.getName(), "warn", reason);
            } else if(cmd.equals("mute") || cmd.equals("mbmute")) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou may not mute this player."));
                notify(p.getName(), target.getName(), "mute", reason);
            } else if(cmd.equals("tempmute") || cmd.equals("mbtempmute")) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou may not temp-mute this player."));
                notify(p.getName(), target.getName(), "temp-mute", reason);
            } else {
                return;
            }
            e.setCancelled(true);
            return;
        }
    }

    private void notify(String player, String target, String type, String reason)
    {
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(p.hasPermission("novamaxbans.notify")) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e" + player + "&c tried to &e" + type + "&c user &e" + target +
                        " &cfor&e" + reason +  "&c, but was denied!"));
            }
        }
        log(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', "&e" + player + "&c tried to &e" + type + "&c user &e" + target +
                " &cfor&e" + reason +  "&c, but was denied!")));
    }

    private void log(String msg)
    {
        try {
            Date dt = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-DD");
            PrintWriter writer = new PrintWriter(new FileWriter("/plugins/NovaMaxBans/" + df.format(dt) + "-log.txt"));

            SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = df2.format(dt);
            writer.write("[" + time + "] " + msg + "");
            writer.write(System.getProperty("line.separator"));

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void join(PlayerJoinEvent e)
    {
        Player p = e.getPlayer();

        if(!p.hasPermission("novamaxban.exempt")) {
            return;
        }

        List<String> uuids = Bans.getInstance().getConfig().getStringList("This");

        if(!uuids.contains(p.getUniqueId().toString())) {
            uuids.add(p.getUniqueId().toString());
            Bans.getInstance().getConfig().set("This", uuids);
        }
    }

    @EventHandler
    public void quit(PlayerQuitEvent e)
    {
        Player p = e.getPlayer();

        if(!p.hasPermission("novamaxban.exempt")) {
            List<String> uuids = Bans.getInstance().getConfig().getStringList("This");

            if(uuids.contains(p.getUniqueId().toString())) {
                uuids.remove(p.getUniqueId().toString());
                Bans.getInstance().getConfig().set("This", uuids);
            }
            return;
        }

        List<String> uuids = Bans.getInstance().getConfig().getStringList("This");

        if(!uuids.contains(p.getUniqueId().toString())) {
            uuids.add(p.getUniqueId().toString());
            Bans.getInstance().getConfig().set("This", uuids);
        }
    }

    @EventHandler
    public void kick(PlayerKickEvent e)
    {
        Player p = e.getPlayer();

        if(!p.hasPermission("novamaxban.exempt")) {
            List<String> uuids = Bans.getInstance().getConfig().getStringList("This");

            if(uuids.contains(p.getUniqueId().toString())) {
                uuids.remove(p.getUniqueId().toString());
                Bans.getInstance().getConfig().set("This", uuids);
            }
            return;
        }

        List<String> uuids = Bans.getInstance().getConfig().getStringList("This");

        if(!uuids.contains(p.getUniqueId().toString())) {
            uuids.add(p.getUniqueId().toString());
            Bans.getInstance().getConfig().set("This", uuids);
        }
    }
}
