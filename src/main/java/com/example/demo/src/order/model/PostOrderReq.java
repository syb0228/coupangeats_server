package com.example.demo.src.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostOrderReq {
    private int userAddressId;
    private int storeId;
    private List<PostOrderDetailReq> orderDetails;
    private int userPaymentId;
    private String storeRequest;
    private int disposableItems;
    private String deliveryRequest;
}
