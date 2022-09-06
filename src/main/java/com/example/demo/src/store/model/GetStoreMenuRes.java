package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetStoreMenuRes {
    private int storeMenuId;
    private String storeMenuName;
    private int storeMenuPrice;
    private String storeMenuContent;
    private String storeMenuImgUrl;
}
