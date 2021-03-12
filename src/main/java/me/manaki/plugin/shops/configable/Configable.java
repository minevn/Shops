package me.manaki.plugin.shops.configable;

import org.bukkit.configuration.file.FileConfiguration;

public abstract class Configable {
	
	public Configable() {}
	public Configable(FileConfiguration config, String path) {
		load(config, path);
	};
	public abstract void load(FileConfiguration config, String path);
	public abstract void save(FileConfiguration config, String path);
	
}
