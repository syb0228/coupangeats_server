package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class GetStoreInfoRes {

    private BigDecimal storeLatitude;
    private BigDecimal storeLongitude;
    private String storeName;
    private String storePhoneNum;
    private String storeAddress;
    private String storeOwner;
    private String storeBusinessNum;
    private String storeBusinessName;
    private String storeFindTip;
    private String storeBusinessHour;
    private String storeContent;
    private String storeNotice;
    private String originInfo;
    private String nutritionFactsInfo;
    private String allergenInfo;

}
