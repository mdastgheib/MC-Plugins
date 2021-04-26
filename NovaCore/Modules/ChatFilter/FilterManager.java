package net.novaprison.Modules.ChatFilter;

import net.novaprison.Core;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class FilterManager {

    public static HashMap<String, String> words = new HashMap<String, String>();

    public static void setupFilter()
    {
        File file = new File(getCore().getDataFolder() + File.separator + "filter.yml");
        boolean setup = false;
        if(!file.exists()) {
            try {
                file.createNewFile();
                setup = true;
            } catch(IOException ex) {
                ex.printStackTrace();
            }
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        if(setup) {
            config.addDefault("FilteredWords", Arrays.asList(new String[]{"fuck", "cunt"}));
            config.addDefault("Filter.fuck", "fork");
            config.addDefault("Filter.cunt", "count");

            config.options().copyDefaults(true);
        }
        try {
            config.save(file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        for(String s : config.getStringList("FilteredWords")) {
            String r = config.getString("Filter." + s);
            words.put(s, r);
        }
    }

    public static void addToFilter(String word, String replacement)
    {
        File file = new File(getCore().getDataFolder() + File.separator + "filter.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        words.put(word, replacement);
        config.set("Filter." + word, replacement);
        List<String> words = config.getStringList("FilteredWords");
        words.add(word);
        config.set("FilteredWords", words);
        try {
            config.save(file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void removeFromFilter(String word)
    {
        File file = new File(getCore().getDataFolder() + File.separator + "filter.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        if(words.containsKey(word)) {
            words.remove(word);
        }
        config.set("Filter." + word, null);
        List<String> words = config.getStringList("FilteredWords");
        words.remove(word);
        config.set("FilteredWords", words);
        try {
            config.save(file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /*
        Core and Config variables
     */
    private static Core getCore()
    {
        return (Core) Core.getInstance();
    }
    private static FileConfiguration getConfig()
    {
        return getCore().getConfig();
    }

    /*
        Destroy static variables
     */
    public static void destroy(Core core)
    {
        words.clear();
        words = null;
    }
}
