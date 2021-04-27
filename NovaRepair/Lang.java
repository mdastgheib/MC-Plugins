package me.nova.novarepair;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public enum Lang {
    COMMAND_NO_PERMISSIONS("Command.No permissions", "&4You do not have access to that command."),
    COMMAND_REPAIR_CREATIVE("Command.Creative repair", "&cYou must be in survival mode to repair your pickaxe."),
    COMMAND_TOGGLE_ON_TOGGLED("Command.Toggle.On.Toggled", "&6You will now receive the repair warning."),
    COMMAND_TOGGLE_ON_ALREADY_NOT_IGNORER("Command.Toggle.On.Already not ignorer", "&cYou are already able to receive the repair warning!"),
    COMMAND_TOGGLE_OFF_TOGGLED("Command.Toggle.Off.Toggled", "&6You are now ignoring the repair warning."),
    COMMAND_TOGGLE_OFF_ALREADY_IGNORER("Command.Toggle.Off.Already ignorer", "&cYou have already ignored the repair warning!"),
    COMMAND_TOGGLE_AUTO_ON_TOGGLED("Command.Toggle.Auto.On", "&6You are now automatically paying for a repair."),
    COMMAND_TOGGLE_AUTO_OFF_TOGGLED("Command.Toggle.Auto.Off", "&6You are no longer automatically paying for a repair."),
    REPAIR_REPAIRED("Repair.Repaired", "&6Your pickaxe has been repaired."),
    REPAIR_NOT_PICKAXE("Repair.Not pickaxe", "&cThe item you are holding must be a pickaxe!"), REPAIR_NOT_DAMAGED("Repair.Not damaged", "&cYour pickaxe is not damaged enough to be repaired!"),
    REPAIR_NOT_ENOUGH_MONEY("Repair.Not enough money", "&cYou require $%s more to repair this pickaxe."),
    REPAIR_IGNORER("Repair.Ignorer", "&cYou cannot repair your pickaxe whilst you are ignoring the warning."),
    REPAIR_REQUIRED("Repair.Required", "&6Your pickaxe is close to breaking! Type /novarepair to repair it for $%s, or type /novarepair off to ignore this permanently.");

    private static YamlConfiguration config = null;
    private static File configFile = null;

    private String key = "";
    private String defaultValue = "";

    private Lang(String key, String defValue) {
        this.key = key;
        this.defaultValue = defValue;
    }

    public String getMessage() {
        return replaceChatColours(this.getRawMessage());
    }

    public String getMessage(Object... format) {
        return replaceChatColours(String.format(this.getRawMessage(), format));
    }

    public String getRawMessage() {
        return config != null ? config.getString(this.key, this.defaultValue) : this.defaultValue;
    }

    public String getReplacedMessage(Object... objects) {
        String langMessage = this.getRawMessage();
        if (objects != null) {
            Object firstObject = null;
            for (int i = 0; i < objects.length; i++) {
                if (i % 2 == 0) {
                    firstObject = objects[i];
                } else {
                    if (firstObject != null && objects[i] != null)
                        langMessage = langMessage.replace(firstObject.toString(), objects[i].toString());
                }
            }
        }
        return replaceChatColours(langMessage);
    }

    public static void sendMessage(CommandSender sender, Lang lang) {
        String strMessage = lang.getMessage();
        if (!strMessage.isEmpty()) {
            List<String> messages = new ArrayList<String>();
            if (strMessage.contains("\n")) {
                String[] messageSplit = strMessage.split("\n");
                for (String message : messageSplit) messages.add(message);
            } else {
                messages.add(strMessage);
            }
            for (String message : messages) sender.sendMessage(message);
        }
    }

    public static void sendMessage(CommandSender sender, Lang lang, Object... objects) {
        String strMessage = lang.getMessage(objects);
        if (!strMessage.isEmpty()) {
            List<String> messages = new ArrayList<String>();
            if (strMessage.contains("\n")) {
                String[] messageSplit = strMessage.split("\n");
                for (String message : messageSplit) messages.add(message);
            } else {
                messages.add(strMessage);
            }
            for (String message : messages) sender.sendMessage(message);
        }
    }

    public static void sendRawMessage(CommandSender sender, Lang lang) {
        String strMessage = lang.getRawMessage();
        if (!strMessage.isEmpty()) {
            List<String> messages = new ArrayList<String>();
            if (strMessage.contains("\n")) {
                String[] messageSplit = strMessage.split("\n");
                for (String message : messageSplit) messages.add(message);
            } else {
                messages.add(strMessage);
            }
            for (String message : messages) sender.sendMessage(message);
        }
    }

    public static void sendReplacedMessage(CommandSender sender, Lang lang, Object... objects) {
        String strMessage = lang.getReplacedMessage(objects);
        if (!strMessage.isEmpty()) {
            List<String> messages = new ArrayList<String>();
            if (strMessage.contains("\n")) {
                String[] messageSplit = strMessage.split("\n");
                for (String message : messageSplit) messages.add(message);
            } else {
                messages.add(strMessage);
            }
            for (String message : messages) sender.sendMessage(message);
        }
    }

    public static void init(JavaPlugin plugin) {
        configFile = new File(plugin.getDataFolder(), "messages.yml");
        config = YamlConfiguration.loadConfiguration(configFile);

        for (Lang value : values()) {
            if (!config.isSet(value.key)) config.set(value.key, value.defaultValue);
        }
        try {
            config.save(configFile);
        } catch (Exception ex) {
        }
    }

    public static String getString(String path) {
        return config.getString(path);
    }

    public static String getString(String path, String defaultValue) {
        return config.getString(path, defaultValue);
    }

    public static String saveString(String path, String value) {
        if (!config.isSet(path)) {
            config.set(path, value);
            try {
                config.save(configFile);
            } catch (Exception ex) {
            }
        }
        return config.isSet(path) ? config.getString(value) : value;
    }

    public static String replaceChatColours(String aString) {
        return aString != null ? ChatColor.translateAlternateColorCodes('&', aString) : "";
    }

    public static List<String> replaceChatColours(List<String> lines) {
        if (lines != null) {
            for (int i = 0; i < lines.size(); i++) {
                lines.set(i, replaceChatColours(lines.get(i)));
            }
        }
        return lines;
    }

}