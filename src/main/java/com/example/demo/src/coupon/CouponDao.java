package com.example.demo.src.coupon;

import com.example.demo.src.coupon.model.GetCouponRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class CouponDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetCouponRes> getStoreCoupons(int storeId){
        String getStoreCouponsQuery = "select Coupon.couponId\n" +
                "       , case\n" +
                "           when couponCategory = 1 then '배달'\n" +
                "           when couponCategory = 2 then '포장'\n" +
                "           else '배달/포장'\n" +
                "           end as couponCategory\n" +
                "     , couponName, discountPrice, minOrderPrice\n" +
                "     , CONCAT(REPLACE(SUBSTRING(expiredAt, 6, 5), '-', '.'), ' 까지') as expiredAt\n" +
                "from Coupon\n" +
                "inner join StoreCoupon SC on Coupon.couponId = SC.couponId\n" +
                "where storeId = ? and SC.status = 'active' and expiredAt > now()";
        int getStoreCouponsParams = storeId;
        return this.jdbcTemplate.query(getStoreCouponsQuery,
                (rs, rowNum) -> new GetCouponRes(
                        rs.getInt("couponId"),
                        rs.getString("couponCategory"),
                        rs.getString("couponName"),
                        rs.getInt("discountPrice"),
                        rs.getInt("minOrderPrice"),
                        rs.getString("expiredAt")),
                getStoreCouponsParams);
    }

    public List<GetCouponRes> getUserCoupons(int userId){
        String getUserCouponsQuery = "select Coupon.couponId\n" +
                "       , case\n" +
                "           when couponCategory = 1 then '배달'\n" +
                "           when couponCategory = 2 then '포장'\n" +
                "           else '배달/포장'\n" +
                "           end as couponCategory\n" +
                "     , couponName, discountPrice, minOrderPrice\n" +
                "     , CONCAT(REPLACE(SUBSTRING(expiredAt, 6, 5), '-', '.'), ' 까지') as expiredAt\n" +
                "from Coupon\n" +
                "inner join UserCoupon UC on Coupon.couponId = UC.couponId\n" +
                "where userId = ? and UC.status = 'active' and expiredAt > now()\n" +
                "order by UC.createdAt DESC";
        int getUserCouponsParams = userId;
        return this.jdbcTemplate.query(getUserCouponsQuery,
                (rs, rowNum) -> new GetCouponRes(
                        rs.getInt("couponId"),
                        rs.getString("couponCategory"),
                        rs.getString("couponName"),
                        rs.getInt("discountPrice"),
                        rs.getInt("minOrderPrice"),
                        rs.getString("expiredAt")),
                getUserCouponsParams);
    }



}
