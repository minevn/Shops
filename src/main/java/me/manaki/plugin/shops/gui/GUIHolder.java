package me.manaki.plugin.shops.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class GUIHolder implements InventoryHolder {

	private String shopID;
	
	public GUIHolder(String shopID) {
		super();
		this.shopID = shopID;
	}
	
	public String getShopID() {
		return this.shopID;
	}
	
	@Override
	public Inventory getInventory() {
		return null;
	}

}
