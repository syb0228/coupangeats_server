package com.example.demo.src.store;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.store.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
public class StoreController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final StoreProvider storeProvider;
    @Autowired
    private final JwtService jwtService;

    public StoreController(StoreProvider storeProvider, JwtService jwtService){
        this.storeProvider = storeProvider;
        this.jwtService = jwtService;
    }

    /**
     * 전체 매장 조회 API
     * [GET] /stores
     * 매장 검색 조회 API
     * [GET] /stores? searchName
     * @return BaseResponse<List<GetStoreListRes>>
     */
    @ResponseBody
    @GetMapping("/app/stores")
    public BaseResponse<List<GetStoreListRes>> getStoreList(@RequestParam(required = false) String searchName){
        try{
            if(searchName == null){
                List<GetStoreListRes> getStoreListRes = storeProvider.getStores();
                return new BaseResponse<>(getStoreListRes);
            }
            List<GetStoreListRes> getStoreListRes = storeProvider.getStoresByName(searchName);
            return new BaseResponse<>(getStoreListRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }


    /**
     * 특정 매장 조회(메인 화면) API
     * [GET] /stores/:userId/:storeId/:menuCategoryId
     * @return BaseResponse<GetStoreRes>
     */
    @ResponseBody
    @GetMapping("/stores/{userId}/{storeId}/{menuCategoryId}")
    public BaseResponse<GetStoreRes> getStore(@PathVariable("userId") int userId, @PathVariable("storeId") int storeId, @PathVariable("menuCategoryId") int menuCategoryId){
        try{
            GetStoreRes getStoreRes = storeProvider.getStore(userId, storeId, menuCategoryId);
            return new BaseResponse<>(getStoreRes);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 특정 매장 정보 조회 API
     * [GET] /stores/:storeId
     * @return BaseResponse<GetStoreInfoRes>
     */
    @ResponseBody
    @GetMapping("/store-infos/{storeId}")
    public BaseResponse<GetStoreInfoRes> getStoreInfo(@PathVariable("storeId") int storeId){
        try{
            GetStoreInfoRes getStoreInfoRes = storeProvider.getStoreInfo(storeId);
            return new BaseResponse<>(getStoreInfoRes);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 특정 유저가 즐겨찾기한 매장 조회 API
     * [GET] /stores
     * @return BaseResponse<List<GetStoreListRes>>
     */
    @ResponseBody
    @GetMapping("/app/store-likes")
    public BaseResponse<List<GetStoreLikeRes>> getStoreLike(){
        try{
            int userIdByJwt = jwtService.getUserId();
            List<GetStoreLikeRes> getStoreListRes = storeProvider.getStoreLike(userIdByJwt);
            return new BaseResponse<>(getStoreListRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 매장 사장님 사진 조회 API
     * [GET] /storeImgs/:storeId
     * @return BaseResponse<List<GetStoreImgRes>>
     */
    @ResponseBody
    @GetMapping("/app/storeImgs/{storeId}")
    public BaseResponse<List<GetStoreImgRes>> getStoreMainImgs(@PathVariable("storeId") int storeId){
        try{
            List<GetStoreImgRes> getStoreImgRes = storeProvider.getStoreImgs(storeId);
            return new BaseResponse<>(getStoreImgRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 매장 리뷰 사진 조회 API
     * [GET] /reviewImgs/:storeId
     * @return BaseResponse<List<GetStoreImgRes>>
     */
    @ResponseBody
    @GetMapping("/app/reviewImgs/{storeId}")
    public BaseResponse<List<GetStoreImgRes>> getStoreReviewImgs(@PathVariable("storeId") int storeId){
        try{
            List<GetStoreImgRes> getStoreImgRes = storeProvider.getStoreReviewImgs(storeId);
            return new BaseResponse<>(getStoreImgRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
