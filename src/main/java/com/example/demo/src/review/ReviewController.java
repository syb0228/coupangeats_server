package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.review.model.GetOrderReviewRes;
import com.example.demo.src.review.model.GetReviewRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
public class ReviewController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ReviewProvider reviewProvider;
    @Autowired
    private final ReviewService reviewService;
    @Autowired
    private final JwtService jwtService;

    public ReviewController(ReviewProvider reviewProvider, ReviewService reviewService, JwtService jwtService){
        this.reviewProvider = reviewProvider;
        this.reviewService = reviewService;
        this.jwtService = jwtService;
    }

    /**
     * 특정 매장 전체 리뷰 조회 API
     * [GET] /reviews/:userId/:storeId
     * @return BaseResponse<List<GetReviewListRes>>
     */
    @ResponseBody
    @GetMapping("/reviews/{userId}/{storeId}")
    public BaseResponse<List<GetReviewRes>> getReviews(@PathVariable("userId") int userId, @PathVariable("storeId") int storeId){
        try{
            List<GetReviewRes> getReviewRes = reviewProvider.getReviews(userId, storeId);
            return new BaseResponse<>(getReviewRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 특정 주문 리뷰 조회 API
     * [GET] /reviews/:userOrderId
     * @return BaseResponse<List<GetOrderReviewRes>>
     */
    @ResponseBody
    @GetMapping("/reviews/{userOrderId}")
    public BaseResponse<List<GetOrderReviewRes>> getReviews(@PathVariable("userOrderId") int userOrderId){
        try{
            jwtService.getUserId();

            List<GetOrderReviewRes> getOrderReviewRes = reviewProvider.getOrderReview(userOrderId);
            return new BaseResponse<>(getOrderReviewRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
