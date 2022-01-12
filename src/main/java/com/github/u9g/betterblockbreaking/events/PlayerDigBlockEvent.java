package com.github.u9g.betterblockbreaking.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class PlayerDigBlockEvent extends Event implements Cancellable {
  private static final HandlerList handlers = new HandlerList();
  private boolean isCancelled = false;
  public Player player;
  public Location location;
  public float tickSize = 0.7F;

  public PlayerDigBlockEvent(Player player, Location location) {
    this.player = player;
    this.location = location;
  }

  public HandlerList getHandlers() {
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
