package com.sixpack.hub.events;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import com.sixpack.hub.Hub;

public class AntiItems implements Listener {

	private Hub plugin = Hub.getInstance();

	private boolean getEnabledPick() {
		boolean enabled = plugin.getConfig().getBoolean("AntiPickUp");
		return enabled;
	}

	private boolean getEnabledDrop() {
		boolean enabled = plugin.getConfig().getBoolean("AntiDrop");
		return enabled;
	}

	private boolean getEnabledInv() {
		boolean enabled = plugin.getConfig().getBoolean("MoveInventory");
		return enabled;
	}

	@EventHandler
	public void invMove(InventoryClickEvent e) {

		if (!getEnabledInv())
			return;

		if (!(e.getWhoClicked().hasPermission("hub.build") && e.getWhoClicked().getGameMode() == GameMode.CREATIVE)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void pickup(PlayerPickupItemEvent e) {

		if (!getEnabledPick())
			return;

		if (!(e.getPlayer().hasPermission("hub.build") && e.getPlayer().getGameMode() == GameMode.CREATIVE)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void drop(PlayerDropItemEvent e) {

		if (!getEnabledDrop())
			return;

		if (!(e.getPlayer().hasPermission("hub.build") && e.getPlayer().getGameMode() == GameMode.CREATIVE)) {
			e.setCancelled(true);
		}
	}

}
