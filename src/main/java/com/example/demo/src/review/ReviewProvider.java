package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.src.review.model.GetOrderReviewRes;
import com.example.demo.src.review.model.GetReviewRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@Transactional(readOnly = true)
public class ReviewProvider {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ReviewDao reviewDao;
    private final JwtService jwtService;

    @Autowired
    public ReviewProvider(ReviewDao reviewDao, JwtService jwtService){
        this.reviewDao = reviewDao;
        this.jwtService = jwtService;
    }

    public List<GetReviewRes> getReviews(int userId, int storeId) throws BaseException {
        try {
            List<GetReviewRes> getReviewRes = reviewDao.getReviews(userId, storeId);
            return getReviewRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetOrderReviewRes> getOrderReview(int userOrderId) throws BaseException {
        try {
            List<GetOrderReviewRes> getOrderReviewRes = reviewDao.getOrderReview(userOrderId);
            return getOrderReviewRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkReviewScore(int reviewId) throws BaseException {
        try {
            int checkReviewScore = reviewDao.checkReviewScore(reviewId);
            return checkReviewScore;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String checkReviewContent(int reviewId) throws BaseException {
        try {
            String checkReviewContent = reviewDao.checkReviewContent(reviewId);
            return checkReviewContent;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
