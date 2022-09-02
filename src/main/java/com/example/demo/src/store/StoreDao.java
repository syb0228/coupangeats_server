package com.example.demo.src.store;

import com.example.demo.src.store.model.GetStoreInfoRes;
import com.example.demo.src.store.model.GetStoreListRes;
import com.example.demo.src.store.model.GetStoreRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class StoreDao {

    public JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public GetStoreRes getStore(int userId, int storeId){
        String getStoreQuery = "select SI1.storeImgId, SI1.storeImgUrl\n" +
                "    , (select case\n" +
                "                when storeLikeId is not null then 1\n" +
                "                else 0 end\n" +
                "       from User\n" +
                "       inner join StoreLike SL on User.userId = SL.userId\n" +
                "       where User.userId = ? and Store.storeId = SL.storeId\n" +
                "    ) as likeOrNot\n" +
                "    , storeName\n" +
                "    , case\n" +
                "        when isCheetahDelivery = 'Y' then '치타배달'\n" +
                "        else 0\n" +
                "        end as isCheetahDelivery\n" +
                "    ,(select CONCAT(\n" +
                "        CAST(AVG(score) AS CHAR(3))\n" +
                "        , '(', CAST(COUNT(reviewId) AS CHAR(20)), ')')\n" +
                "       from Review\n" +
                "       where Store.storeId = Review.storeId\n" +
                "       ) as reviewScoreAndCount\n" +
                "    , (select CONCAT('최대 ', MAX(discountPrice), '원 쿠폰 받기')\n" +
                "        from Coupon\n" +
                "        inner join StoreCoupon SC on Coupon.couponId = SC.couponId\n" +
                "        where Store.storeId = SC.storeId\n" +
                "       ) as storeCouponPrice\n" +
                "    , deliveryFee, minOrderPrice\n" +
                "from Store\n" +
                "inner join (\n" +
                "    select storeImgId, storeId, storeImgUrl\n" +
                "    from StoreImg\n" +
                "    where StoreImg.storeId = ? and IsRepImg = 'Y'\n" +
                "    ) as SI1 on Store.storeId = SI1.storeId\n" +
                "where Store.storeId = ? and Store.status = 'active'";
        Object[] getStoreParams = new Object[]{userId, storeId, storeId};
        return this.jdbcTemplate.queryForObject(getStoreQuery,
                (rs, rowNum) -> new GetStoreRes(
                        rs.getInt("storeImgId"),
                        rs.getString("storeImgUrl"),
                        rs.getInt("likeOrNot"),
                        rs.getString("storeName"),
                        rs.getString("isCheetahDelivery"),
                        rs.getString("reviewScoreAndCount"),
                        rs.getString("storeCouponPrice"),
                        rs.getInt("deliveryFee"),
                        rs.getInt("minOrderPrice")),
                getStoreParams);
    }

    public GetStoreInfoRes getStoreInfo(int storeId){
        String getStoreInfoQuery = "select storeLatitude, storeLongitude\n" +
                "     , storeName, storePhoneNum, storeAddress, storeOwner, storeBusinessNum, storeBusinessName\n" +
                "    , storeFindTip, storeBusinessHour, storeContent, storeNotice\n" +
                "    , originInfo, nutritionFactsInfo, allergenInfo\n" +
                "from Store\n" +
                "where storeId = ? and status = 'active'";
        int getStoreInfoParams = storeId;
        return this.jdbcTemplate.queryForObject(getStoreInfoQuery,
                (rs, rsNum) -> new GetStoreInfoRes(
                        rs.getBigDecimal("storeLatitude"),
                        rs.getBigDecimal("storeLongitude"),
                        rs.getString("storeName"),
                        rs.getString("storePhoneNum"),
                        rs.getString("storeAddress"),
                        rs.getString("storeOwner"),
                        rs.getString("storeBusinessNum"),
                        rs.getString("storeBusinessName"),
                        rs.getString("storeFindTip"),
                        rs.getString("storeBusinessHour"),
                        rs.getString("storeContent"),
                        rs.getString("storeNotice"),
                        rs.getString("originInfo"),
                        rs.getString("nutritionFactsInfo"),
                        rs.getString("allergenInfo")),
                getStoreInfoParams);
    }

    public List<GetStoreListRes> getStores(){
        String getStoresQuery = "select storeId, storeName\n" +
                "    , case\n" +
                "        when isCheetahDelivery = 'Y' then '치타배달'\n" +
                "        else 0\n" +
                "        end as isCheetahDelivery\n" +
                "    , (select CONCAT(\n" +
                "        CAST(AVG(score) AS CHAR(3))\n" +
                "        , '(', CAST(COUNT(reviewId) AS CHAR(20)), ')')\n" +
                "       from Review\n" +
                "       where Store.storeId = Review.storeId\n" +
                "       ) as reviewScoreAndCount\n" +
                "    , (select CONCAT(\n" +
                "         ROUND((6371 * acos(cos(radians(addressLatitude))\n" +
                "            * cos(radians(storeLatitude))\n" +
                "            * cos(radians(storeLongitude) - radians(addressLongitude))\n" +
                "            + sin(radians(addressLatitude)) * sin(radians(storeLatitude))\n" +
                "            )), 1), 'km')\n" +
                "       from User\n" +
                "       inner join UserAddress UA on User.userId = UA.userId\n" +
                "       where userAddressId = 5\n" +
                "       ) as distance\n" +
                "    , deliveryFee\n" +
                "    , case\n" +
                "        when takeOut = 'Y' then '포장 가능'\n" +
                "        else 0 end as takeOut\n" +
                "    , (select CONCAT(\n" +
                "        CONCAT(MAX(discountPrice), '원 ')\n" +
                "        , case\n" +
                "            when couponCategory = 'delivery' then '배달쿠폰'\n" +
                "            when couponCategory = 'takeout' then '포장쿠폰'\n" +
                "            else '배달/포장쿠폰' end)\n" +
                "       from Coupon\n" +
                "       inner join StoreCoupon SC on Coupon.couponId = SC.couponId\n" +
                "       where SC.storeId = Store.storeId\n" +
                "       ) as storeCouponPriceAndCategory\n" +
                "from Store\n" +
                "where Store.status = 'active'\n" +
                "order by Store.createdAt DESC";
        return jdbcTemplate.query(getStoresQuery,
                (rs, rsNum) -> new GetStoreListRes(
                        rs.getInt("storeId"),
                        rs.getString("storeName"),
                        rs.getString("isCheetahDelivery"),
                        rs.getString("reviewScoreAndCount"),
                        rs.getString("distance"),
                        rs.getInt("deliveryFee"),
                        rs.getString("takeOut"),
                        rs.getString("storeCouponPriceAndCategory")));
    }

    public List<GetStoreListRes> getStoresByName(String searchName){
        String getStoresQuery = "select storeId, storeName\n" +
                "    , case\n" +
                "        when isCheetahDelivery = 'Y' then '치타배달'\n" +
                "        else 0\n" +
                "        end as isCheetahDelivery\n" +
                "    , (select CONCAT(\n" +
                "        CAST(AVG(score) AS CHAR(3))\n" +
                "        , '(', CAST(COUNT(reviewId) AS CHAR(20)), ')')\n" +
                "       from Review\n" +
                "       where Store.storeId = Review.storeId\n" +
                "       ) as reviewScoreAndCount\n" +
                "    , (select CONCAT(\n" +
                "         ROUND((6371 * acos(cos(radians(addressLatitude))\n" +
                "            * cos(radians(storeLatitude))\n" +
                "            * cos(radians(storeLongitude) - radians(addressLongitude))\n" +
                "            + sin(radians(addressLatitude)) * sin(radians(storeLatitude))\n" +
                "            )), 1), 'km')\n" +
                "       from User\n" +
                "       inner join UserAddress UA on User.userId = UA.userId\n" +
                "       where userAddressId = 5\n" +
                "       ) as distance\n" +
                "    , deliveryFee\n" +
                "    , case\n" +
                "        when takeOut = 'Y' then '포장 가능'\n" +
                "        else 0 end as takeOut\n" +
                "    , (select CONCAT(\n" +
                "        CONCAT(MAX(discountPrice), '원 ')\n" +
                "        , case\n" +
                "            when couponCategory = 'delivery' then '배달쿠폰'\n" +
                "            when couponCategory = 'takeout' then '포장쿠폰'\n" +
                "            else '배달/포장쿠폰' end)\n" +
                "       from Coupon\n" +
                "       inner join StoreCoupon SC on Coupon.couponId = SC.couponId\n" +
                "       where SC.storeId = Store.storeId\n" +
                "       ) as storeCouponPriceAndCategory\n" +
                "from Store\n" +
                "where storeName like concat('%', ?,'%') and Store.status = 'active'\n" +
                "order by Store.createdAt DESC";
        String getStoresParams = searchName;
        return jdbcTemplate.query(getStoresQuery,
                (rs, rsNum) -> new GetStoreListRes(
                        rs.getInt("storeId"),
                        rs.getString("storeName"),
                        rs.getString("isCheetahDelivery"),
                        rs.getString("reviewScoreAndCount"),
                        rs.getString("distance"),
                        rs.getInt("deliveryFee"),
                        rs.getString("takeOut"),
                        rs.getString("storeCouponPriceAndCategory")),
                getStoresParams);
    }

    public List<GetStoreListRes> getStoreLike(int userId){
        String getStoreLikeQuery = "select StoreLike.storeId, storeName\n" +
                "    , case\n" +
                "        when isCheetahDelivery = 'Y' then '치타배달'\n" +
                "        else 0\n" +
                "        end as isCheetahDelivery\n" +
                "    , (select CONCAT(\n" +
                "        CAST(AVG(score) AS CHAR(3))\n" +
                "        , '(', CAST(COUNT(reviewId) AS CHAR(20)), ')')\n" +
                "       from Review\n" +
                "       where StoreLike.storeId = Review.storeId\n" +
                "       ) as reviewScoreAndCount\n" +
                "    , CONCAT(\n" +
                "         ROUND((6371 * acos(cos(radians(addressLatitude))\n" +
                "            * cos(radians(storeLatitude))\n" +
                "            * cos(radians(storeLongitude) - radians(addressLongitude))\n" +
                "            + sin(radians(addressLatitude)) * sin(radians(storeLatitude))\n" +
                "            )), 1), 'km') as distance\n" +
                "    , deliveryFee\n" +
                "    , case\n" +
                "        when takeOut = 'Y' then '포장 가능'\n" +
                "        else 0 end as takeOut\n" +
                "    , (select CONCAT(\n" +
                "        CONCAT(MAX(discountPrice), '원 ')\n" +
                "        , case\n" +
                "            when couponCategory = 'delivery' then '배달쿠폰'\n" +
                "            when couponCategory = 'takeout' then '포장쿠폰'\n" +
                "            else '배달/포장쿠폰' end)\n" +
                "       from Coupon\n" +
                "       inner join StoreCoupon SC on Coupon.couponId = SC.couponId\n" +
                "       where SC.storeId = StoreLike.storeId\n" +
                "       ) as storeCouponPriceAndCategory\n" +
                "from StoreLike\n" +
                "inner join Store S on StoreLike.storeId = S.storeId\n" +
                "inner join UserAddress UA on StoreLike.userId = UA.userId and userAddressId = 5\n" +
                "where StoreLike.userId = ? and StoreLike.status = 'active'\n" +
                "order by StoreLike.createdAt DESC";
        int getStoreLikeParams = userId;
        return jdbcTemplate.query(getStoreLikeQuery,
                (rs, rsNum) -> new GetStoreListRes(
                        rs.getInt("storeId"),
                        rs.getString("storeName"),
                        rs.getString("isCheetahDelivery"),
                        rs.getString("reviewScoreAndCount"),
                        rs.getString("distance"),
                        rs.getInt("deliveryFee"),
                        rs.getString("takeOut"),
                        rs.getString("storeCouponPriceAndCategory")),
                getStoreLikeParams);
    }

}
