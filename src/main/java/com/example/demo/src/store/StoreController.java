package com.example.demo.src.store;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.store.model.GetStoreInfoRes;
import com.example.demo.src.store.model.GetStoreListRes;
import com.example.demo.src.store.model.GetStoreRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
public class StoreController {

    @Autowired
    public final StoreProvider storeProvider;
    @Autowired
    public final StoreService storeService;

    public StoreController(StoreProvider storeProvider, StoreService storeService){
        this.storeProvider = storeProvider;
        this.storeService = storeService;
    }

    /**
     * 특정 매장 조회 API
     * [GET] /stores/:userId/:storeId
     * @return BaseResponse<GetStoreRes>
     */
    @ResponseBody
    @GetMapping("/stores/{userId}/{storeId}")
    public BaseResponse<GetStoreRes> getStore(@PathVariable("userId") int userId, @PathVariable("storeId") int storeId){
        try{
            GetStoreRes getStoreRes = storeProvider.getStore(userId, storeId);
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
    @GetMapping("/stores/{storeId}")
    public BaseResponse<GetStoreInfoRes> getStoreInfo(@PathVariable("storeId") int storeId){
        try{
            GetStoreInfoRes getStoreInfoRes = storeProvider.getStoreInfo(storeId);
            return new BaseResponse<>(getStoreInfoRes);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
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
     * 특정 유저가 즐겨찾기한 매장 조회 API
     * [GET] /stores/:userId
     * @return BaseResponse<List<GetStoreListRes>>
     */
    @ResponseBody
    @GetMapping("/app/stores/{userId}")
    public BaseResponse<List<GetStoreListRes>> getStoreLike(@PathVariable("userId") int userId){
        try{
            List<GetStoreListRes> getStoreListRes = storeProvider.getStoreLike(userId);
            return new BaseResponse<>(getStoreListRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
