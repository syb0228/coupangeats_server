package com.example.demo.src.coupon.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetCouponRes {
    private int couponId;
    private String couponCategory;
    private String couponName;
    private int discountPrice;
    private int minOrderPrice;
    private String expiredAt;
}
