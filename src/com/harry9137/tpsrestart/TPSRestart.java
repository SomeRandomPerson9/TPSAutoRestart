package com.harry9137.tpsrestart;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TPSRestart extends JavaPlugin {

    public int restartTask;
    private int restartVoters;
    private AutoCheckTps autoCheckTps;
    protected boolean isRestartVoteActive;

    public void onEnable(){
        this.saveDefaultConfig();

        getLogger().info("TPSRestart has been enabled");
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Lag(), 100L, 1L);
        autoCheckTps = new AutoCheckTps();
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, autoCheckTps, this.getConfig().getLong("CheckInterval")*20*60, this.getConfig().getLong("CheckInterval")*20*60);
    }


    public void onDisable(){
        getLogger().info("TPSRestart has been disabled");
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(sender.hasPermission("tpsrestart.commands." + label)){
            if(label.equalsIgnoreCase("SetTimeToRestart")){
                if(args.length == 1) {
                    getConfig().set("TimeToRestart", args[0]);
                    saveConfig();
                    sender.sendMessage("The server will now restart if the tps is " + getConfig().getString("MinTPS") +  " for " + String.valueOf(args[0]) + " minutes.");
                    return true;
                }
                else{
                    sender.sendMessage("Usage: /" + label + " [Minutes]");
                }

            }
        }
        else{
            sender.sendMessage("You don't have permission to use this command. Please contact an admin for more information");
        }

        if(label.equalsIgnoreCase("tps")){
            sender.sendMessage(ChatColor.YELLOW + "The current TPS is " +  String.valueOf(Lag.getTPS()));
            return true;
        }
        else if(label.equalsIgnoreCase("TpsStopRestart")){
            if(restartTask != 0) {
                Bukkit.getServer().getScheduler().cancelTask(restartTask);
                Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "The server restart has been canceled by " + sender.getName());
            }else{
                sender.sendMessage(ChatColor.YELLOW + "No restart has been scheduled.");
            }
            autoCheckTps.restartCounter = 0;
            return true;
        }
        else if(label.equalsIgnoreCase("StartRestartVote")){
            if(!isRestartVoteActive) {
                isRestartVoteActive = true;
                restartVoters = 0;
                Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + sender.getName() + " has started a vote to restart the server.");
                Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "Type /RestartVote to vote for the server to restart. 50% of players are needed");
            }
            else{
                sender.sendMessage(ChatColor.YELLOW + "Restart vote already active. Type /RestartVote to vote.");
            }
            return true;
        }
        else if(label.equalsIgnoreCase("RestartVote")){
            if(isRestartVoteActive) {
                restartVoters++;
                Bukkit.broadcastMessage(sender.getName() + ChatColor.YELLOW + " has voted for a server restart. " + String.valueOf(restartVoters) + "/" + String.valueOf(Bukkit.getServer().getOnlinePlayers().size()) + "(" + String.valueOf(round(Double.valueOf(restartVoters) / Double.valueOf(Bukkit.getServer().getOnlinePlayers().size()), 2) * 100) + ")");
                if (Double.valueOf(restartVoters) / Double.valueOf(Bukkit.getServer().getOnlinePlayers().size()) >= .5d) {
                    Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "50% of players have voted. The server will restart in 30 seconds. If you need the server to not restart, do /TpsStopRestart");
                    this.setRestartTask(Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new RestartRunnable(), 600L));
                }
            }
            else{
                sender.sendMessage(ChatColor.YELLOW + "Restart vote not active. To start one type /StartRestartVote");
            }
            return true;
        }

        return false;
    }

    public int getRestartTask() {
        return restartTask;
    }

    public void setRestartTask(int restartTask) {
        this.restartTask = restartTask;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
