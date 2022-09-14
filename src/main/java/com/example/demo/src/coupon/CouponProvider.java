package com.example.demo.src.coupon;

import com.example.demo.config.BaseException;
import com.example.demo.src.coupon.model.GetCouponRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@Transactional(readOnly = true)
public class CouponProvider {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CouponDao couponDao;

    @Autowired
    public CouponProvider(CouponDao couponDao){
        this.couponDao = couponDao;
    }

    public List<GetCouponRes> getStoreCoupons(int storeId) throws BaseException {
        try {
            List<GetCouponRes> getCouponRes = couponDao.getStoreCoupons(storeId);
            return getCouponRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetCouponRes> getUserCoupons(int userId) throws BaseException {
        try {
            List<GetCouponRes> getCouponRes = couponDao.getUserCoupons(userId);
            return getCouponRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
