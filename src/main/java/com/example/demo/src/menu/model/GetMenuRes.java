package com.example.demo.src.menu.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetMenuRes {
    private List<GetMenuImgRes> menuImgs;
    private String menuName;
    private String menuContent;
    private int menuPrice;
    private List<GetMenuDetailRes> menuDetails;
}
