package com.github.u9g.betterblockbreaking;

import com.github.u9g.betterblockbreaking.events.PlayerBreakBlockEvent;
import com.github.u9g.betterblockbreaking.events.PlayerDigBlockEvent;
import io.papermc.paper.event.player.PlayerArmSwingEvent;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public record EventHandlers(BlockBreakManager blockBreakManager) implements Listener {
  @EventHandler
  private void onPlayerArmSwingEvent(PlayerArmSwingEvent e) {
    if (e.getPlayer().getGameMode().equals(GameMode.CREATIVE) || !blockBreakManager.player2LastAction.get(e.getPlayer()).equals(Action.LEFT_CLICK_BLOCK)) return;
    var block = e.getPlayer().getTargetBlock(4);
    if (block == null || block.getType().equals(Material.BEDROCK)) return;
    Location blockLoc = block.getLocation();
    PlayerDigBlockEvent event = new PlayerDigBlockEvent(e.getPlayer(), blockLoc);
    if (event.callEvent()) {
      blockBreakManager.tickBlock(e.getPlayer(), blockLoc, event.tickSize);
    }
    e.setCancelled(true);
  }

  @EventHandler
  private void onBlockBreak(PlayerBreakBlockEvent e) {
    for (Player p : blockBreakManager.player2Blocks.keySet()) {
      var playerMap = blockBreakManager.player2Blocks.get(p);
      playerMap.remove(e.location);
    }
  }

  @EventHandler
  private void onInteract (PlayerInteractEvent e) {
    blockBreakManager.player2LastAction.put(e.getPlayer(), e.getAction());
  }

  @EventHandler
  private void onPlayerChangeWorld(PlayerChangedWorldEvent e) {
    blockBreakManager.player2Blocks.remove(e.getPlayer());
  }
}
