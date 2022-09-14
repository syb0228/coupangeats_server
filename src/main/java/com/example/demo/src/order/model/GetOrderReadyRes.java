package com.example.demo.src.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetOrderReadyRes {
    private int userOrderId;
    private String orderStoreName;
    private String orderTime;
    private String orderStatus;
    private List<GetOrderDetailRes> orderDetails;
    private int orderPrice;
}
