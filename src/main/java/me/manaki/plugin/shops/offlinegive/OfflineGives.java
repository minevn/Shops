package me.manaki.plugin.shops.offlinegive;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import me.manaki.plugin.shops.Shops;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

import com.google.common.collect.Maps;

import me.manaki.plugin.shops.storage.ItemStorage;

public class OfflineGives {
	
	private static Map<String, List<GivenItem>> gives = Maps.newHashMap();
	
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
			gives.put(name, config.getStringList(name).stream().map(GivenItem::parse).collect(Collectors.toList()));
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
		if (player == null || !player.isOnline()) return;
		if (gives.containsKey(player.getName())) {
			var list = gives.get(player.getName());
			int empty = countEmpty(player.getInventory());
			if (empty < list.size()) {
				player.sendMessage("");
				player.sendMessage("§c§lBạn có món đồ được nhận nhưng kho không có chỗ trống");
				player.sendMessage("§c§lDọn kho, thoát game rồi vào lại để nhận đồ!");
				return;
			}
			for (var item : gives.get(player.getName())) {
				var id = item.getId();
				var amount = item.getAmount();
				ItemStack is = ItemStorage.get(id);
				if (is == null) {
					Shops.get().getLogger().severe("Offlinegive item id " + id + " is NULL!");
					continue;
				}
				is.setAmount(amount);
				player.getInventory().addItem(is);
			}
			player.sendMessage("§a§lNhận được " + list.size() + " đồ, hãy kiểm tra trong kho!");

			// Save
			gives.remove(player.getName());
			config.set(player.getName(), null);
			save(plugin);
		}
	}

	public static int countEmpty(PlayerInventory inv) {
		var contents = inv.getContents();
		int count = 0;
		for (ItemStack content : contents) {
			if (content == null || content.getType() == Material.AIR) count++;
		}
		return count - 5;
	}
	
}
