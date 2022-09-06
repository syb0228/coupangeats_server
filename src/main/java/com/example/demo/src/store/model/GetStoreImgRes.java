package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetStoreImgRes {
    private int storeImgId;
    private String storeImgUrl;
    private String isRepImg;
}
