package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetStoreRes {
    private int storeImgId;
    private String storeImgUrl;
    private int likeOrNot;
    private String storeName;
    private String isCheetahDelivery;
    private String reviewScoreAndCount;
    private String storeCouponPrice;
    private int deliveryFee;
    private int minOrderPrice;
}
