package net.novaprison.Modules;

import net.novaprison.Core;
import net.novaprison.Modules.Announcer.Announcer;
import net.novaprison.Modules.AntiInvis.AntiInvisListener;
import net.novaprison.Modules.Blood.BloodListener;
import net.novaprison.Modules.Blood.BloodManager;
import net.novaprison.Modules.Chat.ChatListener;
import net.novaprison.Modules.Chat.ChatManager;
import net.novaprison.Modules.ChatFilter.FilterManager;
import net.novaprison.Modules.Commands.EasterEggCommandsListener;
import net.novaprison.Modules.PumpkinHelmets.PumpkinListener;
import net.novaprison.Modules.PumpkinHelmets.PumpkinManager;
import net.novaprison.Modules.Status.StatusListener;
import net.novaprison.Modules.Tab.TabListener;
import net.novaprison.Modules.Toggle.ToggleListener;
import net.novaprison.Modules.Toggle.ToggleManager;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;

public class ModuleManager {

    private static Core core;

    public static void setupModules(Core NovaCore)
    {
        core = NovaCore;

        PluginManager pluginManager = core.getServer().getPluginManager();

        pluginManager.registerEvents(new TabListener(), core);
        pluginManager.registerEvents(new ChatListener(), core);
        pluginManager.registerEvents(new PumpkinListener(), core);
        pluginManager.registerEvents(new AntiInvisListener(), core);
        pluginManager.registerEvents(new StatusListener(), core);
        pluginManager.registerEvents(new EasterEggCommandsListener(), core);
        pluginManager.registerEvents(new ToggleListener(), core);
        pluginManager.registerEvents(new BloodListener(), core);
        pluginManager.registerEvents(new Announcer(), core);
        //pluginManager.registerEvents(new Title(), core);

        SettingsManager.load(core);

        FilterManager.setupFilter();
        PumpkinManager.setupHelmet();
        ToggleManager.setupToggles();
        BloodManager.load();
        Announcer.setup();
        //Title.setup();
    }

    public static void reload()
    {
        //reload stored data and shit
    }

    public static void shutdownModules()
    {
        SettingsManager.destroy();
        FilterManager.destroy(core);
        PumpkinManager.destroy(core);
        ChatManager.destroy(core);
        ToggleManager.save();
        BloodManager.save();

        HandlerList.unregisterAll(core);
    }
}
