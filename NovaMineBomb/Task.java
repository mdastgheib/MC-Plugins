package com.novaprison.meow;

import com.novaprison.meow.util.LegitMeowUtil;
import com.novaprison.meow.util.ParticleEffect;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Task {
    private Item theItem = null;
    private Player player = null;
    private int radius = 0;
    private long startTime = 0L;
    private int step = 0;

    public Task(Item item, Player player, int radius) {
        this.theItem = item;
        this.player = player;
        this.radius = radius;
        this.step = 0;
    }

    public Task startBomb() {
        this.step = 0;
        this.startTime = System.currentTimeMillis();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (step >= 4 || theItem == null) {
                    try {
                        this.cancel();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return;
                }

                long timeElapsed = System.currentTimeMillis() - startTime;
                if (step == 0) {
                    if (timeElapsed >= 1750) {
                        ParticleEffect.CLOUD.display(theItem.getLocation(), 0.5F, 0.5F, 0.5F, 0.1F, 20);
                        theItem.getWorld().playSound(theItem.getLocation(), Sound.CREEPER_HISS, 1.0F, 1.0F);
                        step++;
                        startTime = System.currentTimeMillis();
                    }
                } else if (step == 1) {
                    if (timeElapsed >= 1500) {
                        ParticleEffect.CLOUD.display(theItem.getLocation(), 0.5F, 0.5F, 0.5F, 0.1F, 30);
                        ParticleEffect.FIREWORKS_SPARK.display(theItem.getLocation(), 0.5F, 0.5F, 0.5F, 0.1F, 20);
                        theItem.getWorld().playSound(theItem.getLocation(), Sound.CREEPER_HISS, 1.3F, 1.3F);
                        step++;
                        startTime = System.currentTimeMillis();
                    }
                } else if (step == 2) {
                    if (timeElapsed >= 1250) {
                        ParticleEffect.CLOUD.display(theItem.getLocation(), 0.5F, 0.5F, 0.5F, 0.1F, 40);
                        ParticleEffect.FIREWORKS_SPARK.display(theItem.getLocation(), 0.5F, 0.5F, 0.5F, 0.1F, 30);
                        ParticleEffect.SMOKE.display(theItem.getLocation(), 0.5F, 0.5F, 0.5F, 0.1F, 30);
                        theItem.getWorld().playSound(theItem.getLocation(), Sound.CREEPER_HISS, 1.5F, 1.5F);
                        step++;
                        startTime = System.currentTimeMillis();
                    }
                } else if (step == 3) {
                    if (timeElapsed >= 1000) {
                        ParticleEffect.EXPLODE.display(theItem.getLocation(), 1.5F, 1.5F, 1.5F, 0.1F, 50);
                        ParticleEffect.SMOKE.display(theItem.getLocation(), 1.5F, 1.5F, 1.5F, 0.1F, 50);
                        ParticleEffect.LARGE_EXPLODE.display(theItem.getLocation(), 1.5F, 1.5F, 1.5F, 0.1F, 50);
                        ParticleEffect.LARGE_SMOKE.display(theItem.getLocation(), 1.5F, 1.5F, 1.5F, 0.1F, 50);
                        theItem.getWorld().playSound(theItem.getLocation(), Sound.EXPLODE, 2.0F, 1.0F);
                        theItem.remove();
                        if (player != null) LegitMeowUtil.magic(player, theItem.getLocation(), radius);
                        Config.noPickup.remove(theItem.getUniqueId());
                        step++;
                        startTime = System.currentTimeMillis();
                    }
                }
                if (step >= 4) this.cancel();
            }
        }.runTaskTimer(Meow.pluginInstance, 5L, 5L);
        return this;
    }

    public Item getItem() {
        return this.theItem;
    }

    public Player getPlayer() {
        return this.player;
    }

    public int getRadius() {
        return this.radius;
    }

    public static Task startBomb(final Item i, final Player p, final int radius) {
        return new Task(i, p, radius).startBomb();
    }

}
