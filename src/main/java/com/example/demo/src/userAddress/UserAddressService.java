package com.example.demo.src.userAddress;

import com.example.demo.config.BaseException;
import com.example.demo.src.userAddress.model.PatchUserAddressReq;
import com.example.demo.src.userAddress.model.PostUserAddressReq;
import com.example.demo.src.userAddress.model.PostUserAddressRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
@Transactional
public class UserAddressService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserAddressDao userAddressDao;
    private final UserAddressProvider userAddressProvider;

    @Autowired
    public UserAddressService(UserAddressDao userAddressDao, UserAddressProvider userAddressProvider){
        this.userAddressDao = userAddressDao;
        this.userAddressProvider = userAddressProvider;
    }

    public PostUserAddressRes createUserAddress(int userId, PostUserAddressReq postUserAddressReq) throws BaseException {
        try{
            int userAddressId = userAddressDao.createUserAddress(userId, postUserAddressReq);
            return new PostUserAddressRes(userAddressId);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyAddressDetail(PatchUserAddressReq patchUserAddressReq) throws BaseException {
        try{
            int result = userAddressDao.modifyAddressDetail(patchUserAddressReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_ADDRESS_DETAIL);
            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyDirections(PatchUserAddressReq patchUserAddressReq) throws BaseException {
        try{
            int result = userAddressDao.modifyDirections(patchUserAddressReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_DIRECTIONS);
            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyAddressCategory(PatchUserAddressReq patchUserAddressReq) throws BaseException {
        try{
            int result = userAddressDao.modifyAddressCategory(patchUserAddressReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_ADDRESS_CATEGORY);
            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyAddressAlias(PatchUserAddressReq patchUserAddressReq) throws BaseException {
        try{
            int result = userAddressDao.modifyAddressAlias(patchUserAddressReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_ADDRESS_ALIAS);
            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteUserAddress(int userAddressId) throws BaseException {
        try {
            int result = userAddressDao.deleteUserAddress(userAddressId);
            if(result == 0){
                throw new BaseException(DELETE_FAIL_ADDRESS);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
