package com.example.demo.src.menuCategory.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetMenuCategoryRes {
    private int menuCategoryId;
    private String menuCategoryName;
    private String menuCategoryImgUrl;
}
