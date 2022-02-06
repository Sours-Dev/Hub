package com.sixpack.hub.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.sixpack.hub.Hub;
import com.sixpack.hub.config.configLang;
import com.sixpack.hub.items.server.serverManager;
import com.sixpack.hub.util.c;






public class serverSelector implements Listener{

	private Hub plugin = Hub.getInstance();
	private configLang lang = new configLang();
	
	
	// Vars

	private String getTitleMenu() {
		return plugin.getConfig().getString("serverSelector.title");
	}
	private String getFillerID() {
		return plugin.getConfig().getString("serverSelector.filler");
	}
	private boolean getEnchantedMenu() {
		return plugin.getConfig().getBoolean("serverSelector.enchanted");
	}
	
	
	// item
	private boolean getItemEnabled() {
		return plugin.getConfig().getBoolean("HubSelector.ItemEnabled");
	}
	private String getItemTitle() {
		return plugin.getConfig().getString("HubSelector.ItemName");
	}
	public boolean getEnchantedItem() {
		return plugin.getConfig().getBoolean("HubSelector.ItemEnchanted");
	}
	private String getItemID() {
		return plugin.getConfig().getString("HubSelector.ItemID");
	}
	
	private List<String> getItemDesc() {
		return plugin.getConfig().getStringList("HubSelector.ItemDesc");
	}
	private int getItemSlot() {
		return plugin.getConfig().getInt("HubSelector.ItemSlot") - 1;
	}
	
	private boolean getItemInHand() {
		return plugin.getConfig().getBoolean("HubSelector.ItemInHand");
	}
	
	//to do
	public void getServersInfo() {
		
	}
	
	
	public void getItem(Player p) {
		if (!getItemEnabled()) {
			return;
		}
		
		
		p.getInventory().setItem(getItemSlot(), getItem());
		if(getItemInHand()) {
			p.getInventory().setHeldItemSlot(getItemSlot());
		}
		
	}
	
	
	private ItemStack getItem() {
		ItemStack item;
		if (getItemID().contains(":")) {
			String[] id = getItemID().split(":");
			item = new ItemStack(Material.getMaterial(Integer.parseInt(id[0])), 1, (byte) Byte.parseByte(id[1]));
		} else {
			item = new ItemStack(Material.getMaterial(Integer.parseInt(getItemID())), 1);
		}
				
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(c.f(getItemTitle()));;
		if (getEnchantedItem()) {
			itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
			itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		if (!getItemDesc().isEmpty()) {
			// fixs to add color messages
			List<String> rLore = new ArrayList<>();
			for (int i = 0; i < getItemDesc().size(); i++) {
				rLore.add(i, c.f(getItemDesc().get(i)));
			}
			itemMeta.setLore(rLore);
		}
		item.setItemMeta(itemMeta);
		
		return item;
		
	}
	
	@EventHandler
	public void rightClick(PlayerInteractEvent  e) {
		Player p = e.getPlayer();
		if(e.getItem() == null) {
			return;
		}
		

		if(e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(getItem().getItemMeta().getDisplayName())){
			openGUI(p);
		}
		
	}
	
	private void openGUI(Player p) {
		
		
		Inventory inv = Bukkit.createInventory(null, 27, c.f(getTitleMenu()));

		//Filler
		ItemStack filler;
		
		String fillerid = plugin.getConfig().getString("serverSelector.filler");
		
		if (fillerid.contains(":")) {
			String[] id = fillerid.split(":");
			filler = new ItemStack(Material.getMaterial(Integer.parseInt(id[0])), 1, (byte) Byte.parseByte(id[1]));
		} else {
			filler = new ItemStack(Material.getMaterial(Integer.parseInt(fillerid)), 1);
		}
		
		ItemMeta fillerMeta = filler.getItemMeta();
		fillerMeta.setDisplayName(c.f("&c"));
		filler.setItemMeta(fillerMeta);
		for(int i = 0; i<27; i++) {
			inv.setItem(i, filler);
		}
		
		ItemStack server;
		ItemMeta serverMeta;
		for(serverManager sMan : plugin.getServers()) {
			plugin.updateServerPlayers(p, sMan.getName());
			int onlinePlayers = sMan.getOnlinePlayers();
			
			if(sMan.getItemID().contains(":")) {
				String[] id = sMan.getItemID().split(":");
				server = new ItemStack(Material.getMaterial(Integer.parseInt(id[0])), 1, (byte) Byte.parseByte(id[1]));
			}else {
				server = new ItemStack(Material.getMaterial(Integer.parseInt(sMan.getItemID())));
			}
			
			serverMeta = server.getItemMeta();
			serverMeta.setDisplayName(c.f(sMan.getTitle()));
			if(getEnchantedMenu()) {
				serverMeta.addEnchant(Enchantment.DURABILITY, 1, true);
				serverMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			}
			List<String> newLore = new ArrayList<String>();

			
			for(String l : sMan.getDesc()) {
				
				
				
				newLore.add(c.f(l.replaceAll("%online%", String.valueOf(onlinePlayers))));
			}
			serverMeta.setLore(newLore);
			server.setItemMeta(serverMeta);
			inv.setItem(sMan.getSlot() - 1, server);
		}
		
		p.openInventory(inv);
	}
	
	@EventHandler
	public void onInventoryCLICK(InventoryClickEvent e) {
		lang.setupLang();
	
		if (e.getInventory().getName().equalsIgnoreCase(c.f(getTitleMenu()))) {	
			Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			if(e.getCurrentItem() == null) {
				return;
			}
			
			if(!e.getCurrentItem().hasItemMeta()) {
				return;
			}
			
			for(serverManager sMan : plugin.getServers()) {
				if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(c.f(sMan.getTitle()))) {
					plugin.connectServer(p, sMan.getName());
					p.closeInventory();
					return;
				}
			}
			
			
			return;
		}
	
	}
	

}
