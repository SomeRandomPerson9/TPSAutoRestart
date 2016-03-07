package com.harry9137.tpsrestart;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class RestartRunnable implements Runnable {
    @Override
    public void run() {
        Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "Restarting Server");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "restart");
    }
}
