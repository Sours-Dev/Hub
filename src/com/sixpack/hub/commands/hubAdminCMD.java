package com.sixpack.hub.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sixpack.hub.Hub;
import com.sixpack.hub.config.configLang;
import com.sixpack.hub.events.spawn;
import com.sixpack.hub.util.c;

public class hubAdminCMD implements CommandExecutor{

	private Hub plugin = Hub.getInstance();
	private configLang lang = new configLang();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		
		if(!(sender instanceof Player)) {
			Bukkit.getConsoleSender().sendMessage(c.f("&c&l[Hub] &f&lThis command is for players only"));
			return true;
		}
		
		Player p = (Player) sender;
		
		/**
		 *  Commands
		 *  
		 *  setspawn
		 *  spawn
		 *  reload
		 *  
		 */
	
		lang.setupLang();
		if(!p.hasPermission("hub.admin")) {
			p.sendMessage(c.f(lang.getLang().getString("no-perms")));
		}
		
		if(args.length == 1) {
			if(args[0].equalsIgnoreCase("setspawn")) {
				spawn spawn = new spawn();
				spawn.setSpawn(p);			
				return true;
			}
			if(args[0].equalsIgnoreCase("spawn")) {
				spawn spawn = new spawn();
				spawn.spawnTP(p);			
				return true;
			}
			if(args[0].equalsIgnoreCase("reload")) {
				Bukkit.getServer().getPluginManager().disablePlugin(plugin);
				Bukkit.getServer().getPluginManager().enablePlugin(plugin);
				p.sendMessage(c.f(lang.getLang().getString("Hub.reloaded")));
				return true;
			}
			if(args[0].equalsIgnoreCase("help")){
				hubadminHelp(p);
			}
		}
		
		hubadminHelp(p);
		
		
		return true;
	}
	
	
	private void hubadminHelp(Player p) {
		lang.setupLang();
		List<String> helpmsg = lang.getLang().getStringList("Hub.help");
		for(String l : helpmsg) {
			p.sendMessage(c.f(l));
		}
		
	}
	

}
