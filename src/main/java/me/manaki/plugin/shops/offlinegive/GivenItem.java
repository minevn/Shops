package me.manaki.plugin.shops.offlinegive;

public class GivenItem {

    private final String id;
    private final int amount;

    public GivenItem(String id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public static GivenItem parse(String s) {
        var id = s.split(" ")[0];
        var amount = Integer.parseInt(s.split(" ")[1]);

        return new GivenItem(id, amount);
    }

}
