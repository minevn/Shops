package me.manaki.plugin.shops.gui;

import me.manaki.plugin.shops.event.PlayerShopItemBuyEvent;
import me.manaki.plugin.shops.historywriter.HistoryWriter;
import me.manaki.plugin.shops.Shops;
import me.manaki.plugin.shops.shop.Shop;
import me.manaki.plugin.shops.shop.ShopContentData;
import me.manaki.plugin.shops.storage.ShopStorage;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import com.google.common.collect.Lists;

import me.manaki.plugin.shops.util.ShopUtils;

public class GUIShop {
	
	public static void open(Player player, String id) {
		// Create GUI
		Shop shop = ShopStorage.get(id);
		Inventory inv = Bukkit.createInventory(new GUIHolder(id), shop.getSize(), shop.getTitle());
		player.openInventory(inv);
		
		// Load contents
		Bukkit.getScheduler().runTaskAsynchronously(Shops.get(), () -> {
			// Background
			for (int i = 0 ; i < inv.getSize() ; i++) inv.setItem(i, ShopUtils.getBlankSlot());
			// Icons
			shop.getContents().forEach((contentID, content) -> {
				inv.setItem(content.getSlot(), ShopUtils.getIcon(content, shop.getContentData(contentID)));
			});
		});
	}
	
	public static void onClick(InventoryClickEvent e) {
		// Check GUI
		if (!(e.getInventory().getHolder() instanceof GUIHolder)) return;
		e.setCancelled(true);
		GUIHolder holder = (GUIHolder) e.getInventory().getHolder();
		Shop shop = ShopStorage.get(holder.getShopID());
		Player player = (Player) e.getWhoClicked();
		Inventory inv = e.getInventory();
		
		// Check slot
		int slot = e.getSlot();
		shop.getContents().forEach((contentID, content) -> {
			if (content.getSlot() != slot) return;
			ShopContentData contentData = shop.getContentData(contentID);
			
			// Open view
			if (e.getClick() == ClickType.LEFT) {
				GUIView.open(player, Lists.newArrayList(content.getItemID()), holder.getShopID(), 4);
			}
			// Buy
			else if (e.getClick() == ClickType.RIGHT) {
				// Check limit
				if (content.isLimited()) {
					if (ShopUtils.getRemain(content, contentData) <= 0) {
						player.sendMessage("§cBạn không thể mua vật phẩm này được nữa");
						return;
					}
				}
				
				// Check inventory slot
				if (!ShopUtils.canGiveItem(player)) {
					player.sendMessage("§cTúi đồ của bạn không đủ chỗ trống");
					return;
				}
				
				// Pay
				if (!content.getPriceType().pay(player, content.getPriceValue())) {
					player.sendMessage("§cBạn không đủ khả năng chi trả cho món đồ này");
					return;
				}

				// Call
				Bukkit.getPluginManager().callEvent(new PlayerShopItemBuyEvent(player, slot, content.getItemID(), holder.getShopID()));

				// Async task
				Bukkit.getScheduler().runTaskAsynchronously(Shops.get(), () -> {
					// Give
					ShopUtils.give(player, content);
					
					// Check limit
					if (content.isLimited()) {
						contentData.setSold(contentData.getSold() + 1);
					}
					
					// Send message
					player.sendMessage("§aMua thành công");
					player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, 1, 1);
					
					// Write history
					HistoryWriter.write(Shops.get(), player, holder.getShopID(), contentID, true);
					
					// Save shop
					ShopStorage.save(Shops.get(), holder.getShopID());
					
					// Set icon
					inv.setItem(content.getSlot(), ShopUtils.getIcon(content, shop.getContentData(contentID)));
				});
			}

		});
	}
	
}
