package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetStoreLikeRes {
    private int storeId;
    private String storeImgUrl;
    private String storeName;
    private String isCheetahDelivery;
    private String reviewScoreAndCount;
    private String distance;
    private int deliveryFee;
    private String takeOut;
    private String storeCouponPriceAndCategory;
}
