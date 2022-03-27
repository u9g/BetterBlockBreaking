package com.github.u9g.betterblockbreaking;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.destroystokyo.paper.util.SneakyThrow;
import com.github.u9g.betterblockbreaking.events.PlayerBreakBlockEvent;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class BlockBreakManager {
    public Map<Player, Action> player2LastAction = new HashMap<>();
    public PlayerBlocks playerBlocks;
    public JavaPlugin plugin;
    public ProtocolManager protocolManager;
    public static final Set<Material> unbreakableBlocks = Sets.newHashSet(Material.BEDROCK);
    private final int maxBlocksPerPlayer;

    /**
     * @param plugin
     * @param maxBlocksPerPlayer max blocks that are ticked kept in memory
     */
    public BlockBreakManager(JavaPlugin plugin, int maxBlocksPerPlayer) {
        this.plugin = plugin;
        this.maxBlocksPerPlayer = maxBlocksPerPlayer;
        this.playerBlocks = new PlayerBlocks(maxBlocksPerPlayer);
        this.protocolManager = ProtocolLibrary.getProtocolManager();
        plugin.getServer().getPluginManager().registerEvents(new EventHandlers(this), plugin);
    }

    public void sendBlockDamage(Player player, Location location, double progress) {
        Preconditions.checkArgument(location != null, "location must not be null");
        PacketContainer blockAnim = new PacketContainer(PacketType.Play.Server.BLOCK_BREAK_ANIMATION);
        int stage = (int)Math.round(progress * 9); // There are 0 - 9 damage states
        var entityId = player.getEntityId() + location.hashCode();
        blockAnim.getIntegers().write(0, entityId).write(1, stage);
        blockAnim.getBlockPositionModifier().write(0, new BlockPosition(location.toVector()));
        try {
            protocolManager.sendServerPacket(player, blockAnim);
        } catch (Exception e) {
            SneakyThrow.sneaky(e);
        }
    }

    public void tickBlock(Player player, Location location, double tickSize) {
        double newTicks = playerBlocks.get(player, location) + tickSize;
        if (1 > newTicks) { // regular amount of ticks
            sendBlockDamage(player, location, newTicks);
            playerBlocks.set(player, location, newTicks);
        } else {
            PlayerBreakBlockEvent event = new PlayerBreakBlockEvent(player, location, player.getWorld().getBlockAt(location));
            if (event.callEvent()) {
                player.getWorld().getBlockAt(location).setType(event.getNewMaterial());
                playerBlocks.removeBlock(location);
            }
            sendBlockDamage(player, location, -1); // clear block progress
        }
    }

    @Nullable
    public Action getPlayerLastAction(Player player) {
        return this.player2LastAction.get(player);
    }
}