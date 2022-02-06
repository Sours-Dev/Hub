package com.sixpack.hub.events;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import com.sixpack.hub.Hub;
import com.sixpack.hub.items.playerToggle;
import com.sixpack.hub.items.serverSelector;
import com.sixpack.hub.util.scoreboard.PlayerBoard;
import com.sixpack.hub.util.scoreboard.provider.hubScoreboard;



public class playerJoin implements Listener {

	private Hub plugin = Hub.getInstance();

	private int getHealth() {
		int health = plugin.getConfig().getInt("Health");
		return health;
	}

	private int getSpeed() {
		int speed = plugin.getConfig().getInt("Speed");
		return speed;
	}

	private int getJumpBoost() {
		int jump = plugin.getConfig().getInt("JumpBoost");
		return jump;
	}

	private String getJoinMSG() {
		String msg = plugin.getConfig().getString("JoinMessage");
		return msg;
	}

	private String getQuitMSG() {
		String msg = plugin.getConfig().getString("QuitMessage");
		return msg;
	}

	private boolean Scoreboard() {
		boolean enabled = plugin.getConfig().getBoolean("Scoreboard.enabled");
		return enabled;
	}
	
	private boolean changeMode() {
		boolean mode = plugin.getConfig().getBoolean("ChangeGameMode");
		return mode;
	}

	private boolean clearInv() {
		boolean clear = plugin.getConfig().getBoolean("ClearInvOnJoin");
		return clear;
	}

	private boolean spawnJoin() {
		boolean spawn = plugin.getConfig().getBoolean("SpawnonJoin");
		return spawn;
	}

	private playerToggle playerToggle = new playerToggle();
	private serverSelector serverSelector = new serverSelector();
	
	
	@EventHandler
	public void test(PlayerSpawnLocationEvent e) {
		if (spawnJoin()) {
			spawn spawn = new spawn();
			Location loc  = spawn.getSpawnLoction();
			e.setSpawnLocation(loc);
		}


	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {

		Player p = e.getPlayer();
		

		
		p.setFoodLevel(20);
		for (Player players : Bukkit.getOnlinePlayers()) {
			if (playerToggle.status.contains(players)) {
				players.hidePlayer(p);
			}
		}

		if (clearInv()) {
			p.getInventory().clear();
		}

		if (!getJoinMSG().equals("null")) {
			String msg = getJoinMSG();
			msg = msg.replaceAll("%player%", p.getName());
			e.setJoinMessage(msg);
		} else {
			e.setJoinMessage(null);
		}
		;

		if (changeMode())
			p.setGameMode(GameMode.SURVIVAL);

		p.setHealth(getHealth());
		p.setMaxHealth(getHealth());

		if (getSpeed() > 0) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000000, getSpeed()));
		}

		if (getJumpBoost() > 0) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 1000000000, getJumpBoost()));
		}

		
		playerToggle.getItemHide(p);
		serverSelector.getItem(p);
		
		if (spawnJoin()) {
			spawn spawn = new spawn();
			spawn.spawnTP(p);
		}

		if(Scoreboard()) {
			PlayerBoard.get(p).setProvider(hubScoreboard.getInstance());
		}
		
		
		
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {

		Player p = e.getPlayer();

		if (playerToggle.status.contains(p)) {
			playerToggle.status.remove(p);
		}

		if (playerToggle.onCoolDown(p)) {
			playerToggle.removeCoolDown(p);
		}

		if (!getJoinMSG().equals("null")) {
			String msg = getQuitMSG();
			msg = msg.replaceAll("%player%", p.getName());
			e.setQuitMessage(msg);
		} else {
			e.setQuitMessage(null);
		}

		if(Scoreboard()) {
			PlayerBoard.get(p).dispose(p);;
		}
		
	}


	
}
