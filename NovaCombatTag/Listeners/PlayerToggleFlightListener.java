package Nova.Tag.Listeners;

import Nova.Tag.Tag;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class PlayerToggleFlightListener implements Listener {

    Tag plugin;

    public PlayerToggleFlightListener(Tag plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler(priority= EventPriority.LOWEST)
    public void onPlayerToggleFlight(PlayerToggleFlightEvent e)
    {
        Player p = e.getPlayer();
        if(p.hasPermission("novatag.bypass.antiflight")) {
            return;
        }
        if (this.plugin.getTaggedPlayers().containsKey(p.getName())) {
            p.setAllowFlight(false);
            p.setFlying(false);
            e.setCancelled(true);
        }
    }
}
