package net.novaprison.Modules.Announcer;

import net.novaprison.Core;
import net.novaprison.Events.ServerSecondEvent;
import net.novaprison.Modules.Toggle.ToggleManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Announcer implements Listener {

    public static int toAnnounce;
    public static int counter;
    public static int message;

    public static List<String> header;
    public static List<String> footer;

    public static void setup()
    {
        File file = new File(Core.getInstance().getDataFolder() + File.separator + "announcements.yml");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch(IOException ex) {
                ex.printStackTrace();
            }
        }
        FileConfiguration c = YamlConfiguration.loadConfiguration(file);
        if(c.isSet("AutomaticCountdown")) {
            toAnnounce = c.getInt("AutomaticCountdown");
        } else {
            c.set("AutomaticCountdown", 180);
        }
        ArrayList<String> a = new ArrayList<String>();
        a.add("Hi!");
        a.add("HellO!");
        c.addDefault("Announcements.1", a);

        ArrayList<String> b = new ArrayList<String>();
        a.add("header yo");
        a.add("sup");
        c.addDefault("Header", b);
        ArrayList<String> x = new ArrayList<String>();
        a.add("footer yo");
        a.add("sup");
        c.addDefault("Footer", x);

        c.options().copyDefaults(true);
        try {
            c.save(file);
        } catch(IOException ex) {
            ex.printStackTrace();
        }

        header = c.getStringList("Header");
        footer = c.getStringList("Footer");
    }

    @EventHandler
    public void onSecond(ServerSecondEvent e)
    {
        if(!(counter == toAnnounce)) {
            counter++;
            return;
        }
        counter = 0;
        message++;

        FileConfiguration c = YamlConfiguration.loadConfiguration(new File(Core.getInstance().getDataFolder() + File.separator + "announcements.yml"));
        if(!c.isSet("Announcements." + message)) {
            message = 1;
        }
        List<String> msgs = c.getStringList("Announcements." + message);
        for(World world : Bukkit.getWorlds()) {
            for(Player player : world.getPlayers()) {
                if(ToggleManager.announce_toggle.get(player.getUniqueId())) {
                    for(String s : header) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
                    }
                    for(String s : msgs) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
                    }
                    for(String s : footer) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
                    }
                }
            }
        }
    }
}
