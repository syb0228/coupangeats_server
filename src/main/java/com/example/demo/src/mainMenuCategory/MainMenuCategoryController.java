package com.example.demo.src.mainMenuCategory;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.mainMenuCategory.model.GetMainMenuCategoryRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/menu-categories")
public class MainMenuCategoryController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final MainMenuCategoryProvider mainMenuCategoryProvider;

    public MainMenuCategoryController(MainMenuCategoryProvider mainMenuCategoryProvider){
        this.mainMenuCategoryProvider = mainMenuCategoryProvider;
    }

    /**
     * 전체 메뉴 카테고리 조회 API
     * [GET] /menu-categories
     * 메뉴 카테고리 번호 및 이름 검색 조회 API
     * [GET] /menu-categories? mainMenuCategoryName
     * @return BaseResponse<List<GetMainMenuCategoryRes>>
     */
     // Query String
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/app/menu-categories
    public BaseResponse<List<GetMainMenuCategoryRes>> getMenuCategories(@RequestParam(required = false) String mainMenuCategoryName) {
        try{
            if(mainMenuCategoryName == null){
                List<GetMainMenuCategoryRes> getMainMenuCategoryRes = mainMenuCategoryProvider.getMainMenuCategories();
                return new BaseResponse<>(getMainMenuCategoryRes);
            }
            // Get MenuCategories
            List<GetMainMenuCategoryRes> getMainMenuCategoryRes = mainMenuCategoryProvider.getMainMenuCategoriesByName(mainMenuCategoryName);
            return new BaseResponse<>(getMainMenuCategoryRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }


}
