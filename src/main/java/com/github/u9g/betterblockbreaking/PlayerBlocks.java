package com.github.u9g.betterblockbreaking;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Map;

public class PlayerBlocks {
    private final Table<Player, Location, Double> player2Blocks = HashBasedTable.create();
    private final int maxBlocksPerPlayer;
    public PlayerBlocks(int maxBlocksPerPlayer) {
        this.maxBlocksPerPlayer = maxBlocksPerPlayer;
    }

    public void removeBlock(Location location) {
        player2Blocks.column(location).clear();
    }

    public void removePlayer(Player player) {
        player2Blocks.row(player).clear();
    }

    public void set(Player player, Location location, Double progress) {
        Preconditions.checkArgument(1 > progress && progress > 0, "Progress should always be 0 <= progress <= 1");
        var col = player2Blocks.row(player);
        if (col.size() == this.maxBlocksPerPlayer) {
            // remove first
            // FIXME: Maybe reset block break progress here?
            for (Map.Entry<Location, Double> entry : col.entrySet()) {
                col.remove(entry.getKey());
                break;
            }
        }
        player2Blocks.put(player, location, progress);
    }

    public double get(Player player, Location location) {
        var storedProgress = player2Blocks.get(player, location);
        return storedProgress == null ? 0 : storedProgress;
    }
}
