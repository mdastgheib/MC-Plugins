package Nova.Tag.Listeners;

import Nova.Tag.Tag;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.List;

public class PlayerCmdListener implements Listener {

    Tag plugin;

    public PlayerCmdListener(Tag plugin)
    {
        this.plugin = plugin;
    }

    private List<String> list = new ArrayList<String>();

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onCmd(PlayerCommandPreprocessEvent e)
    {
        if(Tag.getTaggedPlayers().containsKey(e.getPlayer().getName())) {
            String msg = e.getMessage().toLowerCase().replace("/", "");
            String[] msgs = msg.split(" ");

            if(list.isEmpty()) {
                for(String s : plugin.getConfig().getStringList("Nova.allowedCmds")) {
                    list.add(s);
                }
            }
            if(!list.contains(msgs[0])) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', (plugin.getConfig().getString("Nova.Prefix")) + " " + plugin.getConfig().getString("Nova.PreventedCmdMsg")));
                return;
            }
        }
    }
}
