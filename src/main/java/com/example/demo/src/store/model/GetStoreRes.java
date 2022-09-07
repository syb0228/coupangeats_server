package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetStoreRes {
    private List<GetStoreImgRes> storeImgs;
    private int likeOrNot;
    private String storeName;
    private String isCheetahDelivery;
    private String reviewScoreAndCount;
    private String storeCouponPrice;
    private int deliveryFee;
    private int minOrderPrice;
    private String menuCategoryName;
    private List<GetReviewRes> reviews;
    private List<GetMenuRes> menus;
}
