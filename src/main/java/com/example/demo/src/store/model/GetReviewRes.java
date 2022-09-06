package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetReviewRes {
    private int reviewId;
    private String reviewImgUrl;
    private String content;
    private int score;
}
