package me.manaki.plugin.shops.openrandom;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.List;

public class OpenRandom {

    private String id;
    private int amount;
    private List<String> items;

    public OpenRandom(String id, int amount, List<String> items) {
        this.id = id;
        this.amount = amount;
        this.items = items;
    }

    public String getId() {
        return id;
    }

    public int getBonus(Player player) {
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
        return amount + getBonus(player);
    }

    public List<String> getItems() {
        return items;
    }
}
