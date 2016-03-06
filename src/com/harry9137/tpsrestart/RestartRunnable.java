package com.harry9137.tpsrestart;

import org.bukkit.Bukkit;

public class RestartRunnable implements Runnable {
    @Override
    public void run() {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "/restart");
    }
}
