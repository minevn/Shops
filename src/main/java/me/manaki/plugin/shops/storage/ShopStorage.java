package me.manaki.plugin.shops.storage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Map;

import me.manaki.plugin.shops.shop.Shop;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.google.common.collect.Maps;

public class ShopStorage {
	
	private static Map<String, Shop> shops = Maps.newHashMap();
	
	private static final String FOLDER_NAME = "shops";
	
	public static void reload(Plugin plugin) {
		shops.clear();
		File folder = new File(plugin.getDataFolder() + "//" + FOLDER_NAME);
		if (!folder.exists()) {
			folder.mkdirs();
			InputStream is = plugin.getResource("example-shop.yml");
	    	File file = new File(folder.getAbsolutePath(), "example-shop.yml");
	   		try (var out = new FileOutputStream(file)) {
				assert is != null : "Resource example-shop null!" ;
				is.transferTo(out);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for (File file : folder.listFiles()) {
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			String id = file.getName().replace(".yml", "");
			shops.put(id, new Shop(config, ""));
		}
		saveAll(plugin);
		System.out.println("[NiceShops] Loaded " + shops.size() + " shops");
	}
	
	public static void save(Plugin plugin, String id) {
		Shop shop = get(id);
		if (shop == null) return;
		FileConfiguration config = getConfig(plugin, id);
		shop.save(config, "");
		File file = new File(plugin.getDataFolder() + "//" + FOLDER_NAME + "//" + id + ".yml");
		try {
			config.save(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void saveAll(Plugin plugin) {
		shops.keySet().forEach(id -> save(plugin, id));
	}
	
	public static Shop get(String id) {
		return shops.getOrDefault(id, null);
	}
	
	public static Map<String, Shop> getShops() {
		return shops;
	}
	
	private static FileConfiguration getConfig(Plugin plugin, String id) {
		File file = new File(plugin.getDataFolder() + "//" + FOLDER_NAME + "//" + id + ".yml");
		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return YamlConfiguration.loadConfiguration(file);
	}
	
}
