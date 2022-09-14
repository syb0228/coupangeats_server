package com.example.demo.src.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetOrderDetailRes {
    private int orderDetailId;
    private String menuName;
    private List<GetOrderDetailOptionRes> orderDetailOptions;
}
