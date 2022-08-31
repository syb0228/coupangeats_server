package com.example.demo.src.menuCategory;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.menuCategory.model.GetMenuCategoryRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/menu-categories")
public class MenuCategoryController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final MenuCategoryProvider menuCategoryProvider;

    @Autowired
    private final MenuCategoryService menuCategoryService;

    public MenuCategoryController(MenuCategoryProvider menuCategoryProvider, MenuCategoryService menuCategoryService){
        this.menuCategoryProvider = menuCategoryProvider;
        this.menuCategoryService = menuCategoryService;
    }

    /**
     * 전체 메뉴 카테고리 조회 API
     * [GET] /menu-categories
     * 메뉴 카테고리 번호 및 이름 검색 조회 API
     * [GET] /menu-categories? menuCategoryName
     * @return BaseResponse<List<GetMenuCategoryRes>>
     */
     // Query String
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/app/menu-categories
    public BaseResponse<List<GetMenuCategoryRes>> getMenuCategories(@RequestParam(required = false) String menuCategoryName) {
        try{
            if(menuCategoryName == null){
                List<GetMenuCategoryRes> getMenuCategoryRes = menuCategoryProvider.getMenuCategories();
                return new BaseResponse<>(getMenuCategoryRes);
            }
            // Get MenuCategories
            List<GetMenuCategoryRes> getMenuCategoryRes = menuCategoryProvider.getMenuCategoriesByName(menuCategoryName);
            return new BaseResponse<>(getMenuCategoryRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }


}
