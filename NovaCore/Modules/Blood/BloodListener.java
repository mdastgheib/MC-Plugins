package net.novaprison.Modules.Blood;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class BloodListener implements Listener {

    @EventHandler
    public void onInteract(EntityDamageByEntityEvent e)
    {
        if(e.isCancelled()) {
            return;
        }
        if(!(e.getDamager() instanceof Player)) {
            return;
        }
        if(!(e.getEntity() instanceof LivingEntity)) {
            return;
        }
        Player player = (Player) e.getDamager();
        LivingEntity entity = (LivingEntity) e.getEntity();

        if(entity instanceof Player) {
            BloodManager.bleed((Player) entity);
        }
    }
}
