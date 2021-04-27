package net.novaprison.spoils;

import org.bukkit.ChatColor;
import java.util.List;

public class Lang {

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