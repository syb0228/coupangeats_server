package com.example.demo.src.payment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
public class GetPaymentsListRes {
    private int paymentMethod;
    private String paymentMethodName;
    private List<GetPaymentsRes> payments;
}
