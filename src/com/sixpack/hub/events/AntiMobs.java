package com.sixpack.hub.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import com.sixpack.hub.Hub;

public class AntiMobs implements Listener {

	private Hub plugin = Hub.getInstance();

	private boolean getEnabled() {
		boolean enabled = plugin.getConfig().getBoolean("AntiMob");
		return enabled;
	}

	@EventHandler
	public void onMobSpawn(CreatureSpawnEvent e) {
		if (getEnabled()) {
			e.setCancelled(true);
		}
	}
}
