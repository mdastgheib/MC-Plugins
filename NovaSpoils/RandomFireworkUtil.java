package net.novaprison.spoils;

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class RandomFireworkUtil {

    public static void LaunchRandomFirework(Location location)
    {
        FireworkEffect.Builder builder = FireworkEffect.builder();

        if (RandomUtils.nextInt(3) == 0)
        {
            builder.withTrail();
        }
        else if (RandomUtils.nextInt(2) == 0)
        {
            builder.withFlicker();
        }

        builder.with(org.bukkit.FireworkEffect.Type.values()[RandomUtils.nextInt(org.bukkit.FireworkEffect.Type.values().length)]);

        int colorCount = 17;

        builder.withColor(Color.fromRGB(RandomUtils.nextInt(255), RandomUtils.nextInt(255), RandomUtils.nextInt(255)));

        while (RandomUtils.nextInt(colorCount) != 0)
        {
            builder.withColor(Color.fromRGB(RandomUtils.nextInt(255), RandomUtils.nextInt(255), RandomUtils.nextInt(255)));
            colorCount--;
        }

        Firework firework = location.getWorld().spawn(location, Firework.class);
        FireworkMeta data = firework.getFireworkMeta();
        data.addEffects(new FireworkEffect[] { builder.build() });
        data.setPower(1);
        firework.setFireworkMeta(data);
    }
}
