package me.nova.novadrops.util;

import me.nova.novadrops.NovaDrops;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HoloAPI {

    public static boolean clearHolograms() {
        if (hasHolographicDisplays()) {
            try {
                com.gmail.filoghost.holograms.api.Hologram[] holograms = getHolograms();
                if (holograms != null) {
                    for (com.gmail.filoghost.holograms.api.Hologram hologram : holograms) {
                        if (hologram != null) hologram.delete();
                    }
                }
                return true;
            } catch (Exception ex) {
            }
        }
        return false;
    }

    public static boolean clearOldHolograms() {
        if (hasHolographicDisplays()) {
            try {
                com.gmail.filoghost.holograms.api.Hologram[] holograms = getHolograms();
                if (holograms != null) {
                    for (com.gmail.filoghost.holograms.api.Hologram hologram : holograms) {
                        if (hologram != null && System.currentTimeMillis() - hologram.getCreationTimestamp() >= 1000) {
                            hologram.delete();
                        }
                    }
                }
                return true;
            } catch (Exception ex) {
            }
        }
        return false;
    }


    public static com.gmail.filoghost.holograms.api.Hologram createHologram(Location hologramLocation, String text) {
        if (hasHolographicDisplays()) {
            try {
                if (hologramLocation != null) {
                    List<String> lines = new ArrayList<String>();
                    if (text == null) text = "";
                    if (text.contains("\n")) {
                        String[] textSplit = text.split("\n");
                        for (String newText : textSplit) {
                            lines.add(newText);
                        }
                    } else {
                        lines.add(text);
                    }
                    lines = MessageUtil.replace(lines);
                    return com.gmail.filoghost.holograms.api.HolographicDisplaysAPI.createHologram(NovaDrops.getInstance(), hologramLocation, lines.toArray(new String[lines.size()]));
                }
            } catch (Exception ex) {
            }
        }
        return null;
    }

    public static com.gmail.filoghost.holograms.api.Hologram createHologram(Player player, Location hologramLocation, String... textArray) {
        if (player == null) {
            return createHologram(hologramLocation, textArray != null && textArray.length > 0 ? textArray[0] : "");
        } else {
            if (hasHolographicDisplays()) {
                try {
                    if (hologramLocation != null) {
                        List<String> lines = new ArrayList<String>();
                        if (textArray == null) textArray = new String[0];
                        for (String text : textArray) {
                            if (text == null) continue;
                            if (text.contains("\n")) {
                                String[] textSplit = text.split("\n");
                                for (String newText : textSplit) {
                                    lines.add(newText);
                                }
                            } else {
                                lines.add(text);
                            }
                        }
                        lines = MessageUtil.replace(lines);
                        return com.gmail.filoghost.holograms.api.HolographicDisplaysAPI.createIndividualHologram(NovaDrops.getInstance(), hologramLocation, Arrays.asList(player), lines.isEmpty() ? new String[]{""} : lines.toArray(new String[lines.size()]));
                    }
                } catch (Exception ex) {
                }
            }
            return null;
        }
    }

    public static com.gmail.filoghost.holograms.api.Hologram getHologram(Object objHologram) {
        if (hasHolographicDisplays()) {
            try {
                return (com.gmail.filoghost.holograms.api.Hologram) objHologram;
            } catch (Exception ex) {
            }
        }
        return null;
    }

    public static com.gmail.filoghost.holograms.api.Hologram[] getHolograms() {
        if (hasHolographicDisplays()) {
            try {
                return com.gmail.filoghost.holograms.api.HolographicDisplaysAPI.getHolograms(NovaDrops.getInstance());
            } catch (Exception ex) {
            }
        }
        return null;
    }

    public static boolean hasHolographicDisplays() {
        return Bukkit.getServer().getPluginManager().isPluginEnabled("HolographicDisplays") && NovaDrops.getInstance().getConfig().getBoolean("holograms", true);
    }

}
