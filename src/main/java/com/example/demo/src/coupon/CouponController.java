package com.example.demo.src.coupon;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.coupon.model.GetCouponRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app")
public class CouponController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final CouponProvider couponProvider;

    public CouponController(CouponProvider couponProvider){
        this.couponProvider = couponProvider;
    }

    /**
     * 특정 매장 할인쿠폰 조회 API
     * [GET] /store-coupons/:storeId
     * @return BaseResponse<List<GetCouponRes>>
     */
    @ResponseBody
    @GetMapping("/store-coupons/{storeId}")
    public BaseResponse<List<GetCouponRes>> getStoreCoupons(@PathVariable("storeId") int storeId){
        try{
            List<GetCouponRes> getCouponRes = couponProvider.getStoreCoupons(storeId);
            return new BaseResponse<>(getCouponRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 특정 사용자 할인쿠폰 조회 API
     * [GET] /user-coupons/:userId
     * @return BaseResponse<List<GetCouponRes>>
     */
    @ResponseBody
    @GetMapping("/user-coupons/{userId}")
    public BaseResponse<List<GetCouponRes>> getUserCoupons(@PathVariable("userId") int userId){
        try{
            List<GetCouponRes> getCouponRes = couponProvider.getUserCoupons(userId);
            return new BaseResponse<>(getCouponRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
