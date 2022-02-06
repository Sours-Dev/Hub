package com.sixpack.hub.events;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.sixpack.hub.Hub;

public class AntiBuild implements Listener {

	private Hub plugin = Hub.getInstance();

	private boolean getEnabled() {
		boolean enabled = plugin.getConfig().getBoolean("AntiBuild");
		return enabled;
	}

	@EventHandler
	public void buildEvent(BlockPlaceEvent e) {

		if (!getEnabled())
			return;

		if (!(e.getPlayer().hasPermission("hub.build") && e.getPlayer().getGameMode() == GameMode.CREATIVE)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void blockMine(BlockBreakEvent e) {

		if (!getEnabled())
			return;

		if (!(e.getPlayer().hasPermission("hub.build") && e.getPlayer().getGameMode() == GameMode.CREATIVE)) {
			e.setCancelled(true);
		}
	}

}
