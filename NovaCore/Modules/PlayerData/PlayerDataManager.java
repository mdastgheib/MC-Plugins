package net.novaprison.Modules.PlayerData;

import net.novaprison.NovaPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerDataManager {

    public static HashMap<UUID, NovaPlayer> novaPlayers = new HashMap<UUID, NovaPlayer>();

    public static void addDefaults(Player player)
    {

    }
}
