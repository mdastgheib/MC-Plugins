package net.novaprison.Modules.Status;

import net.minecraft.server.v1_8_R1.MinecraftServer;
import net.novaprison.Core;
import net.novaprison.Events.ServerSecondEvent;
import net.novaprison.Utils.Slack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class StatusListener implements Listener {

    @EventHandler
    public void onSecond(ServerSecondEvent e)
    {
        if(StatusManager.counter == 300) {
            StatusManager.counter = 0;
            if(MinecraftServer.getServer().recentTps[0] < 19.20) {
                Core.log("Ticks per second below 19.20, reporting to slack! (tps: " + MinecraftServer.getServer().recentTps[0] + ")", Core.logMessage.urgent);
                Slack.send("TPS Below 19.20 (tps: " + MinecraftServer.getServer().recentTps[0] + ")", "NovaCore", Slack.statusURL, "Hack");
            } else {
                Core.log("Ticks per second above 19.20, not reporting. (tps: " + MinecraftServer.getServer().recentTps[0] + ")", Core.logMessage.notify);
            }
        } else {
            StatusManager.counter++;
        }
    }
}
