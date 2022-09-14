package com.example.demo.src.search;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.search.model.GetSearchRes;
import com.example.demo.src.search.model.GetUserSearchRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@Transactional(readOnly = true)
public class SearchProvider {

    private final SearchDao searchDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public SearchProvider(SearchDao searchDao){
        this.searchDao = searchDao;
    }

    public List<GetSearchRes> getSearchList() throws BaseException {
        try {
            List<GetSearchRes> getSearchRes = searchDao.getSearchList();
            return getSearchRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetUserSearchRes> getUserSearchList(int userId) throws BaseException {
        try {
            List<GetUserSearchRes> getUserSearchRes = searchDao.getUserSearchList(userId);
            return getUserSearchRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
