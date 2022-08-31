package com.example.demo.src.search.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class GetUserSearchRes {
    private int searchId;
    private String searchName;
    private String searchTime;
}
