package com.example.demo.src.payment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class PostPaymentReq {
    private int paymentMethod;
    private String paymentBank;
    private String paymentNum;
}
