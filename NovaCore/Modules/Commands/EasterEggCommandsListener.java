package net.novaprison.Modules.Commands;

import net.novaprison.Utils.PacketUtils.NovaTitle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.primesoft.customheadapi.CustomHeadApi;
import org.primesoft.customheadapi.IHeadCreator;
import org.primesoft.customheadapi.implementation.CustomHeadCreator;

public class EasterEggCommandsListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCmd(PlayerCommandPreprocessEvent e)
    {
        if(e.isCancelled()) {
            return;
        }
        Player player = e.getPlayer();
        String command = e.getMessage().split(" ")[0].replace("/", "").toUpperCase();

        try {
            cmds cmd = cmds.valueOf(command);
            switch (cmd) {
                case MOSES: {
                    e.setCancelled(true);
                    NovaTitle.setTime(player, 5, 60, 10);
                    NovaTitle.sendTitle(player, "&c&lRawr", "&7&oMoses says rawr!");
                    break;
                }
                case IMURDER: {
                    e.setCancelled(true);
                    NovaTitle.setTime(player, 5, 60, 10);
                    NovaTitle.sendTitle(player, "&e&lMeow", "&7&oiMurder says meow!");
                    break;
                }
                case SKULL: {
                    e.setCancelled(true);
                    CustomHeadCreator creator = new CustomHeadCreator();
                    player.getInventory().addItem(creator.createItemStack("http://imurder.me/nova/novaskin.png"));
                    player.updateInventory();
                }
                default: {
                    return;
                }
            }
        } catch(IllegalArgumentException ex) {}
    }

    public enum cmds {
        MOSES,
        IMURDER,
        SKULL,
    }
}
