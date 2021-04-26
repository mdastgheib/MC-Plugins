package net.novaprison.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

public final class ServerSecondEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Plugin plugin;

    public ServerSecondEvent(Plugin p) {
        plugin = p;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}