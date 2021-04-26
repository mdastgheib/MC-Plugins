package Nova.Tag.Listeners;

import Nova.Tag.Tag;
import me.libraryaddict.disguise.events.DisguiseEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class DisguiseListener implements Listener {

    Tag plugin;

    public DisguiseListener (Tag plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onDisguise(DisguiseEvent e)
    {
        if(e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();

            if(this.plugin.getTaggedPlayers().containsKey(p.getName())) {
                if(!p.hasPermission("novatag.bypass.disguise")) {
                    e.setCancelled(true);
                }
            }
        }
    }
}
