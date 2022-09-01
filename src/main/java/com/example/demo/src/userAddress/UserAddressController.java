package com.example.demo.src.userAddress;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.userAddress.model.GetUserAddressDetailRes;
import com.example.demo.src.userAddress.model.GetUserAddressRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/app")
public class UserAddressController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final UserAddressProvider userAddressProvider;
    @Autowired
    private final UserAddressService userAddressService;

    public UserAddressController(UserAddressProvider userAddressProvider, UserAddressService userAddressService){
        this.userAddressProvider = userAddressProvider;
        this.userAddressService = userAddressService;
    }

    /**
     * 특정 유저 전체 주소 조회 API
     * [GET] /user-addresses/:userId
     * @return BaseResponse<List<GetUserRes>>
     */
    @ResponseBody
    @GetMapping("/user-addresses/{userId}")
    public BaseResponse<List<GetUserAddressRes>> getUserAddresses(@PathVariable("userId") int userId){
        try{
            List<GetUserAddressRes> getUserAddressRes = userAddressProvider.getUserAddresses(userId);
            return new BaseResponse<>(getUserAddressRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 특정 주소 상세 정보 조회 API
     * [GET] /user-address-detail/:userAddressId
     * @return BaseResponse<List<GetUserRes>>
     */
    @ResponseBody
    @GetMapping("/user-address-details/{userAddressId}")
    public BaseResponse<GetUserAddressDetailRes> getUserAddress(@PathVariable("userAddressId") int userAddressId){
        try{
            GetUserAddressDetailRes getUserAddressDetailRes = userAddressProvider.getUserAddress(userAddressId);
            return new BaseResponse<>(getUserAddressDetailRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
