package com.example.demo.src.userAddress;

import com.example.demo.config.BaseException;
import com.example.demo.src.userAddress.model.GetUserAddressDetailRes;
import com.example.demo.src.userAddress.model.GetUserAddressRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@Transactional(readOnly = true)
public class UserAddressProvider {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserAddressDao userAddressDao;

    @Autowired
    public UserAddressProvider(UserAddressDao userAddressDao){
        this.userAddressDao = userAddressDao;
    }

    public List<GetUserAddressRes> getUserAddresses(int userId) throws BaseException {
        try{
            List<GetUserAddressRes> getUserAddressRes = userAddressDao.getUserAddresses(userId);
            return getUserAddressRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetUserAddressDetailRes getUserAddress(int userAddressId) throws BaseException {
        try {
            GetUserAddressDetailRes getUserAddressDetailRes = userAddressDao.getUserAddress(userAddressId);
            return getUserAddressDetailRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkAddressCategory(int userAddressId) throws BaseException {
        try{
            return userAddressDao.checkAddressCategory(userAddressId);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
