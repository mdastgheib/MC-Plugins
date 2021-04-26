package com.nova.novascoreboards;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;

import static com.nova.novascoreboards.NovaScoreboards.getInstance;

public class EventListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        if (getInstance().inScoreboardWorld(player)) {
            player.getServer().getScheduler().runTaskLater(getInstance(), new Runnable() {
                @Override
                public void run() {
                    if (player != null && player.isOnline() && player.getScoreboard() == null && getInstance().inScoreboardWorld(player)) {
                        player.setScoreboard(getInstance().setScoreboard(player));
                    }
                }
            }, 10L);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLeave(PlayerQuitEvent event) {
        if (getInstance().inScoreboardWorld(event.getPlayer())) {
            getInstance().playersLeaving.add(event.getPlayer().getUniqueId());
            Scoreboard playerScoreboard = event.getPlayer().getScoreboard();
            if (playerScoreboard != null) {
                playerScoreboard.clearSlot(DisplaySlot.SIDEBAR);
                event.getPlayer().setScoreboard(playerScoreboard);
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        try {
            if (event.getEntity().getKiller() != null) {
                int currentDeaths = getInstance().getDeaths(event.getEntity());
                if (currentDeaths + 1 > Integer.MAX_VALUE) currentDeaths = Integer.MAX_VALUE;
                else currentDeaths++;
                getInstance().putDeaths(event.getEntity(), currentDeaths);

                int currentKills = getInstance().getKills(event.getEntity().getKiller());
                if (currentKills + 1 > Integer.MAX_VALUE) currentKills = Integer.MAX_VALUE;
                else currentKills++;
                getInstance().putKills(event.getEntity().getKiller(), currentKills);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerChangeWorlds(PlayerChangedWorldEvent event) {
        try {
            if (getInstance().getSettings().getEnabledWorlds().contains(event.getFrom().getName()) && !getInstance().inScoreboardWorld(event.getPlayer())) {
                getInstance().playersLeaving.add(event.getPlayer().getUniqueId());
                Scoreboard playerScoreboard = event.getPlayer().getScoreboard();
                if (playerScoreboard != null) {
                    playerScoreboard.clearSlot(DisplaySlot.SIDEBAR);
                    event.getPlayer().setScoreboard(playerScoreboard);
                }
            } else if (!getInstance().getSettings().getEnabledWorlds().contains(event.getFrom().getName()) && getInstance().inScoreboardWorld(event.getPlayer())) {
                event.getPlayer().setScoreboard(getInstance().setScoreboard(event.getPlayer()));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
