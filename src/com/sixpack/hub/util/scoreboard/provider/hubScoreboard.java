package com.sixpack.hub.util.scoreboard.provider;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.sixpack.hub.Hub;
import com.sixpack.hub.util.c;
import com.sixpack.hub.util.scoreboard.utils.ScoreboardProvider;

import net.milkbowl.vault.chat.Chat;






public class hubScoreboard implements ScoreboardProvider{
	private static hubScoreboard instance;
	private Hub plugin = Hub.getInstance();
	
	
	
    public String getTitle(Player player) {
    	String title = plugin.getConfig().getString("Scoreboard.title").replaceAll("%totalOnline%", String.valueOf(Bukkit.getOnlinePlayers().size()));
        return c.f(title);
    }


    public String getHeader() {
    	String header = plugin.getConfig().getString("Scoreboard.header");
        return c.f(header);
    }


    public String getFooter() {
    	String footer = plugin.getConfig().getString("Scoreboard.footer");
        return c.f(footer);
    }


    /**
     * 	API:
     * 
     * 	%totalOnline%
     * 	%kd%
     *  %kills%
     *  %deaths%
     *  %money%
     *  %rank
     *  %faction% 
     *  %factionOnline%
     */
    
    
	public List<String> getLinesFor(Player p) {
    List<String> lines = new ArrayList<>();
    	
    	List<String> scoreboard = plugin.getConfig().getStringList("Scoreboard.section");
    	for(String line : scoreboard) {
    		lines.add(c.f(returnAPI(p, line)));

    	}
    
    	

    	

		return lines;		
	}
	public static ScoreboardProvider getInstance() {
		return (instance == null  ? new hubScoreboard() : instance);
	}


	
	private String returnAPI(Player p, String line) {
		String rank = "";
		
		
		
		if(plugin.getChat().getGroupPrefix(p.getWorld(), plugin.getPerms().getPrimaryGroup(p)) == "") {
			rank = plugin.getPerms().getPrimaryGroup(p);					
		}else {		
		rank = plugin.getChat().getGroupPrefix(p.getWorld(), plugin.getPerms().getPrimaryGroup(p));
			for (int i = 0; i < rank.length(); i++){
			    char c = rank.charAt(i);    
			    String str = String.valueOf(c);		    
			    
			   if(Character.isLetter(c) && Character.isLetter(rank.charAt(i + 1)) && rank.charAt(i - 1) != '&') {
				   rank = rank.substring(i-2);
			   }
			   
			   if(c == '}') {
				   rank = rank.replaceAll("}", "&c");  
			   }
			   
			}
			rank = rank.replaceAll("]", "&c");
			
			
		}
		

		rank = rank.substring(0, 1).toUpperCase() + rank.substring(1);
		
		plugin.updateTotalPlayers(p);



		

		
		
		return line.replaceAll("%totalOnline%", String.valueOf(plugin.totalPlayers))
				.replaceAll("%rank%", rank);

	
	}

}
