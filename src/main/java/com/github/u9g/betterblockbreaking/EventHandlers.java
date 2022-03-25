package com.github.u9g.betterblockbreaking;

import com.github.u9g.betterblockbreaking.events.PlayerBreakBlockEvent;
import com.github.u9g.betterblockbreaking.events.PlayerDigBlockEvent;
import com.google.common.base.Stopwatch;
import io.papermc.paper.event.player.PlayerArmSwingEvent;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public record EventHandlers(BlockBreakManager blockBreakManager) implements Listener {
  @EventHandler
  private void onPlayerArmSwingEvent(PlayerArmSwingEvent e) {
    var player = e.getPlayer();

    if (!player.hasPotionEffect(PotionEffectType.SLOW_DIGGING) && isMining(player)) {
      player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 999999999, -1));
    } else if (player.hasPotionEffect(PotionEffectType.SLOW_DIGGING) && !isMining(player)) {
      player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
    }

    if (player.getGameMode().equals(GameMode.CREATIVE) ||
            blockBreakManager.getPlayerLastAction(e.getPlayer()) != Action.LEFT_CLICK_BLOCK) return;
    var block = e.getPlayer().getTargetBlock(5);
    if (block == null || BlockBreakManager.unbreakableBlocks.contains(block.getType())) return;
    Location blockLoc = block.getLocation();
    PlayerDigBlockEvent event = new PlayerDigBlockEvent(e.getPlayer(), blockLoc);
    if (event.callEvent()) {
      blockBreakManager.tickBlock(e.getPlayer(), blockLoc, event.getTickSize());
    }
    e.setCancelled(true);
  }

  @EventHandler
  private void onBlockBreak(PlayerBreakBlockEvent e) {
    var sw = Stopwatch.createStarted();
    blockBreakManager
            .player2Blocks
            .forEach((key, value) -> value.remove(e.getLocation().toBlockKey()));
  }

  @EventHandler
  private void onBlockBroken(BlockBreakEvent e) {
    e.setDropItems(false);
  }

  @EventHandler
  private void onInteract (PlayerInteractEvent e) {
    blockBreakManager.player2LastAction.put(e.getPlayer().getUniqueId(), e.getAction());
  }

  @EventHandler
  private void onPlayerChangeWorld(PlayerChangedWorldEvent e) {
    blockBreakManager.player2Blocks.remove(e.getPlayer().getUniqueId());
  }

  @EventHandler
  private void onPlayerChangeWorld(PlayerQuitEvent e) {
    blockBreakManager.player2Blocks.remove(e.getPlayer().getUniqueId());
    blockBreakManager.player2LastAction.remove(e.getPlayer().getUniqueId());
  }

  private boolean isMining(Player player) {
    return blockBreakManager.getPlayerLastAction(player) == Action.LEFT_CLICK_BLOCK;
  }
}
