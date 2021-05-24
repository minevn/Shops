package me.manaki.plugin.shops.holdpermission;

public class HoldPermission {

    private final String itemID;
    private final String permission;
    private final String message;

    public HoldPermission(String itemID, String permission, String message) {
        this.itemID = itemID;
        this.permission = permission;
        this.message = message;
    }

    public String getItemID() {
        return itemID;
    }

    public String getPermission() {
        return permission;
    }

    public String getMessage() {
        return message;
    }
}
