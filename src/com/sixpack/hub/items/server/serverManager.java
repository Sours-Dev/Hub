package com.sixpack.hub.items.server;

import java.util.List;


public class serverManager {

	
	private String name;
	private int slot;
	private String itemID;
	private String title;
	private List<String> desc;
	private int onlinePlayers;
	
	public serverManager(String name, int slot, String itemID ,String title, List<String> desc, int onlinePlayers) {
		this.setName(name);
		this.setSlot(slot);
		this.setItemID(itemID);
		this.setTitle(title);
		this.setDesc(desc);
		this.setOnlinePlayers(onlinePlayers);
	}

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	public int getSlot() {
		return slot;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getDesc() {
		return desc;
	}

	public void setDesc(List<String> desc) {
		this.desc = desc;
	}

	public String getItemID() {
		return itemID;
	}

	public void setItemID(String itemID) {
		this.itemID = itemID;
	}

	public int getOnlinePlayers() {
		return onlinePlayers;
	}

	public void setOnlinePlayers(int onlinePlayers) {
		this.onlinePlayers = onlinePlayers;
	}



	
	
}
