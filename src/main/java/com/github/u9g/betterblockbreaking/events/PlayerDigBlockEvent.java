package com.github.u9g.betterblockbreaking.events;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class PlayerDigBlockEvent extends Event implements Cancellable {
  private static final HandlerList handlers = new HandlerList();
  private boolean isCancelled = false;
  private final Player player;
  private final Location location;
  private double tickSize = 0.7;

  public PlayerDigBlockEvent(Player player, Location location) {
    this.player = player;
    this.location = location;
  }

  @NotNull
  public Player getPlayer() {
    return this.player;
  }

  @NotNull
  public Location getLocation() {
    return location;
  }

  public Block getBlock() {
    return location.getBlock();
  }

  public double getTickSize() {
    return tickSize;
  }

  public void setTickSize(double tickSize) {
    this.tickSize = tickSize;
  }

  public @NotNull HandlerList getHandlers() {
    return handlers;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }

  @Override
  public boolean isCancelled() {
    return isCancelled;
  }

  @Override
  public void setCancelled(boolean cancel) {
    isCancelled = cancel;
  }
}
