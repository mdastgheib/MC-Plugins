package com.novaprison.meow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Config {

    public static List<UUID> noPickup = new ArrayList<UUID>();
    public static HashMap<UUID, Long> cooldown = new HashMap<UUID, Long>();

    public static void setupConfig() {
        List<String> list = new ArrayList<String>();
        list.add("amine");

        Meow.pluginInstance.getConfig().options().header("MineBomb configuration");
        Meow.pluginInstance.getConfig().addDefault("Meow.regions", list);

        Meow.pluginInstance.getConfig().addDefault("Meow.cooldown", 5);
        Meow.pluginInstance.getConfig().addDefault("Meow.radius", 3);
        Meow.pluginInstance.getConfig().addDefault("Meow.bombitem.ID", 402);

        Meow.pluginInstance.getConfig().addDefault("Meow.bombitem.name", "&b&l>>> &e&lMineBomb &b&l<<<");

        Meow.pluginInstance.getConfig().addDefault("Meow.minDrops", 5);
        Meow.pluginInstance.getConfig().addDefault("Meow.maxDrops", 10);

        Meow.pluginInstance.getConfig().addDefault("Meow.prefix", "&7[&eMineBomb&7]");
        Meow.pluginInstance.getConfig().addDefault("Meow.cooldownmsg", "&cYou must wait a total of %time% inbetween using a minebomb!");
        Meow.pluginInstance.getConfig().addDefault("Meow.usedmsg", "&aYou've thrown a Minebomb! You must wait %cooldown% to throw another one!");
        Meow.pluginInstance.getConfig().addDefault("Meow.regionmsg", "&cYou are not permitted to use a Minebomb in this area!");
        Meow.pluginInstance.getConfig().addDefault("Meow.nopermmsg", "&cYou do not have permission to do this!");
        Meow.pluginInstance.getConfig().addDefault("Meow.reloadconfigmsg", "&aYou have reloaded Minebombs configuration!");
        Meow.pluginInstance.getConfig().addDefault("Meow.addregionmsg", "&aYou have added the region %region%!");
        Meow.pluginInstance.getConfig().addDefault("Meow.removeregionmsg", "&aYou have removed the region %region%!");

        Meow.pluginInstance.getConfig().options().copyHeader(true);
        Meow.pluginInstance.getConfig().options().copyDefaults(true);
        Meow.pluginInstance.saveConfig();
    }
}
