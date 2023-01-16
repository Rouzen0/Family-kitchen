package com.example.family_kitchen.model;

public class Item {
    private String ItemName,ItemPrice,ItemImageUrl,StoreName,UserId,ItemId;

    public Item(){}

    public Item(String itemName, String itemPrice, String itemImageUrl, String storeName, String userId, String itemId) {
        ItemName = itemName;
        ItemPrice = itemPrice;
        ItemImageUrl = itemImageUrl;
        StoreName = storeName;
        UserId = userId;
        ItemId = itemId;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemPrice() {
        return ItemPrice;
    }

    public void setItemPrice(String itemPrice) {
        ItemPrice = itemPrice;
    }

    public String getItemImageUrl() {
        return ItemImageUrl;
    }

    public void setItemImageUrl(String itemImageUrl) {
        ItemImageUrl = itemImageUrl;
    }

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }
}
