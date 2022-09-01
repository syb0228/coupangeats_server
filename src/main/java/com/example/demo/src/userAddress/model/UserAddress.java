package com.example.demo.src.userAddress.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class UserAddress {
    private int userAddressId;
    private String cityName;
    private String districtName;
    private String roadName;
    private String addressDetail;
    private String directions;
    private String addressCategory;
    private String addressName;
    private String addressAlias;
    private BigDecimal addressLatitude;
    private BigDecimal addressLongitude;
}
