package me.manaki.plugin.shops.shop;

import org.bukkit.configuration.file.FileConfiguration;

import me.manaki.plugin.shops.configable.Configable;

public class ShopContentData extends Configable {
	
	private int sold;
	
	public ShopContentData(int sold) {
		this.sold = sold;
	}
	
	public ShopContentData(FileConfiguration config, String path) {
		super(config, path);
	}
	
	public int getSold() {
		return this.sold;
	}

	public void setSold(int sold) {
		this.sold = sold;
	}
	
	@Override
	public void load(FileConfiguration config, String path) {
		this.sold = config.getInt(path + ".sold");
	}

	@Override
	public void save(FileConfiguration config, String path) {
		config.set(path + ".sold", this.sold);
	}
	
}
