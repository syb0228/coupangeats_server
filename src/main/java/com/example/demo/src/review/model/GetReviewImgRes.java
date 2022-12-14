package com.example.demo.src.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetReviewImgRes {
    private int reviewImgId;
    private String reviewImgUrl;
    private String isRepImg;
}
