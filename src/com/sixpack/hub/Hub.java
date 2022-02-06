package com.sixpack.hub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.sixpack.hub.commands.hubAdminCMD;
import com.sixpack.hub.config.configLang;
import com.sixpack.hub.events.AntiBuild;
import com.sixpack.hub.events.AntiHunger;
import com.sixpack.hub.events.AntiItems;
import com.sixpack.hub.events.AntiMobs;
import com.sixpack.hub.events.DoubleJump;
import com.sixpack.hub.events.environment;
import com.sixpack.hub.events.playerJoin;

import com.sixpack.hub.items.playerToggle;
import com.sixpack.hub.items.serverSelector;
import com.sixpack.hub.items.server.serverEVNT;
import com.sixpack.hub.items.server.serverManager;
import com.sixpack.hub.util.c;
import com.sixpack.hub.util.scoreboard.PlayerBoard;
import com.sixpack.hub.util.scoreboard.provider.hubScoreboard;
import com.sixpack.hub.util.scoreboard.task.ScoreboardUpdateTask;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;


public class Hub extends JavaPlugin implements PluginMessageListener {

	private static Hub instance;
	private configLang lang;
	private playerToggle playerToggle = new playerToggle();
    private static Permission perms = null;
    public static int totalPlayers = 0;
    private static Chat chat = null;
    private ArrayList<serverManager> servers;
    
	@Override
	public void onEnable() {
		// sets instance and rulls all voids
		instance = this;
		registerLang();
		registerListeners();
		commands();
		this.saveDefaultConfig();
		servers = new ArrayList<>();
		  this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		   this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
		
			if(!setupPermissions()) {
				Bukkit.getConsoleSender().sendMessage(c.f("&c&l[Hub] &f&lNo vault installed"));
				Bukkit.getPluginManager().disablePlugin(this);	
				return;
			}
			
			if(!setupChat()) {
				Bukkit.getConsoleSender().sendMessage(c.f("&c&l[Hub] &f&lNo vault installed"));
				Bukkit.getPluginManager().disablePlugin(this);	
				return;
			}
			
			
			serverEVNT serverEVNT = new serverEVNT();
			serverEVNT.setupServers();
			
		   
			// do time
		

		if(this.getConfig().getBoolean("Scoreboard.enabled")) {
			for(Player p : Bukkit.getOnlinePlayers()) {
				 PlayerBoard.get(p).setProvider(hubScoreboard.getInstance());
			}
		}

		 if(this.getConfig().getBoolean("Scoreboard.enabled")) {
			 new ScoreboardUpdateTask().runTaskTimerAsynchronously(this, 1, 1);
		 }
		
		 
		 
		PluginDescriptionFile pdf = this.getDescription();		
		Bukkit.getConsoleSender().sendMessage(c.f("&c&l[Hub] &f&l Hub plugin has been enabled &7(ver:" + pdf.getVersion() + ")" ));
		

		
	}

	@Override
	public void onDisable() {

		 PlayerBoard.all().clear();

		PluginDescriptionFile pdf = this.getDescription();
		Bukkit.getConsoleSender().sendMessage(c.f("&c&l[Hub] &f&l Hub plugin has been disabled &7(ver:" + pdf.getVersion() + ")" ));
		registerLang();
		this.saveDefaultConfig();
	
	}

	public void registerLang() {
		lang = new configLang();// wrong thing xd
		lang.setupLang();
		lang.saveLang();
	}

	public void commands() {
		getCommand("hubadmin").setExecutor(new hubAdminCMD());
	}

	public void registerListeners() {
		PluginManager pm = getServer().getPluginManager();

		pm.registerEvents(new AntiBuild(), this);
		pm.registerEvents(new AntiHunger(), this);
		pm.registerEvents(new DoubleJump(), this);
		pm.registerEvents(new AntiItems(), this);
		pm.registerEvents(new playerJoin(), this);
		pm.registerEvents(new AntiMobs(), this);
		pm.registerEvents(new environment(), this);
		pm.registerEvents(new playerToggle(), this);
		pm.registerEvents(new serverSelector(), this);
	}

	public static Hub getInstance() {
		return instance;
	}

	public String getTime() {
		String time = this.getConfig().getString("AlwaysTime");
		return time;
	}

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
    
    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
      if (!channel.equals("BungeeCord")) {
        return;
      }
      ByteArrayDataInput in = ByteStreams.newDataInput(message);
      String subchannel = in.readUTF();
      
      if (subchannel.equals("PlayerCount")) {
          String server = in.readUTF();
          int playerCount = in.readInt();
          if(server.equalsIgnoreCase("ALL")) {
              totalPlayers = playerCount;
          }
         
         for(serverManager sMan : this.servers) {
        	 if(server.equalsIgnoreCase(sMan.getName())) {
        		 sMan.setOnlinePlayers(playerCount);
        	 }
         }                 
      }

    }
    
    public void updateTotalPlayers(Player player) {

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PlayerCount");
        out.writeUTF("ALL");

         player.sendPluginMessage(this, "BungeeCord", out.toByteArray());

    }
    
    public void connectServer(Player player, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);

         player.sendPluginMessage(this, "BungeeCord", out.toByteArray());	
    }
    
    public void updateServerPlayers(Player player, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PlayerCount");
        out.writeUTF(server);

         player.sendPluginMessage(this, "BungeeCord", out.toByteArray());	
    }
    
    
    public static Chat getChat() {
        return chat;
    }
    
    public static Permission getPerms() {
        return perms;
    }

    public ArrayList<serverManager> getServers(){
    	return this.servers;
    }
    
}

/**
 * All rights go to 6pack
 */