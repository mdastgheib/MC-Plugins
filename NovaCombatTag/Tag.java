package Nova.Tag;

import Nova.Tag.Commands.CmdNovaTag;
import Nova.Tag.Listeners.*;
import com.dsh105.echopet.EchoPetPlugin;
import com.dsh105.echopet.api.EchoPetAPI;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.LibsDisguises;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class Tag extends JavaPlugin {

    private static Plugin instance;
    private static WorldGuardPlugin worldGuardPlugin;
    private static EchoPetPlugin echoPetPlugin;
    private static LibsDisguises libsDisguisesPlugin;

    public static Long time;

    private static HashMap<String, Long> taggedPlayers = new HashMap();

    @Override
    public void onEnable()
    {
        this.worldGuardPlugin = getWGPlugin();
        this.echoPetPlugin = getEPPlugin();
        this.libsDisguisesPlugin = getLDPlugin();
        if (this.worldGuardPlugin == null) {
            getLogger().warning("Could not WorldGuard, disabling.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (this.echoPetPlugin == null) {
            getLogger().warning("Could not EchoPet, disabling.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (this.libsDisguisesPlugin == null) {
            getLogger().warning("Could not LibsDisguises, disabling.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        time = 10L;
        instance = this;

        Config.setupConfig();

        registerEvents(this, new DisguiseListener(this), new EntityDamageByEntityListener(this),
                new PlayerDeathListener(this), new PlayerGameModeChangeListener(this), new PlayerKickListener(this),
                new PlayerQuitListener(this), new PlayerRespawnListener(this),
                new PlayerTeleportListener(this), new PlayerToggleFlightListener(this), new PlayerCmdListener(this));

        getCommand("novatag").setExecutor(new CmdNovaTag());

        Task.startTask();
    }

    @Override
    public void onDisable()
    {
        instance = null;
        worldGuardPlugin = null;
        echoPetPlugin = null;
        libsDisguisesPlugin = null;
    }

    private void registerEvents(Plugin plugin, Listener... listeners)
    {
        for(Listener l : listeners) {
            getServer().getPluginManager().registerEvents(l, plugin);
        }
    }

    public static Plugin getInstance()
    {
        return instance;
    }

    public static HashMap<String, Long> getTaggedPlayers()
    {
        return taggedPlayers;
    }

    public HashMap<String, Long> NotStaticGetTaggedPlayers()
    {
        return taggedPlayers;
    }


    public void removeUnfairness(Player... players)
    {
        for(Player player : players) {
            if (player.isFlying()) {
                player.setFlying(false);
                player.setAllowFlight(false);
            }
            if (player.getGameMode() == GameMode.CREATIVE) {
                player.setGameMode(GameMode.SURVIVAL);
            }
            if (DisguiseAPI.isDisguised(player)) {
                DisguiseAPI.undisguiseToAll(player);
            }
            if(echoPetPlugin != null) {
                if (EchoPetAPI.getAPI().hasPet(player)) {
                    EchoPetAPI.getAPI().removePet(player, false, true);
                }
            }
        }
    }

    private WorldGuardPlugin getWGPlugin()
    {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
        if ((plugin == null) || (!(plugin instanceof WorldGuardPlugin))) {
            return null;
        }
        return (WorldGuardPlugin)plugin;
    }
    private EchoPetPlugin getEPPlugin()
    {
        Plugin plugin = getServer().getPluginManager().getPlugin("EchoPet");
        if ((plugin == null) || (!(plugin instanceof EchoPetPlugin))) {
            return null;
        }
        return (EchoPetPlugin)plugin;
    }
    private LibsDisguises getLDPlugin()
    {
        Plugin plugin = getServer().getPluginManager().getPlugin("LibsDisguises");
        if ((plugin == null) || (!(plugin instanceof LibsDisguises))) {
            return null;
        }
        return (LibsDisguises)plugin;
    }
}
