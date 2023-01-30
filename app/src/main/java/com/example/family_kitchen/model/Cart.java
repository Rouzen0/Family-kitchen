package com.example.family_kitchen.model;

public class Cart {
    private String ItemName,ItemPrice,ItemImageUrl,UserId,ItemId,orderId,itemQuantity,totalPrice,totalOrderPrice,FamilyId,OrderStatus,OrderDate,OrderItemId,DriverId;

    public Cart(){}

    public Cart(String itemName, String itemPrice, String itemImageUrl, String userId, String itemId, String orderId, String itemQuantity, String totalPrice, String totalOrderPrice, String familyId, String orderStatus, String orderDate, String orderItemId, String driverId) {
        ItemName = itemName;
        ItemPrice = itemPrice;
        ItemImageUrl = itemImageUrl;
        UserId = userId;
        ItemId = itemId;
        this.orderId = orderId;
        this.itemQuantity = itemQuantity;
        this.totalPrice = totalPrice;
        this.totalOrderPrice = totalOrderPrice;
        FamilyId = familyId;
        OrderStatus = orderStatus;
        OrderDate = orderDate;
        OrderItemId = orderItemId;
        DriverId = driverId;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getTotalOrderPrice() {
        return totalOrderPrice;
    }

    public void setTotalOrderPrice(String totalOrderPrice) {
        this.totalOrderPrice = totalOrderPrice;
    }

    public String getFamilyId() {
        return FamilyId;
    }

    public void setFamilyId(String familyId) {
        FamilyId = familyId;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public String getOrderItemId() {
        return OrderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        OrderItemId = orderItemId;
    }

    public String getDriverId() {
        return DriverId;
    }

    public void setDriverId(String driverId) {
        DriverId = driverId;
    }
}
