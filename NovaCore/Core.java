package net.novaprison;

import net.novaprison.Modules.Chat.ChatManager;
import net.novaprison.Modules.Commands.*;
import net.novaprison.Modules.ModuleManager;
import net.novaprison.Tasks.TickTask;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Core extends JavaPlugin {

    private static Plugin instance;
    private static String logPrefix = "[NovaCore] ";

    @Override
    public void onEnable()
    {
        long startTime = System.currentTimeMillis();
        log("Enabling NovaCore!", logMessage.notify);

        instance = this;
        setupConfig(instance.getConfig());
        ModuleManager.setupModules((Core) instance);
        TickTask.startTask(instance);

        getCommand("core").setExecutor(new CoreCommand());
        getCommand("developer").setExecutor(new DeveloperCommand());
        getCommand("chatfilter").setExecutor(new FilterCommand());
        getCommand("mutechat").setExecutor(new MutechatCommand());
        getCommand("clearchat").setExecutor(new ClearchatCommand());
        getCommand("toggle").setExecutor(new ToggleCommand());
        getCommand("blood").setExecutor(new BloodCommand());
        //getCommand("titleannouncement").setExecutor(new TitleannouncementsCommand());
        getCommand("announcement").setExecutor(new AnnouncementCommand());
        override("balance", new BalanceCommand());
        //override("title", new TitleCommand());

        registerEvents(instance, new PlayerListener(instance));

        long timeTaken = System.currentTimeMillis() - startTime;
        log("Finished enabling NovaCore - Took " + timeTaken + "ms!", logMessage.notify);
    }

    @Override
    public void onDisable()
    {
        instance.getServer().getScheduler().cancelTasks(instance);
        ModuleManager.shutdownModules();
        instance = null;
    }

    public static Plugin getInstance()
    {
        return instance;
    }

    private void registerEvents(Plugin plugin, Listener... listeners)
    {
        PluginManager pluginManager = plugin.getServer().getPluginManager();
        for(Listener listener : listeners) {
            pluginManager.registerEvents(listener, plugin);
        }
    }

    public static void log(String msg, logMessage type)
    {
        if(type.equals(logMessage.notify)) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&b" + logPrefix + "&a[&2@&a] &9" + msg));
        } else if(type.equals(logMessage.error)) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&b" + logPrefix + "&c[&4&l(!!!)&c] &c" + msg));
        } else if(type.equals(logMessage.urgent)) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&b" + logPrefix + "&e[&6&l(***)&e] &e" + msg));
        } else if(type.equals(logMessage.debug)) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&b" + logPrefix + "&7[&9%&7] &f" + msg));
        }
    }
    public static enum logMessage {
        notify, error, urgent, debug
    }

    private void setupConfig(FileConfiguration cx)
    {
        cx.addDefault("Nova.Chat.Format", "<staff> <donate> <mine> <displayname> <prestige>&7: &6<msg>");
        for(ChatManager.mineRank rank : ChatManager.mineRank.values()) {
            cx.addDefault("Nova.Chat.Mine." + rank.toString(), "&7&l[&a&l" + rank.toString() + "&7&l]");
        }
        for(ChatManager.staffRank rank : ChatManager.staffRank.values()) {
            cx.addDefault("Nova.Chat.Staff." + rank.toString(), "&7&l[&c&l" + rank.toString() + "&7&l]");
        }
        for(ChatManager.donatorRank rank : ChatManager.donatorRank.values()) {
            cx.addDefault("Nova.Chat.Donator." + rank.toString(), "&7&l[&9&l" + rank.toString() + "&7&l]");
        }
        for(ChatManager.prestigeRank rank : ChatManager.prestigeRank.values()) {
            cx.addDefault("Nova.Chat.Prestige." + rank.toString(), "&7&l[&2&l" + rank.toString() + "&7&l]");
        }

        FileConfiguration c = YamlConfiguration.loadConfiguration(new File(Core.getInstance().getDataFolder() + File.separator + "chat.yml"));

        c.addDefault("Nova.Chat.globalmute", "&cAll chat is currently muted.");
        c.addDefault("Nova.Chat.togglemuteon", "&cAll chat has been muted by %player%");
        c.addDefault("Nova.Chat.togglemuteoff", "&cAll chat has been allowed again by %player%");

        c.addDefault("Nova.Chat.togglemuteontitle", "&c&lChat has been muted.");
        c.addDefault("Nova.Chat.togglemuteonsubtitle", "&7&oShhh, don't talk baby...");
        c.addDefault("Nova.Chat.toggleunmuteontitle", "&c&lk talk bby");
        c.addDefault("Nova.Chat.toggleunmuteonsubtitle", "&7&ospeak loud and proud");

        List<String> stringList = Arrays.asList(new String[] { "msg", "m", "message", "r", "reply", "bc", "broadcast", "me"});
        c.addDefault("Nova.CommaMoneyCmds", stringList);
        c.addDefault("Nova.MentionColor", "&b");
        c.addDefault("Nova.MentionTitle", "&b%player% mentioned you!");
        c.addDefault("Nova.MentionSubtitle", "&7%msg%");

        List<String> tabList1 = Arrays.asList(new String[]{"&c&l>>> &b&lNova&e&lPrison &c&l<<<",
                "&a&l>>> &b&lNova&e&lPrison &a&l<<<", "&b&l>>> &b&lNova&e&lPrison &b&l<<<"});

        List<String> tabList2 = Arrays.asList(new String[] {"&e&lOh hai!",
                "&c&lOh hai!", "&c&lOh hai!"});

        c.addDefault("Nova.Tab.Header", tabList1);
        c.addDefault("Nova.Tab.Footer", tabList2);

        c.addDefault("Nova.Helmet.Name", "&c&lSpace helmet!");
        c.addDefault("Nova.Helmet.Lore", Arrays.asList(new String[] {"1", "2", "3"}));

        c.addDefault("Nova.BeginSuffocating.Message.title", "&c&lYou begin to suffocate..");
        c.addDefault("Nova.BeginSuffocating.Message.subtitle", "&7&oYou should probably put your helmet back on...");
        c.addDefault("Nova.Suffocating.Message.1", "&c&lYou continue to suffocate..");
        c.addDefault("Nova.Suffocating.Message.1subtitle", "&7&oYou struggle to breathe...");
        c.addDefault("Nova.Suffocating.Message.2", "&e&lYou continue to suffocate..");
        c.addDefault("Nova.Suffocating.Message.2subtitle", "&7&oYou struggle to breathe...");

        c.addDefault("Nova.noSwear.Title", "&bPlease do not swear.");
        c.addDefault("Nova.noSwear.Subtitle", "&7Please toggle swearing on mate");

        c.addDefault("Nova.chatClear.Messages", Arrays.asList(new String[] {"&e&l====================",
                " &b&lChat was cleared by %player%", "&e&l===================="}));

        c.addDefault("Nova.chatClear.title", "&cChat was cleared yo");
        c.addDefault("Nova.chatClear.subtitle", "&7chat was cleared by %player%");

        c.addDefault("Nova.Balance.message", "&aYou have $%money%");
        c.addDefault("Nova.Balance.othermessage", "&a%other% has $%money%");
        c.addDefault("Nova.Balance.othernotfound", "&cCould not find %name%!");

        c.addDefault("Nova.Chat.Delay", 2000L);
        c.addDefault("Nova.Chat.DelayMessage", "&c&lYou must wait at least %delay% seconds between messages!");
        c.addDefault("Nova.Chat.CapitalPercect", 50);
        c.addDefault("Nova.Chat.capitalTitle", "&cStandard capital letters warning title");
        c.addDefault("Nova.Chat.capitalSubtitle", "&cStandard capital letters warning subtitle");

        c.addDefault("Nova.Chat.mentionDelay", 5000L);
        c.addDefault("Nova.Chat.mentionTooQuick", "&c&lYou must wait before mentioning players again.");

        c.addDefault("Nova.onJoin.Header", "This is a header yo");
        c.addDefault("Nova.onJoin.Footer", "This is a footer yo");
        c.addDefault("Nova.onJoin.Title", "This is a title yo");
        c.addDefault("Nova.onJoin.Subtitle", "This is a subtitle yo");

        c.options().copyDefaults(true);
        try {
            c.save(new File(Core.getInstance().getDataFolder() + File.separator + "chat.yml"));
        } catch(IOException ex) {
            ex.printStackTrace();
        }
        cx.options().copyDefaults(true);
        saveConfig();
    }

    public void override(String name, CommandExecutor executor)
    {
        getServer().getPluginCommand(name).setExecutor(executor);
    }
}
