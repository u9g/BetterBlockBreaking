package com.github.u9g.betterblockbreaking.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class PlayerBreakBlockEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Location location;
    private Material newMaterial = Material.AIR;
    private final Block block;
    private boolean cancelled = false;

    public PlayerBreakBlockEvent(Player player, Location location, Block block) {
        this.player = player;
        this.location = location;
        this.block = block;
    }

    @NotNull
    public Block getBlock() { return this.block; }

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

    public static HandlerList getHandlerList() { return handlers; }

    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
