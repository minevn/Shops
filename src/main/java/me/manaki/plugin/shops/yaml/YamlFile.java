package me.manaki.plugin.shops.yaml;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public enum YamlFile {
	
	CONFIG,
	ITEMS;
	
	private FileConfiguration config;
	
	private YamlFile() {}
	
	public FileConfiguration get() {
		return this.config;
	}
	
	public String getName() {
		return this.name().toLowerCase().replace("_", "-") + ".yml";
	}
	
	public void reload(Plugin plugin) {
		File file = new File(plugin.getDataFolder(), getName());
		if (!file.exists()) {
			if (plugin.getResource(this.getName()) == null)
				try {
					file.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			else plugin.saveResource(getName(), false);
			reload(plugin);
		}
		this.config = YamlConfiguration.loadConfiguration(file);
	}
	
	public static void reloadAll(Plugin plugin) {
		for (YamlFile file : values()) file.reload(plugin);
	}
	
	public void save(Plugin plugin) {
		File file = new File(plugin.getDataFolder(), getName());
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
