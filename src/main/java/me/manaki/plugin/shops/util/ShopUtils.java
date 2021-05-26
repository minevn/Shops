package me.manaki.plugin.shops.util;

import java.util.List;
import java.util.Map;

import me.manaki.plugin.shops.config.Configs;
import me.manaki.plugin.shops.shop.ShopContent;
import me.manaki.plugin.shops.shop.ShopContentData;
import me.manaki.plugin.shops.storage.ItemStorage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.inventory.meta.ItemMeta;

public class ShopUtils {
	
	public static boolean canGiveItem(Player player) {
		return player.getInventory().firstEmpty() != -1;
	}
	
	public static void give(Player player, ShopContent content) {
		ItemStack is = ItemStorage.get(content.getItemID());
		assert is != null;
		is.setAmount(content.getSellAmount());
		player.getInventory().addItem(is);
	}
	
	public static ItemStack getBlankSlot() {
		ItemStack is = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
		ItemMeta meta = is.getItemMeta();
		is.setItemMeta(meta);
		ItemStackUtils.setDisplayName(is, " ");
		return is;
	}
	
	public static int getRemain(ShopContent content, ShopContentData contentData) {
		if (content == null) return 0;
		if (!content.isLimited()) return -1;
		return content.getLimit() - contentData.getSold();
	}
	
	public static ItemStack getIcon(ShopContent content, ShopContentData contentData) {
		ItemStack is = ItemStorage.get(content.getItemID());
		is.setAmount(content.getSellAmount());
		List<String> lore = Lists.newArrayList();
		String name = ItemStackUtils.getName(is);
		
		// Name
		name += name.contains("§l") ? " §7§l(x" + content.getSellAmount() + ")" : " §7(x" + content.getSellAmount() + ")";
		
		// Lore
		Map<String, String> placeholders = Maps.newHashMap();
		placeholders.put("%remain%", getRemain(content, contentData) + "");
		placeholders.put("%limit%", content.getLimit() + "");
		placeholders.put("%priceColor%", content.getPriceType().getColor());
		placeholders.put("%priceValue%", formatValue(content.getPriceValue()) + "");
		placeholders.put("%priceUnit%", content.getPriceType().getUnit());

		lore.addAll(Configs.getBonusLore(placeholders, content.getLimit() != -1));
		
		// Set
		ItemStackUtils.setDisplayName(is, name);
		ItemStackUtils.setLore(is, lore);
		ItemStackUtils.addFlag(is, ItemFlag.HIDE_ATTRIBUTES);
		
		return is;
	}
	
	public static String formatValue(double value) {
		int iv = new Double(value).intValue();
		return String.format("%,d", iv);
	}
	
}
