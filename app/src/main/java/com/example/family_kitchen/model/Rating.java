package com.example.family_kitchen.model;

public class Rating {

    private String DeliveryId,orderId,FamilyId,StoreRating,DeliveryRating;

    public Rating(){}

    public Rating(String deliveryId, String orderId, String familyId, String storeRating, String deliveryRating) {
        DeliveryId = deliveryId;
        this.orderId = orderId;
        FamilyId = familyId;
        StoreRating = storeRating;
        DeliveryRating = deliveryRating;
    }

    public String getDeliveryId() {
        return DeliveryId;
    }

    public void setDeliveryId(String deliveryId) {
        DeliveryId = deliveryId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getFamilyId() {
        return FamilyId;
    }

    public void setFamilyId(String familyId) {
        FamilyId = familyId;
    }

    public String getStoreRating() {
        return StoreRating;
    }

    public void setStoreRating(String storeRating) {
        StoreRating = storeRating;
    }

    public String getDeliveryRating() {
        return DeliveryRating;
    }

    public void setDeliveryRating(String deliveryRating) {
        DeliveryRating = deliveryRating;
    }
}
