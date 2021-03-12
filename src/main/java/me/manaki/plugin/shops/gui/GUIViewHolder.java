package me.manaki.plugin.shops.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class GUIViewHolder implements InventoryHolder {
	
	private String shopID;
	
	public GUIViewHolder(String shopID) {
		super();
		this.shopID = shopID;
	}
	
	public String getPreviousShopID() {
		return this.shopID;
	}

	@Override
	public Inventory getInventory() {
		return null;
	}
	
}
