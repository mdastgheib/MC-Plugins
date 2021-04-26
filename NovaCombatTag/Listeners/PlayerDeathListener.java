package Nova.Tag.Listeners;

import Nova.Tag.Tag;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    Tag plugin;

    public PlayerDeathListener(Tag plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e)
    {
        Player p = e.getEntity().getPlayer();
        if (this.plugin.getTaggedPlayers().containsKey(p.getName())) {
            this.plugin.getTaggedPlayers().remove(p.getName());
        }
    }
}

