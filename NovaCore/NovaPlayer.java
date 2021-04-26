package net.novaprison;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class NovaPlayer {

    private Player player;
    private UUID playerUUID;
    private FileConfiguration playerConfig;

    private Integer tokens;

    public static NovaPlayer newPlayer(Core core, Player player)
    {
        return new NovaPlayer(core, player);
    }

    public NovaPlayer(Core core, Player player)
    {
        File playerFile = new File(core.getDataFolder() + File.separator + "playerdata" +
        File.separator + player.getUniqueId() + ".yml");
        if(!playerFile.exists()) {
            try {
                playerFile.createNewFile();
                // Setup file defaults
            } catch(IOException ex) {
                ex.printStackTrace();
            }
        }
        this.playerConfig = YamlConfiguration.loadConfiguration(playerFile);

        this.tokens = this.playerConfig.getInt("Tokens");

        this.player = player;
        this.playerUUID = player.getUniqueId();
    }

    public void save()
    {
        File playerFile = new File(Core.getInstance().getDataFolder() + File.separator + "playerdata" +
                File.separator + player.getUniqueId() + ".yml");
        this.playerConfig.set("Tokens", this.tokens);
        this.playerConfig.set("LastOnline", System.currentTimeMillis());

        try {
            this.playerConfig.save(playerFile);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /*
        Data getters
     */
    public Player getPlayer()
    {
        return this.player;
    }
    public UUID getPlayerUUID()
    {
        return this.playerUUID;
    }
    public int getTokens()
    {
        return this.tokens;
    }
}
