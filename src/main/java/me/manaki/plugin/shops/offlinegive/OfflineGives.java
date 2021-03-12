package me.manaki.plugin.shops.offlinegive;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.google.common.collect.Maps;

import me.manaki.plugin.shops.storage.ItemStorage;

public class OfflineGives {
	
	private static Map<String, String> gives = Maps.newHashMap();
	
	private static FileConfiguration config;
	
	public static void reload(Plugin plugin) {
		File file = new File(plugin.getDataFolder(), "offline-give.yml");
		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		config = YamlConfiguration.loadConfiguration(file);
		gives.clear();
		config.getConfigurationSection("").getKeys(false).forEach(name -> {
			gives.put(name, config.getString(name));
		});
	}
	
	public static void save(Plugin plugin) {
		try {
			config.save(new File(plugin.getDataFolder(), "offline-give.yml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void checkOfflineGive(Plugin plugin, Player player) {
		if (gives.containsKey(player.getName())) {
			ItemStack is = ItemStorage.get(gives.get(player.getName()));
			if (player.getInventory().firstEmpty() == -1) {
				player.sendMessage("§c§lBạn có món đồ được nhận nhưng kho không có chỗ trống");
				player.sendMessage("§c§lDọn kho, thoát game rồi vào lại để nhận đồ!");
				return;
			}
			player.getInventory().addItem(is);
			gives.remove(player.getName());
			config.set(player.getName(), null);
			save(plugin);
			player.sendMessage("§aNhận được một đồ, hãy kiểm tra trong kho!");
		}
	}
	
}
