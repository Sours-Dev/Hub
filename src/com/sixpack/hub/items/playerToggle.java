package com.sixpack.hub.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Dye;
import org.bukkit.scheduler.BukkitRunnable;

import com.sixpack.hub.Hub;
import com.sixpack.hub.config.configLang;
import com.sixpack.hub.util.c;

public class playerToggle implements Listener {

	private Hub plugin = Hub.getInstance();

	public static ArrayList<Player> status = new ArrayList<>();
	private HashMap<Player, Integer> cooldown = new HashMap<>();
	private HashMap<Player, BukkitRunnable> cooldownTask = new HashMap<>();
	private configLang lang = new configLang();

	private boolean getEnabled() {
		boolean enabled = plugin.getConfig().getBoolean("PlayerToggle.ItemEnabled");
		return enabled;
	}

	private int getMaxCoolDown() {
		int cool = plugin.getConfig().getInt("PlayerToggle.CoolDown");
		return cool;
	}

	private int getSlot() {
		int slot = plugin.getConfig().getInt("PlayerToggle.ItemSlot");
		return slot - 1;
	}

	// Hide palyers

	private String getHideID() {
		String id = plugin.getConfig().getString("PlayerToggle.HidePlayers.ItemID");
		return id;
	}

	private String getHideName() {
		String name = plugin.getConfig().getString("PlayerToggle.HidePlayers.ItemName");
		return name;
	}

	private List<String> getHideDesc() {
		List<String> desc = plugin.getConfig().getStringList("PlayerToggle.HidePlayers.ItemDesc");
		return desc;
	}

	private boolean getHideEnchanted() {
		boolean enchanted = plugin.getConfig().getBoolean("PlayerToggle.HidePlayers.ItemEnchanted");
		return enchanted;
	}

	// Show Players

	private String getShowID() {
		String id = plugin.getConfig().getString("PlayerToggle.ShowPlayers.ItemID");
		return id;
	}

	private String getShowName() {
		String name = plugin.getConfig().getString("PlayerToggle.ShowPlayers.ItemName");
		return name;
	}

	private List<String> getShowDesc() {
		List<String> desc = plugin.getConfig().getStringList("PlayerToggle.ShowPlayers.ItemDesc");
		return desc;
	}

	private boolean getShowEnchanted() {
		boolean enchanted = plugin.getConfig().getBoolean("PlayerToggle.ShowPlayers.ItemEnchanted");
		return enchanted;
	}

	public void getItemHide(Player p) {
		if (!getEnabled())
			return;
		ItemStack hide;
		if (getHideID().contains(":")) {
			String[] id = getHideID().split(":");
			hide = new ItemStack(Material.getMaterial(Integer.parseInt(id[0])), 1, (byte) Byte.parseByte(id[1]));
		} else {
			hide = new ItemStack(Material.getMaterial(Integer.parseInt(getHideID())), 1);
		}

		ItemMeta hideMeta = hide.getItemMeta();

		hideMeta.setDisplayName(c.f(getHideName()));

		if (!getHideDesc().isEmpty()) {
			// fixs to add color messages
			List<String> rLore = new ArrayList<>();
			for (int i = 0; i < getHideDesc().size(); i++) {
				rLore.add(i, c.f(getHideDesc().get(i)));
			}
			hideMeta.setLore(rLore);
		}

		if (getHideEnchanted()) {
			hideMeta.addEnchant(Enchantment.DURABILITY, 1, true);
			hideMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		hide.setItemMeta(hideMeta);

		p.getInventory().setItem(getSlot(), hide);

		// show the players

		for (Player players : Bukkit.getOnlinePlayers()) {
			p.showPlayer(players);
		}
		status.remove(p);
		lang.setupLang();
		p.sendMessage(c.f(lang.getLang().getString("items.TogglePlayers.Show")));
	}

	public void getItemShow(Player p) {

		if (!getEnabled()) {
			return;
		}
		ItemStack show;
		if (getShowID().contains(":")) {
			String[] id = getShowID().split(":");
			show = new ItemStack(Material.getMaterial(Integer.parseInt(id[0])), 1, (byte) Byte.parseByte(id[1]));
		} else {
			show = new ItemStack(Material.getMaterial(Integer.parseInt(getShowID())), 1);
		}

		ItemMeta showMeta = show.getItemMeta();

		showMeta.setDisplayName(c.f(getShowName()));

		if (!getShowDesc().isEmpty()) {
			// fixs to add color messages
			List<String> rLore = new ArrayList<>();
			for (int i = 0; i < getShowDesc().size(); i++) {
				rLore.add(i, c.f(getShowDesc().get(i)));
			}
			showMeta.setLore(rLore);
		}

		if (getShowEnchanted()) {
			showMeta.addEnchant(Enchantment.DURABILITY, 1, true);
			showMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		show.setItemMeta(showMeta);

		p.getInventory().setItem(getSlot(), show);
		// hide players
		for (Player players : Bukkit.getOnlinePlayers()) {
			p.hidePlayer(players);
		}

		status.add(p);

		lang.setupLang();
		p.sendMessage(c.f(lang.getLang().getString("items.TogglePlayers.Hide")));

	}

	@EventHandler
	public void rightClick(PlayerInteractEvent e) {

		if (!getEnabled())
			return;

		Player p = e.getPlayer();

		if (e.getItem() == null) {
			return;
		}

		if (e.getAction() != Action.RIGHT_CLICK_AIR) {
			return;
		}

		ItemStack item = p.getItemInHand();

		if (!getShowID().contains(":")) {
			if (item.getTypeId() == Integer.parseInt(getShowID())) {
				if (onCoolDown(p)) {
					return;
				}
				getItemHide(p);
				addCooldown(p);
				return;
			}
		}

		if (!getHideID().contains(":")) {
			if (item.getTypeId() == Integer.parseInt(getHideID())) {
				if (onCoolDown(p)) {
					return;
				}
				getItemShow(p);
				addCooldown(p);
				return;
			}

		}

		String[] id = getShowID().split(":");

		if (item.getData().getData() == Byte.parseByte(id[1])) {
			if (onCoolDown(p)) {
				return;
			}
			getItemHide(p);
			addCooldown(p);
		}
		id = getHideID().split(":");
		if (item.getData().getData() == Byte.parseByte(id[1])) {
			if (onCoolDown(p)) {
				return;
			}
			getItemShow(p);


			addCooldown(p);
		}

	}

	public boolean onCoolDown(Player p) {
		if (cooldown.containsKey(p)) {
			lang.setupLang();
			String msg = lang.getLang().getString("items.TogglePlayers.Cooldown");
			msg = msg.replaceAll("%seconds%", String.valueOf(cooldown.get(p)));
			p.sendMessage(c.f(msg));
			return true;
		}
		return false;
	}

	public void removeCoolDown(Player p) {
		cooldown.remove(p);
	}

	private void addCooldown(Player p) {

		if(cooldown == null) {
			cooldown = new HashMap<>();
		}
		if(cooldownTask == null) {
			cooldownTask = new HashMap<>();
		}
		if(cooldown.containsKey(p)) {
			cooldown.remove(p);
		}
		if(cooldownTask.containsKey(p)) {
			cooldownTask.remove(p);
		}
		cooldown.put(p, getMaxCoolDown());
		cooldownTask.put(p, new BukkitRunnable() {
			public void run() {
				cooldown.put(p, cooldown.get(p) - 1);
				if (cooldown.get(p) == 0) {
					cooldown.remove(p);
					cooldownTask.remove(p);
					cancel();
				}
			}
		});

		cooldownTask.get(p).runTaskTimer(plugin, 20, 20);
	}

}
