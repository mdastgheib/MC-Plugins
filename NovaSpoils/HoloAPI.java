package net.novaprison.spoils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * HolographicDisplays API Hook.
 */
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
                        if (hologram != null && System.currentTimeMillis() - hologram.getCreationTimestamp() >= 5000) {
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
                    lines = Lang.replaceChatColours(lines);
                    return com.gmail.filoghost.holograms.api.HolographicDisplaysAPI.createHologram(Spoils.getInstance(), hologramLocation, lines.toArray(new String[lines.size()]));
                }
            } catch (Exception ex) {
            }
        }
        return null;
    }

    public static com.gmail.filoghost.holograms.api.Hologram createHologram(Player player, Location hologramLocation, List<String> textList) {
        if (player == null) {
            return createHologram(hologramLocation, textList != null && textList.size() > 0 ? textList.get(0) : "");
        } else {
            if (hasHolographicDisplays()) {
                try {
                    if (hologramLocation != null) {
                        List<String> lines = textList == null ? new ArrayList<String>() : Lang.replaceChatColours(textList);
                        com.gmail.filoghost.holograms.api.Hologram hologram = com.gmail.filoghost.holograms.api.HolographicDisplaysAPI.createIndividualHologram(Spoils.getInstance(), hologramLocation, Arrays.asList(player), lines.isEmpty() ? new String[]{""} : lines.toArray(new String[lines.size()]));
                        final UUID playerUUID = player.getUniqueId();
                        return hologram;
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
                return com.gmail.filoghost.holograms.api.HolographicDisplaysAPI.getHolograms(Spoils.getInstance());
            } catch (Exception ex) {
            }
        }
        return null;
    }

    public static boolean hasHolographicDisplays() {
        return Bukkit.getServer().getPluginManager().isPluginEnabled("HolographicDisplays");
    }
}