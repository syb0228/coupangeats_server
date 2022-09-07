package com.example.demo.src.menu;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.menu.model.GetMenuRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/menus")
public class MenuController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final MenuProvider menuProvider;

    public MenuController(MenuProvider menuProvider){
        this.menuProvider = menuProvider;
    }

    /**
     * 메뉴 상세 조회 API
     * [GET] /menus/menuId
     * @return BaseResponse<List<GetMenuRes>>
     */
    @ResponseBody
    @GetMapping("/{menuId}")
    public BaseResponse<List<GetMenuRes>> getMenu(@PathVariable("menuId") int menuId){
        try{
            List<GetMenuRes> getMenuRes = menuProvider.getMenus(menuId);
            return new BaseResponse<>(getMenuRes);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }


}
