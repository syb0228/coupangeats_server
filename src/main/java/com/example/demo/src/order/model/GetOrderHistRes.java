package com.example.demo.src.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetOrderHistRes {
    private int userOrderId;
    private String orderStoreName;
    private String orderTime;
    private String deliveryStatus;
    private List<GetOrderDetailRes> orderDetails;
    private int orderPrice;
    private String reviewStatus;
}
