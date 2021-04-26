package net.novaprison.Modules.Chat;

import net.novaprison.Core;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ChatManager {

    public static boolean chat_muted = false;

    public static HashMap<UUID, Long> lastChat = new HashMap<UUID, Long>();
    public static HashMap<UUID, Long> lastMention = new HashMap<UUID, Long>();

    private static String format = Core.getInstance().getConfig().getString("Nova.Chat.Format");
    public static String getFormat()
    {
        return format;
    }

    private static HashMap<mineRank, String> mineTags = new HashMap<mineRank, String>();
    private static HashMap<mineRank, String> getMineTags()
    {
        return mineTags;
    }

    private static HashMap<staffRank, String> staffTags = new HashMap<staffRank, String>();
    private static HashMap<staffRank, String> getStaffTags()
    {
        return staffTags;
    }

    private static HashMap<donatorRank, String> donatorTags = new HashMap<donatorRank, String>();
    private static HashMap<donatorRank, String> getDonatorTags()
    {
        return donatorTags;
    }

    private static HashMap<prestigeRank, String> prestigeTags = new HashMap<prestigeRank, String>();
    private static HashMap<prestigeRank, String> getPrestigeTags()
    {
        return prestigeTags;
    }

    // Mine ranks! A - Free
    public static enum mineRank {
        Error, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, Free
    }

    // Staff ranks! The staff peoples are swag!
    public static enum staffRank {
        None, Helper, Guard, Warden, Developer, Manager, Owner
    }

    // Donator ranks for the rich kids!
    public static enum donatorRank {
        None, Astro, Aviator, Captain, Commander, Cosmic, Galactic, Comet, Nova
    }

    // Prestige ranks! You no lifers!
    public static enum prestigeRank {
        None, Prestige1, Prestige2, Prestige3, Prestige4, Prestige5,
        Prestige6, Prestige7, Prestige8, Prestige9, Prestige10
    }

    private static void setTags(List<String> mineTags, List<String> staffTags, List<String> donatorTags, List<String> prestigeTags)
    {
        if(!getMineTags().isEmpty()) {
            getMineTags().clear();
        }
        int mine = 0;
        for(mineRank rank : mineRank.values()) {
            getMineTags().put(rank, mineTags.get(mine));
        }

        if(!getStaffTags().isEmpty()) {
            getStaffTags().clear();
        }
        int staff = 0;
        for(staffRank rank : staffRank.values()) {
            getStaffTags().put(rank, staffTags.get(staff));
        }

        if(!getDonatorTags().isEmpty()) {
            getDonatorTags().clear();
        }
        int donator = 0;
        for(donatorRank rank : donatorRank.values()) {
            getDonatorTags().put(rank, donatorTags.get(donator));
        }

        if(!getPrestigeTags().isEmpty()) {
            getPrestigeTags().clear();
        }
        int prestige = 0;
        for(prestigeRank rank : prestigeRank.values()) {
            getPrestigeTags().put(rank, donatorTags.get(prestige));
        }
    }

    public static HashMap<UUID, mineRank> mineRanks = new HashMap<UUID, mineRank>();
    public static HashMap<UUID, staffRank> staffRanks = new HashMap<UUID, staffRank>();
    public static HashMap<UUID, donatorRank> donatorRanks = new HashMap<UUID, donatorRank>();
    public static HashMap<UUID, prestigeRank> prestigeRanks = new HashMap<UUID, prestigeRank>();

    public static mineRank getMineRank(Player player)
    {
        UUID uuid = player.getUniqueId();
        return mineRanks.get(uuid);
    }
    public static staffRank getStaffRank(Player player)
    {
        UUID uuid = player.getUniqueId();
        return staffRanks.get(uuid);
    }
    public static donatorRank getDonatorRank(Player player)
    {
        UUID uuid = player.getUniqueId();
        return donatorRanks.get(uuid);
    }
    public static prestigeRank getPrestigeRank(Player player)
    {
        UUID uuid = player.getUniqueId();
        return prestigeRanks.get(uuid);
    }

    public static void update(Player player, boolean remove)
    {
        UUID uuid = player.getUniqueId();

        if(mineRanks.containsKey(uuid)) {
            mineRanks.remove(uuid);
        }
        if(staffRanks.containsKey(uuid)) {
            staffRanks.remove(uuid);
        }
        if(donatorRanks.containsKey(uuid)) {
            donatorRanks.remove(uuid);
        }
        if(prestigeRanks.containsKey(uuid)) {
            prestigeRanks.remove(uuid);
        }

        if(remove) {
            return;
        }

        if(player.getUniqueId().toString().replace("-", "").equals("121776d8a6054d4da7d54fe90e56b8e7") ||
                player.getUniqueId().toString().replace("-", "").equals("59dd98522da24aa4b025ff03d5a25c66")) {
            staffRanks.put(uuid, staffRank.Owner);
            mineRanks.put(uuid, mineRank.A);
            donatorRanks.put(uuid, donatorRank.Nova);
            prestigeRanks.put(uuid, prestigeRank.None);
            return;
        }

        PermissionManager permissionManager = PermissionsEx.getPermissionManager();

        for(int i = mineRank.values().length -1; i >= 0; i--) {
            mineRank rank = mineRank.values()[i];
            if(permissionManager.has(player, "nova.mine." + rank.toString().toLowerCase() + "")) {
                mineRanks.put(uuid, rank);
                break;
            }
            if(!mineRanks.containsKey(uuid))
                mineRanks.put(uuid, mineRank.Error);
        }

        for(int i = staffRank.values().length -1; i >= 0; i--) {
            staffRank rank = staffRank.values()[i];
            if(permissionManager.has(player, "nova.staff." + rank.toString().toLowerCase() + "")) {
                staffRanks.put(uuid, rank);
                break;
            }
            if(!staffRanks.containsKey(uuid))
                staffRanks.put(uuid, staffRank.None);
        }

        for(int i = donatorRank.values().length -1; i >= 0; i--) {
            donatorRank rank = donatorRank.values()[i];
            if(permissionManager.has(player, "nova.donator." + rank.toString().toLowerCase() + "")) {
                donatorRanks.put(uuid, rank);
                break;
            }
            if(!donatorRanks.containsKey(uuid))
                donatorRanks.put(uuid, donatorRank.None);
        }

        for(int i = prestigeRank.values().length -1; i >= 0; i--) {
            prestigeRank rank = prestigeRank.values()[i];
            if(permissionManager.has(player, "nova.prestige." + rank.toString().toLowerCase().replaceAll("prestige", "") + "")) {
                prestigeRanks.put(uuid, rank);
                break;
            }
            if(!prestigeRanks.containsKey(uuid))
                prestigeRanks.put(uuid, prestigeRank.None);
        }
    }

    /*
    Core and Config variables
 */
    private static Core getCore()
    {
        return (Core) Core.getInstance();
    }
    private static FileConfiguration getConfig()
    {
        return getCore().getConfig();
    }

    /*
        Destroy static variables
     */
    public static void destroy(Core core)
    {
        format = null;
        mineTags = null;
        staffTags = null;
        donatorTags = null;
        prestigeTags = null;

        mineRanks = null;
        staffTags = null;
        donatorTags = null;
        prestigeTags = null;
    }
}
