package com.example.demo.src.store;

import com.example.demo.config.BaseException;
import com.example.demo.src.store.model.GetStoreInfoRes;
import com.example.demo.src.store.model.GetStoreRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class StoreProvider {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final StoreDao storeDao;

    @Autowired
    public StoreProvider(StoreDao storeDao){
        this.storeDao = storeDao;
    }

    public GetStoreRes getStore(int userId, int storeId) throws BaseException {
        try{
            GetStoreRes getStoreRes = storeDao.getStore(userId, storeId);
            return getStoreRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetStoreInfoRes getStoreInfo(int storeId) throws BaseException {
        try{
            GetStoreInfoRes getStoreInfoRes = storeDao.getStoreInfo(storeId);
            return getStoreInfoRes;
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}