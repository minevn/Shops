package me.manaki.plugin.shops.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerShopItemBuyEvent extends PlayerEvent {

    private final int slot;
    private final String itemID;
    private final String shopID;

    public PlayerShopItemBuyEvent(Player player, int slot, String itemID, String shopID) {
        super(player);
        this.slot = slot;
        this.itemID = itemID;
        this.shopID = shopID;
    }

    public int getSlot() {
        return slot;
    }

    public String getItemID() {
        return itemID;
    }

    public String getShopID() {
        return shopID;
    }

    /*
     *  Required
     */

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public final static HandlerList getHandlerList(){
        return handlers;
    }

}
