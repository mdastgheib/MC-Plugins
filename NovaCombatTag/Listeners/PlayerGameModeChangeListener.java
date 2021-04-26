package Nova.Tag.Listeners;

import Nova.Tag.Tag;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

public class PlayerGameModeChangeListener implements Listener {

    Tag plugin;

    public PlayerGameModeChangeListener(Tag plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler(priority= EventPriority.HIGHEST)
    public void onPlayerGameModeChange(PlayerGameModeChangeEvent e)
    {
        Player p = e.getPlayer();

        if(this.plugin.getTaggedPlayers().containsKey(p.getName())) {
            if(!p.hasPermission("novatag.bypass.gamemodeincombat")) {
                e.setCancelled(true);
            }
        }
    }
}
