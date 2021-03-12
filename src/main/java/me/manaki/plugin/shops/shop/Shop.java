package me.manaki.plugin.shops.shop;

import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;

import com.google.common.collect.Maps;

import me.manaki.plugin.shops.configable.Configable;

public class Shop extends Configable {

	private String title;
	private int size;
	private Map<String, ShopContent> contents;
	private Map<String, ShopContentData> contentData;
	
	public Shop(FileConfiguration config, String path) {
		super(config, path);
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public int getSize() {
		return this.size;
	}
	
	public Map<String, ShopContent> getContents() {
		return this.contents;
	}
	
	public ShopContent getContent(String contentID) {
		return this.contents.getOrDefault(contentID, null);
	}
	
	public Map<String, ShopContentData> getContentData() {
		return this.contentData;
	}
	
	public ShopContentData getContentData(String contentID) {
		return this.contentData.getOrDefault(contentID, null);
	}

	@Override
	public void load(FileConfiguration config, String path) {
		this.title = config.getString(path + ".title").replace("&", "§");
		this.size = config.getInt(path + ".size");
		this.contents = Maps.newHashMap();
		this.contentData = Maps.newHashMap();
		config.getConfigurationSection("content").getKeys(false).forEach(id -> {
			ShopContent content = new ShopContent(config, path + ".content." + id);
			
			// Check slot
			for (ShopContent c : contents.values()) {
				if (c.getSlot() == content.getSlot()) {
					System.out.println("[NiceShops] Slot " + c.getSlot() + " is duplicated");
					break;
				}
			}
			
			// Save
			this.contents.put(id, content);
			if (config.contains(path + ".content-data." + id)) {
				this.contentData.put(id, new ShopContentData(config, path + ".content-data." + id));
			} else {
				this.contentData.put(id, new ShopContentData(0));
				System.out.println("[NiceShops] Content " + id + " has no data. Dont worry, im creating one");
			}
		});
	}

	@Override
	public void save(FileConfiguration config, String path) {
		config.set(path + ".title", this.title);
		config.set(path + ".size", this.size);
		this.contents.forEach((id, content) -> {
			content.save(config, path + ".content." + id);
		});
		this.contentData.forEach((id, condata) -> {
			condata.save(config, path + ".content-data." + id);
		});
	}
	
}
