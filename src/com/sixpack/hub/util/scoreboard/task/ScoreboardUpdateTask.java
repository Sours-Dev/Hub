package com.sixpack.hub.util.scoreboard.task;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.sixpack.hub.util.scoreboard.PlayerBoard;



public class ScoreboardUpdateTask extends BukkitRunnable {

    @Override
    public void run() {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            PlayerBoard.get(p).send();
        }
    }

}