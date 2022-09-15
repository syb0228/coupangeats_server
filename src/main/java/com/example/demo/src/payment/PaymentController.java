package com.example.demo.src.payment;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.payment.model.GetPaymentsListRes;
import com.example.demo.src.payment.model.PostPaymentReq;
import com.example.demo.src.payment.model.PostPaymentRes;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/payments")
@RequiredArgsConstructor
public class PaymentController {

    @Autowired
    private final PaymentProvider paymentProvider;

    @Autowired
    private final PaymentService paymentService;

    @Autowired
    private final JwtService jwtService;

    /**
     * 특정 유저 결제 정보 전체 조회 API
     * [GET] /payments
     * @return BaseResponse<List<GetPaymentsListRes>>
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetPaymentsListRes>> getPaymentsList(){
        try {
            int userIdByJwt = jwtService.getUserId();
            List<GetPaymentsListRes> getPaymentsListRes = paymentProvider.getPaymentsList(userIdByJwt);
            return new BaseResponse<>(getPaymentsListRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 유저 결제 정보 생성 API
     * [POST] /payments
     * @return BaseResponse<PostPaymentRes>
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostPaymentRes> createPayment(@RequestBody PostPaymentReq postPaymentReq){
        try {
            int userIdByJwt = jwtService.getUserId();

            // 결제 정보 유효성 검사
            if(postPaymentReq.getPaymentBank() == null){
                return new BaseResponse<>(POST_PAYMENT_EMPTY_BANK);
            }
            if(postPaymentReq.getPaymentNum() == null){
                return new BaseResponse<>(POST_PAYMENT_EMPTY_NUM);
            }

            if (postPaymentReq.getPaymentMethod() != 0 && postPaymentReq.getPaymentMethod() != 1){
                return new BaseResponse<>(POST_PAYMENT_INVAILD_METHOD);
            }
            if(postPaymentReq.getPaymentNum().length() != 8){
                return new BaseResponse<>(POST_PAYMENT_INVAILD_NUM);
            }

            PostPaymentRes postPaymentRes = paymentService.createPayment(userIdByJwt, postPaymentReq);
            return new BaseResponse<>(postPaymentRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 유저 결제 정보 삭제 API
     * [DELETE] /payments/:paymentId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @DeleteMapping("/{paymentId}")
    public BaseResponse<String> deletePayment(@PathVariable("paymentId") int paymentId){
        try {
            jwtService.getUserId();

            paymentService.deletePayment(paymentId);

            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception){
            return new BaseResponse<>(DATABASE_ERROR);
        }
    }


}
