package com.example.demo.src.user;


import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
@Transactional(readOnly = true)
public class UserProvider {

    private final UserDao userDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }

    public GetUserRes getUser(int userId) throws BaseException {
        try {
            GetUserRes getUserRes = userDao.getUser(userId);
            return getUserRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkEmail(String email) throws BaseException{
        try{
            return userDao.checkEmail(email);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostLoginRes logIn(PostLoginReq postLoginReq) throws BaseException{
        // 이메일 유효성 검사
        if(userDao.checkEmail(postLoginReq.getUserEmail()) == 0){ // 존재하지 않은 이메일
            throw new BaseException(FAILED_TO_LOGIN);
        }

        User user = userDao.getPwd(postLoginReq);
        // 유효성 검사
        if(user.getStatus().equals("inactive")){ // 비활성화된 유저
            throw new BaseException(INACTIVE_USER);
        }
        if(user.getStatus().equals("deleted")){ // 탈퇴한 유저
            throw new BaseException(DELETED_USER);
        }

        String encryptPwd;
        try {
            encryptPwd=new SHA256().encrypt(postLoginReq.getUserPassword());
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }

        if(user.getUserPassword().equals(encryptPwd)){
            int userId = user.getUserId();
            String jwt = jwtService.createJwt(userId);
            return new PostLoginRes(userId,jwt);
        }
        else{
            throw new BaseException(FAILED_TO_LOGIN);
        }

    }

}
