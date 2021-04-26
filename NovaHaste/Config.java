package Nova.Haste;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class Config {

    public static void setupConfig()
    {
        FileConfiguration c = Haste.getStaticPlugin().getConfig();

        c.addDefault("Nova.Messages.Prefix", "&7[&eNovaHaste&7]");
        c.addDefault("Nova.Messages.TimeRemaining", "%prefix% &c&l%time% &aseconds remaining of your haste boost!");
        c.addDefault("Nova.Messages.Expired", "%prefix% &c&lYour haste boost has ran out! You must wait %cooldown% to use it again!");
        c.addDefault("Nova.Messages.StillInCooldown", "%prefix% &cYou still have to wait %time% seconds to use your haste boost again!");
        c.addDefault("Nova.Messages.Reloaded", "%prefix% &aPlugin Configuration reloaded!");
        c.addDefault("Nova.Messages.AddWorld", "%prefix% &aSuccessfully added world %world% to list of worlds that Haste Boosts can be used!");
        c.addDefault("Nova.Messages.RemoveWorld", "%prefix% &aSuccessfully removed world %world% from the list of worlds that Haste Boosts can be used!");
        c.addDefault("Nova.Messages.Activated", "%prefix% &aActivated Haste Boost! Power: %power% - Duration: %duration% - Cooldown: %cooldown%");
        c.addDefault("Nova.Messages.toggleOff", "%prefix% &cToggled Haste off!");
        c.addDefault("Nova.Messages.toggleOn", "%prefix% &aToggled Haste on!");

        c.addDefault("Nova.MaxDuration", 30);
        c.addDefault("Nova.MaxCooldown", 60);
        c.addDefault("Nova.MaxPower", 5);

        List<String> worlds = new ArrayList<String>();
        c.addDefault("Nova.Worlds", worlds);

        List<String> hastetools = new ArrayList<String>();
        hastetools.add("DIAMOND_PICKAXE");
        hastetools.add("IRON_PICKAXE");
        c.addDefault("Nova.HasteTools", hastetools);

        List<String> players = new ArrayList<String>();
        c.addDefault("Players", players);

        c.options().copyDefaults(true);
        Haste.getStaticPlugin().saveConfig();
    }
}
