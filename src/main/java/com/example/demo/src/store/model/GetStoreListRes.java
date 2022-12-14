package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetStoreListRes {
    private int storeId;
    private String storeName;
    private String isCheetahDelivery;
    private String reviewScoreAndCount;
    private String distance;
    private int deliveryFee;
    private String takeOut;
    private String storeCouponPriceAndCategory;
    private List<GetStoreImgRes> storeImgs;
}
