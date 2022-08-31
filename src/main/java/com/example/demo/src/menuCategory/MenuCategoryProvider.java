package com.example.demo.src.menuCategory;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.menuCategory.model.GetMenuCategoryRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class MenuCategoryProvider {

    private final MenuCategoryDao menuCategoryDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public MenuCategoryProvider(MenuCategoryDao menuCategoryDao){
        this.menuCategoryDao = menuCategoryDao;
    }

    public List<GetMenuCategoryRes> getMenuCategories() throws BaseException {
        try{
            List<GetMenuCategoryRes> getMenuCategoryRes = menuCategoryDao.getMenuCategories();
            return getMenuCategoryRes;
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetMenuCategoryRes> getMenuCategoriesByName(String menuCategoryName) throws BaseException {
        try {
            List<GetMenuCategoryRes> getMenuCategoryRes = menuCategoryDao.getMenuCategoriesByName(menuCategoryName);
            return getMenuCategoryRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
