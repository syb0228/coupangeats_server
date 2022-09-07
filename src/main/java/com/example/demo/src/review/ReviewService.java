package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.review.model.PatchReviewReq;
import com.example.demo.src.review.model.PostReviewReq;
import com.example.demo.src.review.model.PostReviewRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class ReviewService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ReviewDao reviewDao;
    private final ReviewProvider reviewProvider;
    private final JwtService jwtService;

    @Autowired
    public ReviewService(ReviewDao reviewDao, ReviewProvider reviewProvider, JwtService jwtService){
        this.reviewDao = reviewDao;
        this.reviewProvider = reviewProvider;
        this.jwtService = jwtService;
    }

    public PostReviewRes createReview(int userId, int storeId, int userOrderId, PostReviewReq postReviewReq) throws BaseException {
        try {
            int reviewId = reviewDao.createReview(userId, storeId, userOrderId, postReviewReq);
            int reviewImgId = 0;
            for (int i = 0; i < postReviewReq.getReviewImgs().size(); i++){
                reviewImgId = reviewDao.createReviewImg(reviewId, postReviewReq.getReviewImgs().get(i));
            }
            return new PostReviewRes(reviewId, reviewImgId);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyReviewScore(PatchReviewReq patchReviewReq) throws BaseException {
        try{
            int result = reviewDao.modifyReviewScore(patchReviewReq);
            if(result == 0) {
                throw new BaseException(MODIFY_FAIL_REVIEW_SCORE);
            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyReviewContent(PatchReviewReq patchReviewReq) throws BaseException {
        try{
            int result = reviewDao.modifyReviewContent(patchReviewReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_REVIEW_CONTENT);
            }
      } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
      }
    }

}
