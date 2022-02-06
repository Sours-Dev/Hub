package com.sixpack.hub.config;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.sixpack.hub.Hub;

import net.md_5.bungee.api.ChatColor;

public class configData {

	private Hub plugin = Hub.getPlugin(Hub.class);

	// -----------------------------------
	// Data Config
	// -----------------------------------

	// Files & Configs

	public FileConfiguration DataCFG;
	public File DataFile;

	// creating setup

	public void setupData() {

		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
		}

		DataFile = new File(plugin.getDataFolder(), "Data.yml");

		if (!DataFile.exists()) {
			try {
				DataFile.createNewFile();
				Bukkit.getServer().getConsoleSender()
						.sendMessage(ChatColor.GREEN + "[Hub] Data.yml file has been created");
				DataCFG = YamlConfiguration.loadConfiguration(DataFile);

			} catch (IOException e) {
				Bukkit.getServer().getConsoleSender()
						.sendMessage(ChatColor.RED + "[Hub] Could not create Data.yml file");
			}
		}

		DataCFG = YamlConfiguration.loadConfiguration(DataFile);

	}

	public FileConfiguration getData() {
		return DataCFG;
	}

	public void saveData() {
		try {
			DataCFG.save(DataFile);
		} catch (IOException e) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[Hub] Could not save Data.yml file");
		}
	}

	public void reloadData() {
		Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "[Hub] Data.yml file has been reloaded");
		DataCFG = YamlConfiguration.loadConfiguration(DataFile);
	}

	// -----------------------------------
	// end of Data Config
	// -----------------------------------

}
