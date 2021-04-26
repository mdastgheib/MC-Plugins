package net.novaprison;

import net.novaprison.Events.ServerTickEvent;
import net.novaprison.Modules.Blood.BloodManager;
import net.novaprison.Modules.Toggle.ToggleManager;
import net.novaprison.Utils.ParticleEffect;
import net.novaprison.Utils.Slack;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public class PlayerListener implements Listener {

    private Plugin plugin;

    public PlayerListener(Plugin plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e)
    {
        Slack.send(e.getPlayer().getName() + " has joined the 2.0 test server!", e.getPlayer().getName(), "");
        if(!ToggleManager.swearfilter_toggle.containsKey(e.getPlayer().getUniqueId())) {
            ToggleManager.swearfilter_toggle.put(e.getPlayer().getUniqueId(), false);
        }
        if(!ToggleManager.blood_toggle.containsKey(e.getPlayer().getUniqueId())) {
            ToggleManager.blood_toggle.put(e.getPlayer().getUniqueId(), false);
        }
        if(!BloodManager.bloodData.containsKey(e.getPlayer().getUniqueId())) {
            BloodManager.bloodData.put(e.getPlayer().getUniqueId(), BloodManager.Blood.regular);
        }
        if(!BloodManager.bloodPower.containsKey(e.getPlayer().getUniqueId())) {
            BloodManager.bloodPower.put(e.getPlayer().getUniqueId(), BloodManager.Power.weak);
        }
        if(!ToggleManager.announce_toggle.containsKey(e.getPlayer().getUniqueId())) {
            ToggleManager.announce_toggle.put(e.getPlayer().getUniqueId(), true);
        }

        /*
        NovaTitle.setTime(e.getPlayer(), 10, 60, 10);
        NovaTitle.sendTitle(e.getPlayer(), SettingsManager.onJoin_Title, SettingsManager.onJoin_Subtitle);
        PacketPlayOutPlayerListHeaderFooter packetPlayOutPlayerListHeaderFooter = new PacketPlayOutPlayerListHeaderFooter();
        IChatBaseComponent h = ChatSerializer.a("{\"text\": \"" + ChatColor.translateAlternateColorCodes('&', SettingsManager.onJoin_Header) + "\"}");
        IChatBaseComponent f = ChatSerializer.a("{\"text\": \"" + ChatColor.translateAlternateColorCodes('&', SettingsManager.onJoin_Footer) + "\"}");

        try {
            Field headerField = packetPlayOutPlayerListHeaderFooter.getClass().getDeclaredField("a");
            headerField.setAccessible(true);
            headerField.set(packetPlayOutPlayerListHeaderFooter, h);
            headerField.setAccessible(!headerField.isAccessible());
            Field footerField = packetPlayOutPlayerListHeaderFooter.getClass().getDeclaredField("b");
            footerField.setAccessible(true);
            footerField.set(packetPlayOutPlayerListHeaderFooter, f);
            footerField.setAccessible(!footerField.isAccessible());
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        CraftPlayer craftPlayer = (CraftPlayer) e.getPlayer();
        craftPlayer.getHandle().playerConnection.sendPacket(packetPlayOutPlayerListHeaderFooter);
        */
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent e)
    {
        Slack.send(e.getPlayer().getName() + " has left the 2.0 test server!", e.getPlayer().getName(), "");
    }
    @EventHandler
    public void onKick(PlayerKickEvent e)
    {
        Slack.send(e.getPlayer().getName() + " has been kicked from the 2.0 test server!", e.getPlayer().getName(), "");
    }

    private static ArrayList<Arrow> arrows = new ArrayList<Arrow>();

    @EventHandler
    public void onTick(ServerTickEvent e)
    {
        if(arrows.isEmpty()) {
            return;
        }
        ArrayList<Arrow> arrowsToRemove = new ArrayList<Arrow>();
        for(Arrow arrow : arrows) {
            if(!arrow.isOnGround()) {
                ParticleEffect effect = new ParticleEffect(ParticleEffect.ParticleType.REDSTONE, 0D, 1, 0.5F, 0.5F, 0.5F);
                effect.sendToLocation(arrow.getLocation());
            } else {
                arrowsToRemove.add(arrow);
            }
        }
        for(Arrow arrow : arrowsToRemove) {
            arrows.remove(arrow);
        }
    }

    @EventHandler
    public void onHit(ProjectileHitEvent e)
    {
        if(!(e.getEntity().getShooter() instanceof Player)) {
            return;
        }
        if(!(e.getEntity() instanceof Arrow)) {
            return;
        }
        arrows.remove(e.getEntity());
        ParticleEffect effect = new ParticleEffect(ParticleEffect.ParticleType.SMOKE_LARGE, 0.1D, 10, 0.5F, 0.5F, 0.5F);
        effect.sendToLocation(e.getEntity().getLocation());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onShoot(ProjectileLaunchEvent e)
    {
        if(!(e.getEntity().getShooter() instanceof Player)) {
            return;
        }
        if(!(e.getEntity() instanceof Arrow)) {
            return;
        }
        arrows.add((Arrow) e.getEntity());
    }
}
