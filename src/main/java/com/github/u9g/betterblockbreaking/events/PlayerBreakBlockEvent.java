package com.github.u9g.betterblockbreaking.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class PlayerBreakBlockEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    public Player player;
    public Location location;
    public Material newMaterial = Material.AIR;

    public PlayerBreakBlockEvent(Player player, Location location) {
        this.player = player;
        this.location = location;
    }

    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
