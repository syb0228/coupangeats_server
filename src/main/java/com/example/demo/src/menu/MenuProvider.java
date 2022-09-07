package com.example.demo.src.menu;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.menu.model.GetMenuRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class MenuProvider {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private MenuDao menuDao;

    @Autowired
    public MenuProvider(MenuDao menuDao){
        this.menuDao = menuDao;
    }

    public List<GetMenuRes> getMenus(int menuId) throws BaseException {
        try{
            List<GetMenuRes> getMenuRes = menuDao.getMenus(menuId);
            return getMenuRes;
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
