package me.manaki.plugin.shops.config;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;
import me.manaki.plugin.shops.holdpermission.HoldPermission;
import me.manaki.plugin.shops.openrandom.Category;
import me.manaki.plugin.shops.openrandom.OpenRandom;
import org.bukkit.configuration.file.FileConfiguration;

import com.google.common.collect.Lists;

public class Configs {
	
	private static List<String> BONUS_LORE = Lists.newArrayList();
	private static Map<String, OpenRandom> openRandom = Maps.newHashMap();

	private static Map<String, HoldPermission> holdPermissions = Maps.newHashMap();

	public static void load(FileConfiguration config) {
		BONUS_LORE.clear();
		config.getStringList("bonus-lore").forEach(s -> {
			BONUS_LORE.add(s.replace("&", "§"));
		});

		openRandom.clear();
		for (String id : config.getConfigurationSection("open-random").getKeys(false)) {
			List<Category> categories = Lists.newArrayList();
			for (String id2 : config.getConfigurationSection("open-random." + id).getKeys(false)) {
				if (!id2.equals("bonus")) {
					int amount = config.getInt("open-random." + id + "." + id2 + ".amount");
					var items = config.getStringList("open-random." + id + "." + id2 + ".items");
					var category = new Category(id2, amount, items);
					categories.add(category);
				}
			}
			List<String> bonus = config.getStringList("open-random." + id + ".bonus");
			openRandom.put(id, new OpenRandom(id, categories, bonus));
		}

		holdPermissions.clear();
		for (String id : config.getConfigurationSection("hold-permission").getKeys(false)) {
			var paerm = config.getString("hold-permission." + id + ".permission");
			var mess = config.getString("hold-permission." + id + ".message").replace("&", "§");
			holdPermissions.put(id, new HoldPermission(id, paerm, mess));
		}
	}
	
	public static List<String> getBonusLore(Map<String, String> placeholders, boolean limit) {
		List<String> list = Lists.newArrayList();
		BONUS_LORE.forEach(s -> {
			String line = s;
			if (!limit && (line.contains("%limit%") || line.contains("%remain%"))) return;
			for (Entry<String, String> e : placeholders.entrySet()) line = line.replace(e.getKey(), e.getValue());
			list.add(line);
		});
		return list;
	}

	public static Map<String, OpenRandom> getOpenRandom() {
		return openRandom;
	}

	public static OpenRandom getOR(String id) {
		return openRandom.getOrDefault(id, null);
	}

	public static Map<String, HoldPermission> getHoldPermissions() {
		return holdPermissions;
	}
}
