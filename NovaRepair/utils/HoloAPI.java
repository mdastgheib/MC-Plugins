import me.nova.novarepair.Lang;
import me.nova.novarepair.NovaRepair;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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
                    return com.gmail.filoghost.holograms.api.HolographicDisplaysAPI.createHologram(NovaRepair.getInstance(), hologramLocation, lines.toArray(new String[lines.size()]));
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
                        com.gmail.filoghost.holograms.api.Hologram repairHologram = com.gmail.filoghost.holograms.api.HolographicDisplaysAPI.createIndividualHologram(NovaRepair.getInstance(), hologramLocation, Arrays.asList(player), lines.isEmpty() ? new String[]{""} : lines.toArray(new String[lines.size()]));
                        final UUID playerUUID = player.getUniqueId();
                        repairHologram.setTouchHandler(new com.gmail.filoghost.holograms.api.TouchHandler() {
                            @Override
                            public void onTouch(com.gmail.filoghost.holograms.api.Hologram hologram, Player clicker) {
                                if (hologram == null || hologram.isDeleted()) return;
                                if (playerUUID != null && clicker != null && playerUUID.equals(clicker.getUniqueId())) {
                                    if (clicker.performCommand("novarepair")) hologram.delete();
                                }
                            }
                        });
                        return repairHologram;
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
                return com.gmail.filoghost.holograms.api.HolographicDisplaysAPI.getHolograms(NovaRepair.getInstance());
            } catch (Exception ex) {
            }
        }
        return null;
    }

    public static boolean hasHolographicDisplays() {
        return Bukkit.getServer().getPluginManager().isPluginEnabled("HolographicDisplays");
    }

}
