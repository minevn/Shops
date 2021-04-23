package me.manaki.plugin.shops.openrandom;

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

    public int getAmount() {
        return amount;
    }

    public List<String> getItems() {
        return items;
    }
}
