package com.example.demo.src.search;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.search.model.GetSearchRes;
import com.example.demo.src.search.model.GetUserSearchRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/search")
public class SearchController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final SearchProvider searchProvider;
    @Autowired
    private final SearchService searchService;

    public SearchController(SearchProvider searchProvider, SearchService searchService){
        this.searchProvider = searchProvider;
        this.searchService = searchService;
    }

    /**
     * 전체 검색어 상위 10개 조회 API
     * [GET] /search
     * @return BaseResponse<List<GetSearchRes>>
     */
    // Query String
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/app/search
    public BaseResponse<List<GetSearchRes>> getSearchList(){
        try{
            List<GetSearchRes> getSearchRes = searchProvider.getSearchList();
            return new BaseResponse<>(getSearchRes);
        } catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 특정 유저 최근 검색어 조회 API
     * [GET] /search/:userId
     * @return BaseResponse<List<GetSearchRes>>
     */
    @ResponseBody
    @GetMapping("/{userId}")
    public BaseResponse<List<GetUserSearchRes>> getUserSearchList(@PathVariable("userId") int userId){
        try{
            List<GetUserSearchRes> getUserSearchRes = searchProvider.getUserSearchList(userId);
            return new BaseResponse<>(getUserSearchRes);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }



}
