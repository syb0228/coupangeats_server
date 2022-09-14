package com.example.demo.src.mainMenuCategory;

import com.example.demo.config.BaseException;
import com.example.demo.src.mainMenuCategory.model.GetMainMenuCategoryRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@Transactional(readOnly = true)
public class MainMenuCategoryProvider {

    private final MainMenuCategoryDao mainMenuCategoryDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public MainMenuCategoryProvider(MainMenuCategoryDao mainMenuCategoryDao){
        this.mainMenuCategoryDao = mainMenuCategoryDao;
    }

    public List<GetMainMenuCategoryRes> getMainMenuCategories() throws BaseException {
        try{
            List<GetMainMenuCategoryRes> getMainMenuCategoryRes = mainMenuCategoryDao.getMainMenuCategories();
            return getMainMenuCategoryRes;
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetMainMenuCategoryRes> getMainMenuCategoriesByName(String mainMenuCategoryName) throws BaseException {
        try {
            List<GetMainMenuCategoryRes> getMainMenuCategoryRes = mainMenuCategoryDao.getMainMenuCategoriesByName(mainMenuCategoryName);
            return getMainMenuCategoryRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
