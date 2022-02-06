package com.sixpack.hub.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sixpack.hub.config.configData;
import com.sixpack.hub.config.configLang;
import com.sixpack.hub.util.c;

public class spawn {

	public void setSpawn(Player p) {

		String w = p.getWorld().getName();
		double x = p.getLocation().getX();
		double y = p.getLocation().getY();
		double z = p.getLocation().getZ();
		float yaw = p.getLocation().getYaw();
		float pitch = p.getLocation().getPitch();

		configData data = new configData();

		data.setupData();

		data.getData().set("Spawn.w", w);
		data.getData().set("Spawn.x", x);
		data.getData().set("Spawn.y", y);
		data.getData().set("Spawn.z", z);
		data.getData().set("Spawn.yaw", yaw);
		data.getData().set("Spawn.pitch", pitch);

		data.saveData();
		configLang lang = new configLang();
		lang.setupLang();

		p.sendMessage(c.f(lang.getLang().getString("Hub.spawnSet")));

		return;
	}

	public void spawnTP(Player p) {
		configData data = new configData();

		data.setupData();
		if (!data.getData().contains("Spawn.w")) {
			return;
		}


		p.teleport(getSpawnLoction());
	}
	
	public Location getSpawnLoction() {
		configData data = new configData();

		data.setupData();
		World w = Bukkit.getWorld(data.getData().getString("Spawn.w"));
		double x = data.getData().getDouble("Spawn.x");
		double y = data.getData().getDouble("Spawn.y");
		double z = data.getData().getDouble("Spawn.z");
		float yaw = (float) data.getData().getDouble("Spawn.yaw");
		float pitch = (float) data.getData().getDouble("Spawn.pitch");

		Location loc = new Location(w, x, y, z, yaw, pitch);
		
		return loc;
	}
	

}
