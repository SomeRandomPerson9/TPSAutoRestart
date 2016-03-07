package com.harry9137.tpsrestart;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class AutoCheckTps implements Runnable {

    @Override
    public void run() {
        double tps = Lag.getTPS();
        if(tps < TPSRestart.getPlugin(TPSRestart.class).getConfig().getDouble("MinTPS")){
            TPSRestart.restartCounter++;
        }
        else {
            TPSRestart.restartCounter = 0;
        }

        if(TPSRestart.restartCounter >= TPSRestart.getPlugin(TPSRestart.class).getConfig().getDouble("TimeToRestart") / TPSRestart.getPlugin(TPSRestart.class).getConfig().getDouble("CheckInterval")){
            Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "The server has been under " + TPSRestart.getPlugin(TPSRestart.class).getConfig().getString("MinTPS") + " TPS for " + TPSRestart.getPlugin(TPSRestart.class).getConfig().getString("TimeToRestart") + " minutes. A restart will happen in 30 seconds. If you want to stop it, type /TpsStopRestart");
            TPSRestart.getPlugin(TPSRestart.class).setRestartTask(Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TPSRestart.getPlugin(TPSRestart.class), new RestartRunnable(), (long)(600L * (Lag.getTPS()/20))));
        }

        for(CommandSender sender : TPSRestart.getPlugin(TPSRestart.class).debug){
            sender.sendMessage("TPS Auto Check:");
            sender.sendMessage("TPS: " + TPSRestart.colorTPS(tps));
            sender.sendMessage("Restart Counter: " + TPSRestart.restartCounter);
            sender.sendMessage("Would Restart in " + (long)(600L * TPSRestart.round(Lag.getTPS()/20, 4)) + " ticks");
        }
    }
}
