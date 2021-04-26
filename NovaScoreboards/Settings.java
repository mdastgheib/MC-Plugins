package com.nova.novascoreboards;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class Settings {
    private List<String> enabledWorlds = new ArrayList<String>();

    private String scoreboardTitle = "";
    private List<String> scoreboardKeys = new ArrayList<String>();
    private double refreshRate = 5D;

    private String rankupMessage = "";

    public List<String> getEnabledWorlds() {
        return this.enabledWorlds;
    }

    public String getRankupMessage() {
        return this.rankupMessage;
    }

    public double getRefreshRate() {
        return this.refreshRate;
    }

    public List<String> getScoreboardKeys() {
        return this.scoreboardKeys;
    }

    public String getScoreboardTitle() {
        return scoreboardTitle;
    }

    public void setEnabledWorlds(List<String> enabledWorlds) {
        if (enabledWorlds != null) this.enabledWorlds = enabledWorlds;
    }

    public void setRankupMessage(String rankupMessage) {
        if (rankupMessage != null) this.rankupMessage = ChatColor.translateAlternateColorCodes('&', rankupMessage);
    }

    public void setRefreshRate(double refreshRate) {
        this.refreshRate = refreshRate > 0D ? refreshRate : 5D;
    }

    public void setScoreboardKeys(List<String> scoreboardKeys) {
        if (scoreboardKeys != null) {
            if (this.scoreboardKeys == null) this.scoreboardKeys = new ArrayList<String>();
            else this.scoreboardKeys.clear();
            for (int keyIndex = 0; keyIndex < scoreboardKeys.size(); keyIndex++) {
                String scoreboardKey = scoreboardKeys.get(keyIndex);
                if (scoreboardKey != null)
                    this.scoreboardKeys.add(ChatColor.translateAlternateColorCodes('&', scoreboardKey));
            }
        }
    }

    public void setScoreboardTitle(String scoreboardTitle) {
        if (scoreboardTitle != null)
            this.scoreboardTitle = ChatColor.translateAlternateColorCodes('&', scoreboardTitle);
    }

}
