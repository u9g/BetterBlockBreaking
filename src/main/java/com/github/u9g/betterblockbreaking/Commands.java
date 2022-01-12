package com.github.u9g.betterblockbreaking;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class Commands {
  public static void init () {
    Bukkit.getCommandMap().register("mf", "pl", new Command("mf") {
      @Override
      public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (sender instanceof Player p) {
          PotionEffect pe = new PotionEffect(PotionEffectType.SLOW_DIGGING, 999999999, -1);
          p.addPotionEffect(pe);
        }
        return true;
      }
    });
  }
}
