package com.sixpack.hub.events;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

import com.sixpack.hub.Hub;

public class environment implements Listener {

	private Hub plugin = Hub.getInstance();

	private String getTime() {
		String time = plugin.getConfig().getString("AlwaysTime");
		return time;
	}

	private boolean enabledRain() {
		boolean enabled = plugin.getConfig().getBoolean("AntiRain");
		return enabled;
	}

	@EventHandler
	public void onWeather(WeatherChangeEvent e) {
		
		/** dsiabled for now
		
		World w = e.getWorld();
		if (w.hasStorm()) {
			w.setWeatherDuration(0);
			w.setStorm(false);
		}
		if (w.isThundering()) {
			w.setThunderDuration(0);
			w.setThundering(false);
		}
		
		**/
	}

	public void checkTime() {

		if (!getTime().equalsIgnoreCase("none")
				&& (getTime().equalsIgnoreCase("day") || getTime().equalsIgnoreCase("night"))) {
			List<World> world = Bukkit.getWorlds();
			for (World w : world) {
				if (getTime().equalsIgnoreCase("day")) {
					w.setTime(1000);
				} else {
					w.setTime(19000);
				}
				w.setGameRuleValue("doDaylightCycle", "false");
			}
		}

		if (getTime().equalsIgnoreCase("none")) {
			List<World> world = Bukkit.getWorlds();
			for (World w : world) {
				w.setGameRuleValue("doDaylightCycle", "true");
			}
		}
	}

}
