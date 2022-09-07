package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetMenuRes {
    private int menuId;
    private String menuName;
    private int menuPrice;
    private String menuContent;
    private String menuImgUrl;
}
