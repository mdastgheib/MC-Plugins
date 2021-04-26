package com.nova.novascoreboards.utils;
import com.nova.novascoreboards.NovaScoreboards;
import com.nova.novascoreboards.hooks.PointsAPI;
import com.nova.novascoreboards.hooks.RankupAPI;
import com.nova.novascoreboards.hooks.Vault;
import com.nova.novascoreboards.hooks.VoteAPI;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Utils {

    public static String formatMoney(double num) {
        if (num > 1000000000000D) return String.format("%.1f", num / 1000000000000D) + "Tri";
        else if (num > 1000000000D) return String.format("%.1f", num / 1000000000D) + "Bil";
        else if (num > 1000000D) return String.format("%.1f", num / 1000000D) + "Mil";
        else if (num > 1000D) return String.format("%.1f", num / 1000D) + "K";
        else return String.format("%.1f", num);
    }

    public static List<? extends Player> getOnlinePlayers() {
        Object onlinePlayers = Bukkit.getServer().getOnlinePlayers();
        return onlinePlayers instanceof Collection ? new ArrayList<Player>((Collection<? extends Player>) onlinePlayers) : (onlinePlayers instanceof Player[] ? Lists.newArrayList((Player[]) onlinePlayers) : new ArrayList<Player>());
    }

    public static int getPing(Player player) {
        Class craftPlayerClass = player.getClass();
        Object craftPlayer = craftPlayerClass.cast(player);
        ReflectionUtils.MethodInvoker getHandleMethod = ReflectionUtils.getMethod(craftPlayerClass, "getHandle");
        if (getHandleMethod != null) {
            try {
                Object entityPlayer = getHandleMethod.invoke(craftPlayer);
                if (entityPlayer != null) {
                    ReflectionUtils.FieldAccess pingField = ReflectionUtils.getField(entityPlayer.getClass(), "ping");
                    if (pingField != null) {
                        return pingField.getField().getInt(entityPlayer);
                    }
                }
            } catch (Exception ex) {
            }
        }
        return 0;
    }

    public static boolean isInteger(String aString) {
        try {
            Integer.parseInt(aString);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static String replaceFormats(Player player, String aString) {
        if (aString != null && player != null) {
            if (aString.contains("<online>"))
                aString = aString.replace("<online>", String.valueOf(getOnlinePlayers().size()));
            if (aString.contains("<tps>"))
                aString = aString.replace("<tps>", String.format("%.1f", TPSManager.getTPS()));
            if (aString.contains("<name>")) aString = aString.replace("<name>", player.getName());
            if (aString.contains("<ping>")) aString = aString.replace("<ping>", String.valueOf(getPing(player)));
            if (aString.contains("<money>") || aString.contains("<balance>")) {
                String playerBalance = Vault.hasVaultEconomy() ? formatMoney(Vault.getEconomy().getBalance(player)) : "0";
                aString = aString.replace("<money>", playerBalance).replace("<balance>", playerBalance);
            }
            if (aString.contains("<rank>"))
                aString = aString.replace("<rank>", Vault.hasVaultPermissions() ? Vault.getPermissions().getPrimaryGroup(player) : "Default");
            if (aString.contains("<rurank>")) aString = aString.replace("<rurank>", RankupAPI.getRank(player));
            long nextRankCost = RankupAPI.getNextRankCost(player);
            if (aString.contains("<nextrank>"))
                aString = aString.replace("<nextrank>", formatMoney(nextRankCost));
            if (aString.contains("<nextrankleft>")) {
                if (player.getName().equals("Test")) {
                    DebugManager.sendMessage("&cYour next rank cost is: $" + nextRankCost);
                }
                if (nextRankCost == -1L) {
                    aString = aString.replace("<nextrankleft>", "Can't rank up!");
                } else {
                    double playerBalance = (Vault.hasVaultEconomy() ? Vault.getEconomy().getBalance(player) : 0);
                    double moneyRequired = (playerBalance - nextRankCost) * -1;
                    if (player.getName().equals("Test")) {
                        DebugManager.sendMessage("&eYou have:require: $" + playerBalance + " ~~ $" + moneyRequired);
                    }
                    if (moneyRequired <= 0D)
                        aString = aString.replace("<nextrankleft>", NovaScoreboards.getInstance().getSettings().getRankupMessage());
                    else aString = aString.replace("<nextrankleft>", formatMoney(moneyRequired));
                }
            }
            if (aString.contains("<points>"))
                aString = aString.replace("<points>", String.valueOf(PointsAPI.hasPlayerPoints() ? PointsAPI.getPlayerPoints().getAPI().look(player.getUniqueId()) : 0));
            if (aString.contains("<votes>")) aString = aString.replace("<votes>", String.valueOf(VoteAPI.getVotes()));
            if (aString.contains("<votesleft>"))
                aString = aString.replace("<votesleft>", String.valueOf(VoteAPI.getVotesLeft()));
            if (aString.contains("<kills>"))
                aString = aString.replace("<kills>", String.valueOf(NovaScoreboards.getInstance().getKills(player)));
            if (aString.contains("<deaths>"))
                aString = aString.replace("<deaths>", String.valueOf(NovaScoreboards.getInstance().getDeaths(player)));
            if (aString.contains("<health>"))
                aString = aString.replace("<health>", String.valueOf((int) player.getHealth()));
            if (aString.contains("<hunger>"))
                aString = aString.replace("<hunger>", String.valueOf(player.getFoodLevel()));
            if (aString.contains("<xp>"))
                aString = aString.replace("<xp>", String.valueOf(player.getTotalExperience()));
            if (aString.contains("<world>"))
                aString = aString.replace("<world>", player.getWorld() != null ? player.getWorld().getName() : "Space");
            if (aString.contains("<x>"))
                aString = aString.replace("<x>", String.format("%.1f", player.getLocation().getX()));
            if (aString.contains("<y>"))
                aString = aString.replace("<y>", String.format("%.1f", player.getLocation().getY()));
            if (aString.contains("<z>"))
                aString = aString.replace("<z>", String.format("%.1f", player.getLocation().getZ()));
            if (aString.contains("<yaw>"))
                aString = aString.replace("<yaw>", String.format("%.1f", player.getLocation().getYaw()));
            if (aString.contains("<pitch>"))
                aString = aString.replace("<pitch>", String.format("%.1f", player.getLocation().getPitch()));
        }
        return aString;
    }

}
