package com.example.demo.src.review;

import com.example.demo.src.review.model.*;
import com.example.demo.src.userAddress.model.PatchUserAddressReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ReviewDao {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetReviewImgRes> getReviewImgs(int reviewId){
        String getReviewImgQuery = "select reviewImgId, reviewImgUrl, isRepImg\n" +
                "from ReviewImg\n" +
                "where reviewId = ?";
        int getReviewImgParams = reviewId;
        return this.jdbcTemplate.query(getReviewImgQuery,
                (rs, rsNum) -> new GetReviewImgRes(
                        rs.getInt("reviewImgId"),
                        rs.getString("reviewImgUrl"),
                        rs.getString("isRepImg")),
                getReviewImgParams);
    }

    public List<GetOrderMenuListRes> getOrderMenuList(int userOrderId){
        String getOrderMenuListQuery = "select OrderDetail.menuId, menuName\n" +
                "from OrderDetail\n" +
                "inner join Menu M on OrderDetail.menuId = M.menuId\n" +
                "where userOrderId = ?";
        int getUserOrderParams = userOrderId;
        return this.jdbcTemplate.query(getOrderMenuListQuery,
                (rs, rsNum) -> new GetOrderMenuListRes(
                        rs.getInt("menuId"),
                        rs.getString("menuName")),
                getUserOrderParams);
    }

    public List<GetReviewListRes> getReviewList(int userId, int storeId){
        String getReviewListQuery = "select reviewId, Review.userOrderId\n" +
                "    , (select replace(userName, SUBSTRING(userName, 2, 2), '**')\n" +
                "       from User\n" +
                "       where Review.userId = User.userId\n" +
                "    ) as reviewUserName\n" +
                "    , score as reviewScore\n" +
                "    , case\n" +
                "        when DATE(Review.createdAt) >= DATE(SUBDATE(now(), INTERVAL 7 DAY))\n" +
                "            then CONCAT(TIMESTAMPDIFF(DAY, Review.createdAt, CURRENT_TIMESTAMP()), '일 전')\n" +
                "        when DATE(Review.createdAt) >= DATE(SUBDATE(now(), INTERVAL 14 DAY))\n" +
                "            then '지난 주'\n" +
                "        when DATE(Review.createdAt) >= DATE(SUBDATE(now(), INTERVAL 1 MONTH))\n" +
                "            then '이번 달'\n" +
                "        when DATE(Review.createdAt) >= DATE(SUBDATE(now(), INTERVAL 2 MONTH))\n" +
                "            then '지난 달'\n" +
                "        else DATE_FORMAT(Review.createdAt, '%Y-%m-%d')\n" +
                "        end as lastReviewDate\n" +
                "    , content as reviewContent\n" +
                "    , (select count(*)\n" +
                "        from ReviewHelp\n" +
                "        where Review.reviewId = ReviewHelp.reviewId\n" +
                "    ) as reviewHelpCount\n" +
                "    , case\n" +
                "        when (select reviewLikeId\n" +
                "        from ReviewHelp\n" +
                "        where Review.reviewId = ReviewHelp.reviewId and userId = ?) is not null then 1\n" +
                "        else 0 end as helpOrNot\n" +
                "from Review\n" +
                "inner join UserOrder US on Review.userOrderId = US.userOrderId\n" +
                "inner join OrderDetail OD on US.userOrderId = OD.userOrderId\n" +
                "where Review.storeId = ? and Review.status = 'active'\n" +
                "order by Review.createdAt desc";
        int getReviewListParams1 = userId;
        int getReviewListParams2 = storeId;
        return this.jdbcTemplate.query(getReviewListQuery,
                (rs, rsNum) -> new GetReviewListRes(
                        rs.getInt("reviewId"),
                        rs.getInt("userOrderId"),
                        rs.getString("reviewUserName"),
                        rs.getInt("reviewScore"),
                        rs.getString("lastReviewDate"),
                        getReviewImgs(rs.getInt("reviewId")),
                        rs.getString("reviewContent"),
                        getOrderMenuList(rs.getInt("userOrderId")),
                        rs.getInt("reviewHelpCount"),
                        rs.getInt("helpOrNot")),
                getReviewListParams1, getReviewListParams2);
    }

    public List<GetReviewRes> getReviews(int userId, int storeId){
        String getReviewQuery = "select CAST(AVG(score) AS FLOAT(2)) as scoreAvg,\n" +
                "       COUNT(reviewId) as reviewCount\n" +
                "from Review\n" +
                "where storeId = ?";
        int getReviewParams = storeId;
        return this.jdbcTemplate.query(getReviewQuery,
                (rs, rsNum) -> new GetReviewRes(
                        rs.getFloat("scoreAvg"),
                        rs.getInt("reviewCount"),
                        getReviewList(userId, storeId)),
                getReviewParams);
    }

    public List<GetOrderReviewRes> getOrderReview(int userOrderId){
        String getOrderReviewQuery = "select reviewId, US.storeId\n" +
                "    , (select storeName\n" +
                "       from Store\n" +
                "       where US.storeId = Store.storeId\n" +
                "    ) as orderStoreName\n" +
                "    , score as reviewScore\n" +
                "    , case\n" +
                "        when DATE(Review.createdAt) >= DATE(SUBDATE(now(), INTERVAL 7 DAY))\n" +
                "            then CONCAT(TIMESTAMPDIFF(DAY, Review.createdAt, CURRENT_TIMESTAMP()), '일 전')\n" +
                "        when DATE(Review.createdAt) >= DATE(SUBDATE(now(), INTERVAL 14 DAY))\n" +
                "            then '지난 주'\n" +
                "        when DATE(Review.createdAt) >= DATE(SUBDATE(now(), INTERVAL 1 MONTH))\n" +
                "            then '이번 달'\n" +
                "        when DATE(Review.createdAt) >= DATE(SUBDATE(now(), INTERVAL 2 MONTH))\n" +
                "            then '지난 달'\n" +
                "        else DATE_FORMAT(Review.createdAt, '%Y-%m-%d')\n" +
                "        end as lastReviewDate\n" +
                "    , content as reviewContent\n" +
                "    , (select count(*)\n" +
                "        from ReviewHelp\n" +
                "        where Review.reviewId = ReviewHelp.reviewId\n" +
                "    ) as reviewHelpCount\n" +
                "from Review\n" +
                "inner join UserOrder US on Review.userOrderId = US.userOrderId\n" +
                "where US.userOrderId = ? and Review.status = 'active'";
        int getOrderReviewParams = userOrderId;
        return this.jdbcTemplate.query(getOrderReviewQuery,
                (rs, rsNum) -> new GetOrderReviewRes(
                        rs.getInt("reviewId"),
                        rs.getInt("storeId"),
                        rs.getString("orderStoreName"),
                        rs.getInt("reviewScore"),
                        rs.getString("lastReviewDate"),
                        getReviewImgs(rs.getInt("reviewId")),
                        rs.getString("reviewContent"),
                        getOrderMenuList(userOrderId),
                        rs.getInt("reviewHelpCount")),
                getOrderReviewParams);
    }

    public int createReview(int userId, int storeId, int userOrderId, PostReviewReq postReviewReq){
        String createReviewQuery = "insert into Review (userId, storeId, userOrderId, score, content) VALUES (?, ?, ?, ?, ?)";
        Object[] createReviewParams = new Object[]{userId, storeId, userOrderId, postReviewReq.getScore(), postReviewReq.getContent()};
        this.jdbcTemplate.update(createReviewQuery, createReviewParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public int createReviewImg(int reviewId, PostReviewImgReq postReviewImgReq){
        String createReviewImgQuery = "insert into ReviewImg (reviewId, reviewImgUrl, isRepImg) VALUES (?, ?, ?)";
        Object[] createReviewParams = new Object[]{reviewId, postReviewImgReq.getReviewImgUrl(), postReviewImgReq.getIsRepImg()};
        this.jdbcTemplate.update(createReviewImgQuery, createReviewParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public int checkReviewScore(int reviewId){
        String checkReviewScoreQuery = "select score\n" +
                "from Review\n" +
                "where reviewId = ?";
        int checkReviewScoreParams = reviewId;
        return this.jdbcTemplate.queryForObject(checkReviewScoreQuery,
                int.class,
                checkReviewScoreParams);
    }

    public String checkReviewContent(int reviewId){
        String checkReviewContentQuery = "select content\n" +
                "from Review\n" +
                "where reviewId = ?";
        int checkReviewContentParams = reviewId;
        return this.jdbcTemplate.queryForObject(checkReviewContentQuery,
                String.class,
                checkReviewContentParams);
    }

    public int modifyReviewScore(PatchReviewReq patchReviewReq){
        String modifyReviewScoreQuery = "update Review set score = ? where reviewId = ?";
        Object[] modifyReviewScoreParams = new Object[]{patchReviewReq.getScore(), patchReviewReq.getReviewId()};
        return this.jdbcTemplate.update(modifyReviewScoreQuery, modifyReviewScoreParams);
    }

    public int modifyReviewContent(PatchReviewReq patchReviewReq){
        String modifyReviewContentQuery = "update Review set content = ? where reviewId = ?";
        Object[] modifyReviewContentParams = new Object[]{patchReviewReq.getContent(), patchReviewReq.getReviewId()};
        return this.jdbcTemplate.update(modifyReviewContentQuery, modifyReviewContentParams);
    }

}
