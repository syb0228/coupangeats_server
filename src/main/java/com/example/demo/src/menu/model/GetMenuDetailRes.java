package com.example.demo.src.menu.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetMenuDetailRes {
    private int menuDetailId;
    private String detailName;
    private String required;
    private List<GetMenuDetailOptionRes> menuDetailOptions;
}
