package me.manaki.plugin.shops.openrandom;

import com.google.common.collect.Lists;
import me.manaki.plugin.shops.Shops;
import me.manaki.plugin.shops.config.Configs;
import me.manaki.plugin.shops.storage.ItemStorage;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

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
        int amount = or.getAmount(player);
//        if (amount > or.getItems().size()) {
//            Shops.get().getLogger().warning("Amount > Size");
//            return;
//        }
        int size = Math.max(9, amount % 9 == 0 ? amount : (amount / 9 + 1) * 9);
        var inv = Bukkit.createInventory(new RandomHolder(), size, "§0Đóng lại là tự chuyển vào kho đồ");
        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
        player.openInventory(inv);
        int bonus = or.getBonusFromPerm(player);
        if (bonus > 0) {
            player.sendMessage("§aBạn được thêm " + bonus + " vật phẩm khi mở rương");
        }

        List<String> result = Lists.newArrayList();

        // Categories
        for (Category category : or.getCategories()) {
            int c = 0;
            List<String> items = Lists.newArrayList(category.getItems());
            while (c != category.getAmount()) {
                c++;
                int i = new Random().nextInt(items.size());
                result.add(items.get(i));
                items.remove(i);
            }
        }

        // Bonus
        int c = 0;
        List<String> items = Lists.newArrayList(or.getBonus());
        while (c != bonus) {
            c++;
            int i = new Random().nextInt(items.size());
            result.add(items.get(i));
            items.remove(i);
        }

        Bukkit.getScheduler().runTaskAsynchronously(Shops.get(), () -> {
            for (int i = 0; i < result.size(); i++) inv.setItem(i, ItemStorage.get(result.get(i)));
        });
    }

    public static void onClose(InventoryCloseEvent e) {
        if (!(e.getInventory().getHolder() instanceof RandomHolder)) return;
        var inv = e.getInventory();
        for (ItemStack is : inv.getContents()) {
            e.getPlayer().getInventory().addItem(is);
        }
    }

}

class RandomHolder implements InventoryHolder {

    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }
}