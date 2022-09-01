package com.example.demo.src.userAddress.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserAddressRes {
    private int userAddressId;
    private String addressName;
    private String cityName;
    private String districtName;
    private String roadName;
    private String addressDetail;
}
