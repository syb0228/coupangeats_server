package com.example.demo.src.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetReviewListRes {
    private int reviewId;
    private int userOrderId;
    private String reviewUserName;
    private int reviewScore;
    private String lastReviewDate;
    private List<GetReviewImgRes> reviewImgs;
    private String reviewContent;
    private List<GetOrderMenuListRes> orderMenuList;
    private int reviewHelpCount;
    private int helpOrNot;
}
