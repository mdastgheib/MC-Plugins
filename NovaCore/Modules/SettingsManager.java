package net.novaprison.Modules;

import net.novaprison.Core;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SettingsManager {

    public static String helmet_name;
    public static List<String> helmet_lore;

    public static String beginSuffocating;
    public static String beginSuffocatingSubtitle;
    public static String suffocating_message_1;
    public static String suffocating_message_1subtitle;
    public static String suffocating_message_2;
    public static String suffocating_message_2subtitle;

    public static String swear_message;
    public static String swear_messagesub;

    public static String globalmute;
    public static String globalmuteon;
    public static String globalmuteoff;
    public static String globalmutetitle;
    public static String globalmutesubtitle;
    public static String globalunmutetitle;
    public static String globalunmutesubtitle;

    public static List<String> clearchatmsg;
    public static String clearchattitle;
    public static String clearchatsubtitle;

    public static String mentiontitle;
    public static String mentionsubtitle;

    public static String balance;
    public static String otherbalance;
    public static String othernotfound;

    public static Long chatDelay;
    public static String chatDelayMsg;

    public static int capPercent;
    public static String capTitle;
    public static String capSubtitle;

    public static Long mentionDelay;
    public static String mentionTooQuickMsg;

    public static String onJoin_Header;
    public static String onJoin_Footer;
    public static String onJoin_Title;
    public static String onJoin_Subtitle;

    private static boolean isSet = false;

    private static void set()
    {
        isSet = true;
    }

    public static void load(Core core)
    {
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(Core.getInstance().getDataFolder() + File.separator + "chat.yml"));

        helmet_name = config.getString("Nova.Helmet.Name");
        helmet_lore = config.getStringList("Nova.Helmet.Lore");
        ArrayList<String> c = new ArrayList<String>();
        for(String s : helmet_lore) {
            c.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        helmet_lore = c;

        beginSuffocating = config.getString("Nova.BeginSuffocating.Message.title");
        beginSuffocatingSubtitle = config.getString("Nova.BeginSuffocating.Message.subtitle");
        suffocating_message_1 = config.getString("Nova.Suffocating.Message.1");
        suffocating_message_1subtitle = config.getString("Nova.Suffocating.Message.1subtitle");
        suffocating_message_2 = config.getString("Nova.Suffocating.Message.2");
        suffocating_message_2subtitle = config.getString("Nova.Suffocating.Message.2subtitle");

        swear_message = config.getString("Nova.noSwear.Title");
        swear_messagesub = config.getString("Nova.noSwear.Subtitle");

        globalmute = config.getString("Nova.Chat.globalmute");
        globalmuteon = config.getString("Nova.Chat.togglemuteon");
        globalmuteoff = config.getString("Nova.Chat.togglemuteoff");

        globalmutetitle = config.getString("Nova.Chat.togglemuteontitle");
        globalmutesubtitle = config.getString("Nova.Chat.togglemuteonsubtitle");
        globalunmutetitle = config.getString("Nova.Chat.toggleunmuteontitle");
        globalunmutesubtitle = config.getString("Nova.Chat.toggleunmuteonsubtitle");

        clearchatmsg = config.getStringList("Nova.chatClear.Messages");

        clearchattitle = config.getString("Nova.chatClear.title");
        clearchatsubtitle = config.getString("Nova.chatClear.subtitle");

        mentiontitle = config.getString("Nova.MentionTitle");
        mentionsubtitle = config.getString("Nova.MentionSubtitle");

        balance = config.getString("Nova.Balance.message");
        otherbalance = config.getString("Nova.Balance.othermessage");
        othernotfound = config.getString("Nova.Balance.othernotfound");

        chatDelay = config.getLong("Nova.Chat.Delay");
        chatDelayMsg = config.getString("Nova.Chat.DelayMessage");

        capPercent = config.getInt("Nova.Chat.CapitalPercect");
        capTitle = config.getString("Nova.Chat.capitalTitle");
        capSubtitle = config.getString("Nova.Chat.capitalSubtitle");

        mentionDelay = config.getLong("Nova.Chat.mentionDelay");
        mentionTooQuickMsg = config.getString("Nova.Chat.mentionTooQuick");

        onJoin_Header = config.getString("Nova.onJoin.Header");
        onJoin_Footer = config.getString("Nova.onJoin.Footer");
        onJoin_Title = config.getString("Nova.onJoin.Title");
        onJoin_Subtitle = config.getString("Nova.onJoin.Subtitle");

        set();
    }

    public static void reload(Core core)
    {
        if(isSet) {
            load(core);
        } else {
            Core.log("SettingsManager - tried to reload when config options were not set!", Core.logMessage.urgent);
        }
    }

    public static void destroy()
    {
        helmet_name = null;
        helmet_lore = null;

        beginSuffocating = null;
        beginSuffocatingSubtitle = null;
        suffocating_message_1 = null;
        suffocating_message_1subtitle = null;
        suffocating_message_2 = null;
        suffocating_message_2subtitle = null;
    }
}
