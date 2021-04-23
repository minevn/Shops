package me.manaki.plugin.shops.openrandom;

import com.google.common.collect.Lists;
import me.manaki.plugin.shops.Shops;
import me.manaki.plugin.shops.config.Configs;
import me.manaki.plugin.shops.storage.ItemStorage;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;

public class OpenRandoms {

    public static void open(Player player, String id) {
        open(player, Configs.getOR(id));
    }

    public static void open(Player player, OpenRandom or) {
        if (or == null) {
            Shops.get().getLogger().warning("NULL");
            return;
        }
        if (or.getAmount() > or.getItems().size()) {
            Shops.get().getLogger().warning("Amount > Size");
            return;
        }
        int size = Math.max(9, or.getAmount() % 9 == 0 ? or.getAmount() : (or.getAmount() / 9 + 1) * 9);
        var inv = Bukkit.createInventory(null, size);
        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
        player.openInventory(inv);

        int c = 0;
        List<String> result = Lists.newArrayList();
        List<String> items = Lists.newArrayList(or.getItems());
        while (c != or.getAmount()) {
            c++;
            int i = new Random().nextInt(items.size());
            result.add(items.get(i));
            items.remove(i);
        }
        Bukkit.getScheduler().runTaskAsynchronously(Shops.get(), () -> {
            for (int i = 0; i < result.size(); i++) inv.setItem(i, ItemStorage.get(result.get(i)));
        });
    }

}
