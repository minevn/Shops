package me.manaki.plugin.shops.listener;

import me.manaki.plugin.shops.config.Configs;
import me.manaki.plugin.shops.holdpermission.HoldPermission;
import me.manaki.plugin.shops.offlinegive.OfflineGives;
import me.manaki.plugin.shops.storage.ItemStorage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import me.manaki.plugin.shops.gui.GUIShop;
import me.manaki.plugin.shops.gui.GUIView;
import me.manaki.plugin.shops.Shops;
import org.bukkit.inventory.Inventory;

import java.util.Map;

public class NSListener implements Listener {

	@EventHandler
	public void onItemHold(PlayerItemHeldEvent e) {
		var player = e.getPlayer();
		var is = e.getPlayer().getInventory().getItem(e.getNewSlot());
		if (is == null || is.getType() == Material.AIR) return;
		var id = ItemStorage.getID(is);
		if (id == null) return;

		for (Map.Entry<String, HoldPermission> entry : Configs.getHoldPermissions().entrySet()) {
			if (id.equals(entry.getKey())) {
				var perm = entry.getValue();
				if (!player.hasPermission(perm.getPermission())) {
					e.setCancelled(true);
					player.sendMessage(perm.getMessage());
				}
			}
		}
	}

	@EventHandler
	public void onItemToHeld(InventoryClickEvent e) {
		var player = (Player) e.getWhoClicked();
		var is = e.getCursor();
		if (is == null || is.getType() == Material.AIR) return;
		if (e.getSlot() != player.getInventory().getHeldItemSlot()) return;
		var id = ItemStorage.getID(is);
		if (id == null) return;

		for (Map.Entry<String, HoldPermission> entry : Configs.getHoldPermissions().entrySet()) {
			if (id.equals(entry.getKey())) {
				var perm = entry.getValue();
				if (!player.hasPermission(perm.getPermission())) {
					e.setCancelled(true);
					player.sendMessage(perm.getMessage());
				}
			}
		}
	}

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
