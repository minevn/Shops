package me.manaki.plugin.shops;

import me.manaki.plugin.shops.offlinegive.OfflineGives;
import me.manaki.plugin.shops.listener.NSListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.manaki.plugin.shops.command.AdminCommand;
import me.manaki.plugin.shops.config.Configs;
import me.manaki.plugin.shops.price.PriceType;
import me.manaki.plugin.shops.storage.ItemStorage;
import me.manaki.plugin.shops.storage.ShopStorage;
import me.manaki.plugin.shops.yaml.YamlFile;

public class Shops extends JavaPlugin {
	@Override
	public void onEnable() {
		this.reloadConfigs();
		this.registerListeners();
		this.registerCommands();
	}
	
	@Override
	public void onDisable() {
		Bukkit.getOnlinePlayers().forEach(player -> player.closeInventory());
	}

	public void reloadConfigs() {
		this.saveDefaultConfig();
		YamlFile.reloadAll(this);
		Configs.load(YamlFile.CONFIG.get());
		PriceType.loadAll(YamlFile.CONFIG.get());
		ItemStorage.reload(this);
		ShopStorage.reload(this);
		OfflineGives.reload(this);
	}
	
	public void registerListeners() {
		Bukkit.getPluginManager().registerEvents(new NSListener(), this);
	}
	
	public void registerCommands() {
		this.getCommand("shops").setExecutor(new AdminCommand());
	}
	
	public static Shops get() {
		return JavaPlugin.getPlugin(Shops.class);
	}
	
}
