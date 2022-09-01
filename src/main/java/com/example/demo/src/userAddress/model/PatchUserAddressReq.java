package com.example.demo.src.userAddress.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchUserAddressReq {
    private int userAddressId;
    private String addressDetail;
    private String directions;
    private int addressCategory;
    private String addressAlias;
}
