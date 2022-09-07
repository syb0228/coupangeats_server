package com.example.demo.src.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetOrderReviewRes {
    private int reviewId;
    private int storeId;
    private String orderStoreName;
    private int reviewScore;
    private String lastReviewDate;
    private List<GetReviewImgRes> reviewImgs;
    private String reviewContent;
    private List<GetOrderMenuListRes> orderMenuList;
    private int reviewHelpCount;
}
