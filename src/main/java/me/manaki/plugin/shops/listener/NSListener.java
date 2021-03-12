package me.manaki.plugin.shops.listener;

import me.manaki.plugin.shops.offlinegive.OfflineGives;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import me.manaki.plugin.shops.gui.GUIShop;
import me.manaki.plugin.shops.gui.GUIView;
import me.manaki.plugin.shops.Shops;

public class NSListener implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Bukkit.getScheduler().runTaskLater(Shops.get(), () -> {
			OfflineGives.checkOfflineGive(Shops.get(), e.getPlayer());
		}, 20);
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		GUIShop.onClick(e);
		GUIView.onClick(e);
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		GUIView.onClose(e);
	}
	
}
