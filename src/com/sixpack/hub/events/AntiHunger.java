package com.sixpack.hub.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import com.sixpack.hub.Hub;

public class AntiHunger implements Listener {

	private Hub plugin = Hub.getInstance();

	private boolean getEnabledH() {
		boolean enabled = plugin.getConfig().getBoolean("AntiHunger");
		return enabled;
	}

	@EventHandler
	public void foodChange(FoodLevelChangeEvent e) {
		if (!getEnabledH())
			return;
		e.setCancelled(true);
	}

	// anti pvp

	public boolean getEnabledP() {
		boolean enabled = plugin.getConfig().getBoolean("AntiPvP");
		return enabled;
	}

	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if (!getEnabledP())
			return;
		e.setCancelled(true);
	}
}
