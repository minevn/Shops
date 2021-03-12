package me.manaki.plugin.shops.gui;

import java.util.List;

import me.manaki.plugin.shops.Shops;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import me.manaki.plugin.shops.storage.ItemStorage;
import me.manaki.plugin.shops.util.ShopUtils;

public class GUIView {
	
	private static final String TITLE = "§c§lXEM TRƯỚC";
	
	public static void open(Player player, List<String> items, String fromShopID, int fromSlot) {
		// Open
		int size = (items.size() + fromSlot) % 9 == 0 ? items.size() + fromSlot : ((items.size() + fromSlot) / 9 + 1) * 9;
		Inventory inv = Bukkit.createInventory(new GUIViewHolder(fromShopID), size, TITLE);
		player.openInventory(inv);
		
		// Load
		Bukkit.getScheduler().runTaskAsynchronously(Shops.get(), () -> {
			for (int i = 0 ; i < inv.getSize() ; i++) inv.setItem(i, ShopUtils.getBlankSlot());
			for (int i = 0 ; i < items.size() ; i++) inv.setItem(fromSlot + i, ItemStorage.get(items.get(i)));
		});
	}
	
	public static void onClick(InventoryClickEvent e) {
		if (e.getInventory().getHolder() instanceof GUIViewHolder) e.setCancelled(true);
	}
	
	public static void onClose(InventoryCloseEvent e) {
		if (e.getInventory().getHolder() instanceof GUIViewHolder) {
			Player player = (Player) e.getPlayer();
			GUIViewHolder holder = (GUIViewHolder) e.getInventory().getHolder();
			if (holder.getPreviousShopID() == null) return;
			Bukkit.getScheduler().runTask(Shops.get(), () -> {
				GUIShop.open(player, holder.getPreviousShopID());
			});
		}
	}
	
}
