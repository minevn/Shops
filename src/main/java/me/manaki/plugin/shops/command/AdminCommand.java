package me.manaki.plugin.shops.command;

import java.util.List;

import me.manaki.plugin.shops.gui.GUIShop;
import me.manaki.plugin.shops.gui.GUIView;
import me.manaki.plugin.shops.Shops;
import me.manaki.plugin.shops.openrandom.OpenRandoms;
import me.manaki.plugin.shops.storage.ItemStorage;
import me.manaki.plugin.shops.storage.ShopStorage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

public class AdminCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		
		try {
			
			if (args[0].equalsIgnoreCase("reload")) {
				Shops.get().reloadConfigs();
				sender.sendMessage("§aReloaded");
			}
			
			else if (args[0].equalsIgnoreCase("open")) {
				Player player = null;
				if (args.length == 3) player = Bukkit.getPlayer(args[2]);
				else player = (Player) sender;
				String id = args[1].replace(".yml", "");
				GUIShop.open(player, id);
			}
			
			else if (args[0].equalsIgnoreCase("listitem")) {
				int page = 1;
				if (args.length > 1) page = Integer.valueOf(args[1]);
				int maxpage = ItemStorage.getItemStacks().size() / 8 + 1;
				sender.sendMessage("");
				sender.sendMessage("§2§lList item page §c§l" + page + "/" + maxpage);
				List<String> ids = Lists.newArrayList(ItemStorage.getItemStacks().keySet());
				for (int i = (page - 1) * 8 ; i < Math.min(page * 8, ids.size()) ; i++) {
					sender.sendMessage("§a" + (i + 1) + ". §f" + ids.get(i));
				}
				sender.sendMessage("");
			}
			
			else if (args[0].equalsIgnoreCase("listshop")) {
				int i = 0;
				List<String> ids = Lists.newArrayList(ShopStorage.getShops().keySet());
				sender.sendMessage("");
				sender.sendMessage("§2§lList shop");
				for (String shop : ids) {
					i++;
					sender.sendMessage("§a" + i + ". §f" + shop);
				}
				sender.sendMessage("");
			}
			
			else if (args[0].equalsIgnoreCase("load")) {
				String id = args[1];
				ItemStack is = ItemStorage.get(id);
				if (is == null) {
					sender.sendMessage("§7Can't find item id §c" + id);
					return false;
				}
				int amount = 1;
				Player player = null;
				boolean hand = false;
				if (args.length == 2) player = (Player) sender;
				else {
					amount = Integer.valueOf(args[2]);
					player = Bukkit.getPlayer(args[3]);
					if (args.length == 5) hand = Boolean.valueOf(args[4]);
				}
				if (player == null) {
					sender.sendMessage("§cPlayer is null?");
					return false;
				}
				is.setAmount(amount);
				if (!hand) player.getInventory().addItem(is);
				else {
					var handItem = player.getInventory().getItemInMainHand();
					player.getInventory().setItemInMainHand(is);
					player.getInventory().addItem(handItem);
				}
				
				if (sender instanceof Player) {
					sender.sendMessage("Ok, gave it to " + player.getName());
				}

			}
			
			else if (args[0].equalsIgnoreCase("save")) {
				Player player = (Player) sender;
				String id = args[1];
				boolean replace = ItemStorage.getItemStacks().containsKey(id);
				ItemStack is = player.getInventory().getItemInMainHand();
				is.setAmount(1);
				ItemStorage.save(Shops.get(), id, is);
				if (replace) sender.sendMessage("§aReplaced item id " + id);
				else sender.sendMessage("§aSaved item id " + id);
			}
			
			else if (args[0].equalsIgnoreCase("remove")) {
				String id = args[1];
				ItemStorage.remove(Shops.get(), id);
				sender.sendMessage("§aRemoved item id " + id);
			}
			
			else if (args[0].equalsIgnoreCase("view")) {
				Player player = (Player) sender;
				List<String> items = Lists.newArrayList(args[1].split(":"));
				GUIView.open(player, items, null, 0);
			}

			else if (args[0].equalsIgnoreCase("openrandom")) {
				String id = args[1];
				Player player = Bukkit.getPlayer(args[2]);
				OpenRandoms.open(player, id);
			}
			
		}
		catch (ArrayIndexOutOfBoundsException e) {
			sendHelp(sender);
		}
		
		return false;
	}
	
	public void sendHelp(CommandSender sender) {
		sender.sendMessage("");
		sender.sendMessage("§6§lShops by MankaiStep");
		sender.sendMessage("§a/shops reload");
		sender.sendMessage("§a/shops open <*shopID> <player>");
		sender.sendMessage("§a/shops listitem <page>");
		sender.sendMessage("§a/shops listshop");
		sender.sendMessage("§a/shops load <*itemID> <amount> <player> <tohand(true/false)>");
		sender.sendMessage("§a/shops save <*itemID>");
		sender.sendMessage("§a/shops remove <*itemID>");
		sender.sendMessage("§a/shops view <*itemID:itemID2:itemID3:...>");
		sender.sendMessage("§a/shops openrandom <*id> <player>");
		sender.sendMessage("");
	}
	
}
