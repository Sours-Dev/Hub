package com.sixpack.hub.items.server;

import java.util.List;

import org.bukkit.Bukkit;

import com.sixpack.hub.Hub;
import com.sixpack.hub.util.c;

public class serverEVNT {

	private Hub plugin = Hub.getInstance();
	
	public void setupServers() {
	// name, slot, title, desc
		for(String key : plugin.getConfig().getConfigurationSection("serverSelector.servers").getKeys(false)) {
			String shrt = "serverSelector.servers." + key;
			String name = plugin.getConfig().getString(shrt + ".name");
			String title = plugin.getConfig().getString(shrt + ".title");
			int slot = plugin.getConfig().getInt(shrt + ".slot");
			String blockID = plugin.getConfig().getString(shrt + ".itemid");
			List<String> desc = plugin.getConfig().getStringList(shrt + ".desc");
		
			serverManager sMan = new serverManager(name, slot, blockID,  title, desc, 0);
			plugin.getServers().add(sMan);
			Bukkit.getConsoleSender().sendMessage(c.f("&c[Hub] &7Added server: " + name));
		}
		
		
	}
}
