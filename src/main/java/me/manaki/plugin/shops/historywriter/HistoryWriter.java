package me.manaki.plugin.shops.historywriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import me.manaki.plugin.shops.shop.Shop;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.manaki.plugin.shops.shop.ShopContent;
import me.manaki.plugin.shops.storage.ShopStorage;

public class HistoryWriter {
	
	public static void write(Plugin plugin, Player player, String shopID, String contentID, boolean log) {
		Shop shop = ShopStorage.get(shopID);
		ShopContent content = shop.getContent(contentID);
		
		// Check file
		File file = new File(plugin.getDataFolder(), content.getPriceType().name().toLowerCase() + "-history.txt");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// Write
		String line = "[" + getCurrentTime() + "] " + player.getName() + " | " + content.getItemID() + " | " + shopID + " | " + content.getPriceValue() + content.getPriceType().getUnit();
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));	
		    out.println(line);
		    out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Log
		if (log) System.out.println(line);
	}
	
	private static String getCurrentTime() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  
		return dtf.format(now);
	}
	
}
