package Nova.Tag;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class Config {

    public static void setupConfig()
    {
        FileConfiguration config = Tag.getInstance().getConfig();

        List<String> loggers = new ArrayList<String>();
        config.addDefault("Nova.loggers", loggers);

        List<String> messages = new ArrayList<String>();
        messages.add("&1&l&m============================================");
        messages.add("      &7&l&oYou have been killed for");
        messages.add("      &7&l&ologging whilst in combat...");
        messages.add("");
        messages.add("      &7&l&o- Was it really worth it?");
        messages.add("&1&l&m============================================");
        config.addDefault("Nova.onAfterCombatLogRejoinMessages", messages);

        config.addDefault("Nova.TagTime", 10);
        config.addDefault("Nova.Prefix", "&7[&c&lNovaTag&7]");
        config.addDefault("Nova.UntaggedMsg", "&7You are no longer in combat! You may now logout!");
        config.addDefault("Nova.TaggedMsg", "&7You have been combat tagged! Do not log out!");
        config.addDefault("Nova.PreventedCmdMsg", "&cYou may not use this command whilst in combat!");
        config.addDefault("Nova.CombatLoggedMsg", "&c&l>>> &7&l%player% has logged out in combat! &c<<<");

        config.addDefault("Nova.punish.world", "world");
        config.addDefault("Nova.punish.x", 0);
        config.addDefault("Nova.punish.y", -5);
        config.addDefault("Nova.punish.z", 0);

        List<String> cmds = new ArrayList<String>();
        cmds.add("msg");
        cmds.add("m");
        cmds.add("r");
        cmds.add("reply");
        cmds.add("ban");
        cmds.add("tempban");
        cmds.add("mute");
        cmds.add("kick");
        cmds.add("tempmute");
        cmds.add("st");
        cmds.add("staffchat");
        cmds.add("vanish");
        cmds.add("ev");
        cmds.add("v");
        cmds.add("evanish");
        cmds.add("sm");
        cmds.add("staffmode");
        cmds.add("tag");
        cmds.add("novatag");
        cmds.add("ntag");


        config.addDefault("Nova.allowedCmds", cmds);

        config.options().copyDefaults(true);
        Tag.getInstance().saveConfig();
    }
}
