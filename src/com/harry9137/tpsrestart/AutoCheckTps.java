package com.harry9137.tpsrestart;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class AutoCheckTps implements Runnable {
    public int restartCounter;

    @Override
    public void run() {
        double tps = Lag.getTPS();
        if(tps < TPSRestart.getPlugin(TPSRestart.class).getConfig().getDouble("MinTPS")){
            restartCounter++;
        }
        else {
            restartCounter = 0;
        }

        if(restartCounter >= TPSRestart.getPlugin(TPSRestart.class).getConfig().getDouble("TimeToRestart") / TPSRestart.getPlugin(TPSRestart.class).getConfig().getDouble("CheckInterval")){
            Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "The server has been under 10tps for 10 minutes. A restart will happen in 30 seconds. If you want to stop it, type /TpsStopRestart");
            TPSRestart.getPlugin(TPSRestart.class).setRestartTask(Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TPSRestart.getPlugin(TPSRestart.class), new RestartRunnable(), 600L));
        }
    }
}
