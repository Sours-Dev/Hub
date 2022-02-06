package com.sixpack.hub.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.sixpack.hub.Hub;
import com.sixpack.hub.util.c;

import net.md_5.bungee.api.ChatColor;

public class configLang {

	// Default Lang
	private void defaultLang() {

		setupLang();
		getLang().set("no-perms", "&cNo Permissions");
		getLang().set("Hub.reloaded", "&b[Hub] &fHub config has been reloaded.");


		List<String> help = new ArrayList<String>();
		help.add("&8&m------------------------------------");
		help.add("&8&l* &b/hub reload &fReloads the hub config.");
		help.add("&8&l* &b/hub setspawn &fSets the hubspawn.");
		help.add("&8&l* &b/hub spawn &fTeleports player to spawn.");
		help.add("&8&m------------------------------------");
		getLang().set("Hub.help", help);
		getLang().set("Hub.spawnSet", "&b[Hub] &fYou have updated spawnpoint.");
		getLang().set("Hub.reloaded", "&b[Hub] &fHub has been reloaded.");
		
		
		getLang().set("items.TogglePlayers.Cooldown", "&b[Hub] &fYou still have &c%seconds% &fsecond(s) on your cooldown.");
		getLang().set("items.TogglePlayers.Hide", "&b[Hub] &fYou have &cHIDDEN &fall players.");
		getLang().set("items.TogglePlayers.Show", "&b[Hub] &fYou have &aSHOWN &fall players.");


		saveLang();
	}

	private Hub plugin = Hub.getInstance();

	// -----------------------------------
	// Lang Config
	// -----------------------------------

	// Files & Configs

	public FileConfiguration LangCFG;
	public File LangFile;

	// creating setup

	public void setupLang() {

		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
		}

		LangFile = new File(plugin.getDataFolder(), "Langs.yml");

		if (!LangFile.exists()) {
			try {
				LangFile.createNewFile();
				Bukkit.getServer().getConsoleSender()
						.sendMessage(ChatColor.GOLD + "[Hub] Lang.yml file has been created");
				LangCFG = YamlConfiguration.loadConfiguration(LangFile);

				defaultLang();

			} catch (IOException e) {
				Bukkit.getServer().getConsoleSender()
						.sendMessage(ChatColor.RED + "[Hub] Could not create Lang.yml file");
			}
		}

		LangCFG = YamlConfiguration.loadConfiguration(LangFile);

	}

	public FileConfiguration getLang() {
		return LangCFG;
	}

	public void saveLang() {
		try {
			LangCFG.save(LangFile);
		} catch (IOException e) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "[Hub] Could not save Lang.yml file");
		}
	}

	public void reloadLang() {
		Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "[Hub] Lang.yml file has been reloaded");
		LangCFG = YamlConfiguration.loadConfiguration(LangFile);
	}

	// -----------------------------------
	// end of Lang Config
	// -----------------------------------

}
