package me.manaki.plugin.shops.openrandom;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.List;

public class Category {

    private String id;
    private int amount;
    private List<String> items;

    public Category(String id, int amount, List<String> items) {
        this.id = id;
        this.amount = amount;
        this.items = items;
    }

    public String getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public List<String> getItems() {
        return items;
    }

}
