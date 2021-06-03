package me.manaki.plugin.shops.openrandom;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.List;

public class OpenRandom {

    private String id;
    private List<Category> categories;
    private List<String> bonus;

    public OpenRandom(String id, List<Category> categories, List<String> bonus) {
        this.id = id;
        this.categories = categories;
        this.bonus = bonus;
    }

    public String getId() {
        return id;
    }

    public int getBonusFromPerm(Player player) {
        int bonus = 0;
        if (player != null) {
            for (PermissionAttachmentInfo p : player.getEffectivePermissions()) {
                if (p.getPermission().contains("shops.random.bonus.") && p.getValue()) {
                    bonus += Integer.parseInt(p.getPermission().replace("shops.random.bonus.", ""));
                }
            }
        }
        return bonus;
    }

    public int getAmount(Player player) {
        int amount = 0;
        for (Category category : categories) {
            amount += category.getAmount();
        }
        return amount + getBonusFromPerm(player);
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<String> getBonus() {
        return bonus;
    }
}
