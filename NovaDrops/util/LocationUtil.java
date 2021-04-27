package me.nova.novadrops.util;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author KingFaris10
 */
public class LocationUtil {
    public static Location getLocationInfront(Player player) {
        String dir = getCardinalDirection(player);
        Location loc = player.getEyeLocation().clone().add(0D, 0.15D, 0D);
        switch (dir) {
            case "N":
                loc = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ() - 1, loc.getYaw(), loc.getPitch());
                break;
            case "NE":
                loc = new Location(loc.getWorld(), loc.getX() + 1, loc.getY(), loc.getZ() - 1, loc.getYaw(), loc.getPitch());
                break;
            case "E":
                loc = new Location(loc.getWorld(), loc.getX() + 1, loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
                break;
            case "SE":
                loc = new Location(loc.getWorld(), loc.getX() + 1, loc.getY(), loc.getZ() + 1, loc.getYaw(), loc.getPitch());
                break;
            case "S":
                loc = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ() + 1, loc.getYaw(), loc.getPitch());
                break;
            case "SW":
                loc = new Location(loc.getWorld(), loc.getX() - 1, loc.getY(), loc.getZ() + 1, loc.getYaw(), loc.getPitch());
                break;
            case "W":
                loc = new Location(loc.getWorld(), loc.getX() - 1, loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
                break;
            case "NW":
                loc = new Location(loc.getWorld(), loc.getX() - 1, loc.getY(), loc.getZ() - 1, loc.getYaw(), loc.getPitch());
                break;
            default:
                break;
        }
        return loc;
    }

    public static String getCardinalDirection(Player player) {
        double rotation = (player.getEyeLocation().getYaw() - 90) % 360;
        if (rotation < 0) rotation += 360D;
        if (rotation >= 0 && rotation < 22.5) {
            return "N";
        } else if (rotation >= 22.5 && rotation < 67.5) {
            return "NE";
        } else if (rotation >= 67.5 && rotation < 112.5) {
            return "E";
        } else if (rotation >= 112.5 && rotation < 157.5) {
            return "SE";
        } else if (rotation >= 157.5 && rotation < 202.5) {
            return "S";
        } else if (rotation >= 202.5 && rotation < 247.5) {
            return "SW";
        } else if (rotation >= 247.5 && rotation < 292.5) {
            return "W";
        } else if (rotation >= 292.5 && rotation < 337.5) {
            return "NW";
        } else if (rotation >= 337.5 && rotation < 360.0) {
            return "N";
        } else {
            return null;
        }
    }
}
