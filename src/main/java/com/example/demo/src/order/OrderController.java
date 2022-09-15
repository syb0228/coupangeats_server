package com.example.demo.src.order;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.order.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.POST_USERORDER_INVAILD_ITEM;

@RestController
@RequestMapping("/app")
public class OrderController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final OrderProvider orderProvider;
    @Autowired
    private final OrderService orderService;
    @Autowired
    private final JwtService jwtService;

    public OrderController(OrderProvider orderProvider, OrderService orderService, JwtService jwtService){
        this.orderProvider = orderProvider;
        this.orderService = orderService;
        this.jwtService = jwtService;
    }

    /**
     * 준비 중인 주문 조회 API
     * [GET] /orders
     * @return BaseResponse<List<GetOrderReadyRes>>
     */
    @ResponseBody
    @GetMapping("/orders")
    public BaseResponse<List<GetOrderReadyRes>> getOrderReady(){
        try {
            int userIdByJwt = jwtService.getUserId();
            List<GetOrderReadyRes> getOrderReadyRes = orderProvider.getOrderReady(userIdByJwt);
            return new BaseResponse<>(getOrderReadyRes);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 과거 주문 내역 조회 API
     * [GET] /orderhists
     * @return BaseResponse<List<GetOrderHistRes>>
     */
    @ResponseBody
    @GetMapping("/orderhists")
    public BaseResponse<List<GetOrderHistRes>> getOrderHist(){
        try {
            int userIdByJwt = jwtService.getUserId();
            List<GetOrderHistRes> getOrderHistRes = orderProvider.getOrderHist(userIdByJwt);
            return new BaseResponse<>(getOrderHistRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 주문 생성 API
     * [POST] /orders
     * @return BaseResponse<PostOrderRes>
     */
    @ResponseBody
    @PostMapping("/orders")
    public BaseResponse<PostOrderRes> createOrder(@RequestBody PostOrderReq postOrderReq){
        try {
            int userIdByJwt = jwtService.getUserId();

            // 일회용품 여부 유효성 검사
            if(postOrderReq.getDisposableItems() != 0 && postOrderReq.getDisposableItems() != 1){
                return new BaseResponse<>(POST_USERORDER_INVAILD_ITEM);
            }

            PostOrderRes postOrderRes = orderService.createOrder(userIdByJwt, postOrderReq);
            return new BaseResponse<>(postOrderRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 주문/배달 현황 조회 API
     * [GET] /orders/:userOrderId
     * @return BaseResponse<GetOrderStatusRes>
     */
    @ResponseBody
    @GetMapping("/orders/{userOrderId}")
    public BaseResponse<GetOrderStatusRes> getOrderStatus(@PathVariable("userOrderId") int userOrderId){
        try {
            jwtService.getUserId();

            GetOrderStatusRes getOrderStatusRes = orderProvider.getOrderStatus(userOrderId);
            return new BaseResponse<>(getOrderStatusRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }


}
