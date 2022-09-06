package com.example.demo.src.userAddress;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.userAddress.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.POST_USERADDRESS_EMPTY_NAME;


@RestController
@RequestMapping("/app")
public class UserAddressController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final UserAddressProvider userAddressProvider;
    @Autowired
    private final UserAddressService userAddressService;
    @Autowired
    private final JwtService jwtService;

    public UserAddressController(UserAddressProvider userAddressProvider, UserAddressService userAddressService, JwtService jwtService){
        this.userAddressProvider = userAddressProvider;
        this.userAddressService = userAddressService;
        this.jwtService = jwtService;
    }

    /**
     * 특정 유저 전체 주소 조회 API
     * [GET] /user-addresses
     * @return BaseResponse<List<GetUserAddressRes>>
     */
    @ResponseBody
    @GetMapping("/user-addresses")
    public BaseResponse<List<GetUserAddressRes>> getUserAddresses(){
        try{
            int userIdByJwt = jwtService.getUserId();
            List<GetUserAddressRes> getUserAddressRes = userAddressProvider.getUserAddresses(userIdByJwt);
            return new BaseResponse<>(getUserAddressRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 특정 주소 상세 정보 조회 API
     * [GET] /user-address-detail/:userAddressId
     * @return BaseResponse<GetUserAddressDetailRes>
     */
    @ResponseBody
    @GetMapping("/user-address-details/{userAddressId}")
    public BaseResponse<GetUserAddressDetailRes> getUserAddress(@PathVariable("userAddressId") int userAddressId){
        try{
            jwtService.getUserId();

            GetUserAddressDetailRes getUserAddressDetailRes = userAddressProvider.getUserAddress(userAddressId);
            return new BaseResponse<>(getUserAddressDetailRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 유저 주소 생성 API(새 주소 추가)
     * [POST] /user-addresses
     * @return BaseResponse<PostUserAddressRes>
     */
    @ResponseBody
    @PostMapping("/user-addresses")
    public BaseResponse<PostUserAddressRes> createUserAddress(@RequestBody PostUserAddressReq postUserAddressReq){
        // 주소 명칭 유효성 검사
        if(postUserAddressReq.getAddressName() == null) {
            return new BaseResponse<>(POST_USERADDRESS_EMPTY_NAME);
        }

        try{
            int userIdByJwt = jwtService.getUserId();
            PostUserAddressRes postUserAddressRes = userAddressService.createUserAddress(userIdByJwt, postUserAddressReq);
            return new BaseResponse<>(postUserAddressRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 유저 주소 정보 변경 API
     * [PATCH] /user-addresses/:userAddressId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/user-addresses/{userAddressId}")
    public BaseResponse<String> modifyUserAddress(@PathVariable("userAddressId") int userAddressId, @RequestBody UserAddress userAddress){
        try{
            jwtService.getUserId();

            PatchUserAddressReq patchUserAddressReq = new PatchUserAddressReq(userAddressId, userAddress.getAddressDetail(), userAddress.getDirections()
                    , userAddress.getAddressCategory(), userAddress.getAddressAlias());
            // 상세 주소 변경
            if(patchUserAddressReq.getAddressDetail() != null){
                userAddressService.modifyAddressDetail(patchUserAddressReq);
            }
            // 길 안내 변경
            if(patchUserAddressReq.getDirections() != null){
                userAddressService.modifyDirections(patchUserAddressReq);
            }
            // 카테고리 변경 (기존 설정과 다르면)
            if(patchUserAddressReq.getAddressCategory() != userAddressProvider.checkAddressCategory(patchUserAddressReq.getUserAddressId())){
                userAddressService.modifyAddressCategory(patchUserAddressReq);
            }
            // 주소 별칭 변경
            if(patchUserAddressReq.getAddressAlias() != null){
                userAddressService.modifyAddressAlias(patchUserAddressReq);
            }

            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
