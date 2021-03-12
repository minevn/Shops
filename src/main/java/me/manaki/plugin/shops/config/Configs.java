package me.manaki.plugin.shops.config;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.file.FileConfiguration;

import com.google.common.collect.Lists;

public class Configs {
	
	private static List<String> BONUS_LORE = Lists.newArrayList();
	
	public static void load(FileConfiguration config) {
		BONUS_LORE.clear();
		config.getStringList("bonus-lore").forEach(s -> {
			BONUS_LORE.add(s.replace("&", "§"));
		});
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
	
}
