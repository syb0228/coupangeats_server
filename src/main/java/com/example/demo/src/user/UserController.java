package com.example.demo.src.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.*;

@RestController
@RequestMapping("/app/users")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;


    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService){
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * 회원 조회 API
     * [GET] /users
     * 회원 번호 및 이메일 검색 조회 API
     * [GET] /users? Email=
     * @return BaseResponse<List<GetUserRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/app/users
    public BaseResponse<List<GetUserRes>> getUsers(@RequestParam(required = false) String userEmail) {
        try{
            if(userEmail == null){
                List<GetUserRes> getUsersRes = userProvider.getUsers();
                return new BaseResponse<>(getUsersRes);
            }
            // Get Users
            List<GetUserRes> getUsersRes = userProvider.getUsersByEmail(userEmail);
            return new BaseResponse<>(getUsersRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 회원 1명 조회 API
     * [GET] /users/:userIdx
     * @return BaseResponse<GetUserRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{userId}") // (GET) 127.0.0.1:9000/app/users/:userId
    public BaseResponse<GetUserRes> getUser(@PathVariable("userId") int userId) {
        // Get Users
        try{
            GetUserRes getUserRes = userProvider.getUser(userId);
            return new BaseResponse<>(getUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 회원가입 API
     * [POST] /users
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {
        // 이메일 유효성 검사
        if(postUserReq.getUserEmail() == null) { // null 값이 입력된 경우
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }
        if(!isRegexEmail(postUserReq.getUserEmail())){ // 이메일 정규 표현
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }

        // 비밀번호 유효성 검사
        if(postUserReq.getUserPassword() == null) { // null 값이 입력된 경우
            return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
        }
        if(!isRegexPassword(postUserReq.getUserPassword())){ // 비밀번호 정규 표현
            return new BaseResponse<>(POST_USERS_INVALID_PASSWORD_1);
        }
//        if(!isRegexPassword2(postUserReq.getUserPassword())){
//            return new BaseResponse<>(POST_USERS_INVALID_PASSWORD_2);
//        }
        if(postUserReq.getUserPassword().contains(postUserReq.getUserEmail())){ // 아이디(이메일) 제외
            return new BaseResponse<>(POST_USERS_INVALID_PASSWORD_3);
        }

        // 이름 유효성 검사
        if(postUserReq.getUserName() == null || postUserReq.getUserName().length() < 2){ // null 값이 입력된 경우, 길이가 2 미만인 경우
            return new BaseResponse<>(POST_USERS_EMPTY_NAME);
        }

        // 휴대폰번호 유효성 검사
        if(postUserReq.getUserPhoneNum() == null){ // null 값이 입력된 경우
            return new BaseResponse<>(POST_USERS_EMPTY_PHONENUM);
        }
        if(!isRegexPhoneNum(postUserReq.getUserPhoneNum())){ // 휴대폰번호 정규 표현
            return new BaseResponse<>(POST_USERS_EMPTY_PHONENUM);
        }

        try{
            PostUserRes postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 로그인 API
     * [POST] /users/logIn
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("/logIn")
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq){
        try{
            // 이메일 유효성 검사
            if(postLoginReq.getUserEmail() == null){ // null 값인 경우
                return new BaseResponse<>(POST_LOGIN_EMPTY_EMAIL);
            }
            if(!isRegexEmail(postLoginReq.getUserEmail())){ // 이메일 정규 표현
                return new BaseResponse<>(POST_LOGIN_INVALID_EMAIL);
            }

            // 비밀번호 유효성 검사
            if(postLoginReq.getUserPassword() == null){ // null 값인 경우
                return new BaseResponse<>(POST_LOGIN_EMPTY_PASSWORD);
            }

            PostLoginRes postLoginRes = userProvider.logIn(postLoginReq);
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 유저정보변경 API
     * [PATCH] /users/:userIdx
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userIdx}")
    public BaseResponse<String> modifyUserName(@PathVariable("userIdx") int userIdx, @RequestBody User user){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 유저네임 변경
            PatchUserReq patchUserReq = new PatchUserReq(userIdx,user.getUserName());
            userService.modifyUserName(patchUserReq);

            String result = "";
        return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}
