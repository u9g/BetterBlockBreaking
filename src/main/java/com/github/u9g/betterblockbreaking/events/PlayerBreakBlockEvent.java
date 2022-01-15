package com.github.u9g.betterblockbreaking.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class PlayerBreakBlockEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Location location;
    private Material newMaterial = Material.AIR;

    public PlayerBreakBlockEvent(Player player, Location location) {
        this.player = player;
        this.location = location;
    }

    @NotNull
    public Player getPlayer() {
        return this.player;
    }

    @NotNull
    public Location getLocation() {
        return this.location;
    }

    @NotNull
    public Material getNewMaterial() {
        return this.newMaterial;
    }

    public void setNewMaterial(final Material newMaterial) {
        this.newMaterial = newMaterial;
    }

    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}
