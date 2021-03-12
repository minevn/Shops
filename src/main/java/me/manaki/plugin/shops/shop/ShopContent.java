package me.manaki.plugin.shops.shop;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import me.manaki.plugin.shops.configable.Configable;
import me.manaki.plugin.shops.price.PriceType;
import me.manaki.plugin.shops.storage.ItemStorage;

public class ShopContent extends Configable {
	
	public ShopContent(FileConfiguration config, String path) {
		super(config, path);
	}

	private int slot;
	private String itemID;
	private int sellAmount;
	private PriceType priceType;
	private double priceValue;
	private int limit;
	
	public int getSlot() {
		return this.slot;
	}
	
	public String getItemID() {
		return this.itemID;
	}
	
	public int getSellAmount() {
		return this.sellAmount;
	}
	
	public PriceType getPriceType() {
		return this.priceType;
	}
	
	public double getPriceValue() {
		return this.priceValue;
	}

	public int getLimit() {
		return this.limit;
	}
	
	public boolean isLimited() {
		return this.limit > 0;
	}
	
	@Override
	public void load(FileConfiguration config, String path) {
		this.slot = config.getInt(path + ".slot");
		this.itemID = config.getString(path + ".item-id");
		this.sellAmount = config.getInt(path + ".sell-amount");
		this.priceType = PriceType.valueOf(config.getString(path + ".price-type").toUpperCase());
		this.priceValue = config.getDouble(path + ".price-value");
		this.limit = config.getInt(path + ".limit");
		
		// Check itemStack
		if (!ItemStorage.getItemStacks().keySet().contains(this.itemID)) {
			try {
				Material.valueOf(this.itemID.toUpperCase());
			}
			catch (Exception e) {
				System.out.println("[NiceShops] ItemStack of " + path + ".item-id." +  this.itemID + " is wrong");
			}
		}
	}

	@Override
	public void save(FileConfiguration config, String path) {
		config.set(path + ".slot", this.slot);
		config.set(path + ".item-id", this.itemID);
		config.set(path + ".sell-amount", this.sellAmount);
		config.set(path + ".price-type", this.priceType.name());
		config.set(path + ".price-value", this.priceValue);
		config.set(path + ".limit", this.limit);
	}
	
}
