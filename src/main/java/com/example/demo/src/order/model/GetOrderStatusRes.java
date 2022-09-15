package com.example.demo.src.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
public class GetOrderStatusRes {
    private BigDecimal storeLatitude;
    private BigDecimal storeLongitude;
    private BigDecimal addressLatitude;
    private BigDecimal addressLongitude;
    private String distance;
    private String orderStatus;
    private String orderStatusTime;
    private String storeName;
    private String storeAddress;
    private int orderPrice;
    private List<GetOrderDetailRes> orderDetails;
}
