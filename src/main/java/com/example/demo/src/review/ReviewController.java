package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.review.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/reviews")
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
    @GetMapping("/{userId}/{storeId}")
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
    @GetMapping("/{userOrderId}")
    public BaseResponse<List<GetOrderReviewRes>> getReviews(@PathVariable("userOrderId") int userOrderId){
        try{
            jwtService.getUserId();

            List<GetOrderReviewRes> getOrderReviewRes = reviewProvider.getOrderReview(userOrderId);
            return new BaseResponse<>(getOrderReviewRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 리뷰 생성 API
     * [POST] /reviews/:storeId/:userOrderId
     * @return BaseResponse<PostReviewRes>
     */
    @ResponseBody
    @PostMapping("/{storeId}/{userOrderId}")
    public BaseResponse<PostReviewRes> createReview(@PathVariable("storeId") int storeId, @PathVariable("userOrderId") int userOrderId, @RequestBody PostReviewReq postReviewReq){
        try {
            int userIdByJwt = jwtService.getUserId();
            PostReviewRes postReviewRes = reviewService.createReview(userIdByJwt, storeId, userOrderId, postReviewReq);
            return new BaseResponse<>(postReviewRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 리뷰 정보 변경 API
     * [PATCH] /reviews/:reviewId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{reviewId}")
    public BaseResponse<String> modifyReview(@PathVariable("reviewId") int reviewId, @RequestBody Review review){
        try {
            jwtService.getUserId();

            PatchReviewReq patchReviewReq = new PatchReviewReq(reviewId, review.getScore(), review.getContent());
            // 원래 평점과 다르면 변경
            if(patchReviewReq.getScore() != reviewProvider.checkReviewScore(reviewId) && patchReviewReq.getScore() != 0){
                reviewService.modifyReviewScore(patchReviewReq);
            }

            // 원래 리뷰 내용과 다르면 변경
            if(patchReviewReq.getContent() != null && patchReviewReq.getContent() != reviewProvider.checkReviewContent(reviewId)){
                reviewService.modifyReviewContent(patchReviewReq);
            }

            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 리뷰 정보 삭제 API
     * [DELETE] /reviews/:reviewId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @DeleteMapping("/{reviewId}")
    public BaseResponse<String> deleteReview(@PathVariable("reviewId") int reviewId){
        try {
            jwtService.getUserId();

            reviewService.deleteReview(reviewId);

            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
