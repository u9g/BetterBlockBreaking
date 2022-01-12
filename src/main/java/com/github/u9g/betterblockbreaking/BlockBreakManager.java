package com.github.u9g.betterblockbreaking;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.destroystokyo.paper.util.SneakyThrow;
import com.github.u9g.betterblockbreaking.events.PlayerBreakBlockEvent;
import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.LinkedHashMap;
import java.util.Map;

public final class BlockBreakManager {
    public Map<Player, Action> player2LastAction = new LinkedHashMap<>();
    public Map<Player, Map<Location, Float>> player2Blocks = new LinkedHashMap<>();
    public JavaPlugin plugin;
    public ProtocolManager protocolManager;

    public BlockBreakManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.protocolManager = ProtocolLibrary.getProtocolManager();
        plugin.getServer().getPluginManager().registerEvents(new EventHandlers(this), plugin);
        Commands.init();
    }

    private void sendBlockDamage(Player p, int entityId, Location loc, float progress) {
        Preconditions.checkArgument(loc != null, "loc must not be null");
        Preconditions.checkArgument((progress >= 0.0 && progress <= 1.0) || progress == -1, "progress must be between 0.0 and 1.0 (inclusive) OR -1");
        PacketContainer blockAnim = new PacketContainer(PacketType.Play.Server.BLOCK_BREAK_ANIMATION);
        int stage = (int) (9 * progress); // There are 0 - 9 damage states
        blockAnim.getIntegers().write(0, entityId).write(1, stage);
        blockAnim.getBlockPositionModifier().write(0, new BlockPosition(loc.toVector()));
        try {
            protocolManager.sendServerPacket(p, blockAnim);
        } catch (Exception e) {
            SneakyThrow.sneaky(e);
        }
    }

    public void tickBlock(Player p, Location l, float tickSize) {
        var playerMap = player2Blocks.getOrDefault(p, new LinkedHashMap<>());
        float newTicks = playerMap.getOrDefault(l, 0F) + tickSize;
        if (newTicks < 10) {
            sendBlockDamage(p, p.getEntityId() + l.hashCode(), l, newTicks/10);
            playerMap.put(l, newTicks);
        } else {
            PlayerBreakBlockEvent event = new PlayerBreakBlockEvent(p, l);
            p.getWorld().getBlockAt(l).setType(event.newMaterial);
            sendBlockDamage(p, p.getEntityId() + l.hashCode(), l, -1); // remove break progress
            playerMap.remove(l);
        }
        player2Blocks.putIfAbsent(p, playerMap);
    }
}