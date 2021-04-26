package Nova.Tag.Listeners;

import Nova.Tag.Tag;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerTeleportListener implements Listener {

    Tag plugin;

    public PlayerTeleportListener(Tag plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler(priority= EventPriority.HIGHEST, ignoreCancelled = false)
    public void onPlayerTeleport(PlayerTeleportEvent e)
    {
        Player p = e.getPlayer();

        if(!this.plugin.getTaggedPlayers().containsKey(p.getName())) {
            return;
        }

        if(!p.hasPermission("novatag.bypass.teleportincombat")) {
            e.setCancelled(true);
        }
    }
}
