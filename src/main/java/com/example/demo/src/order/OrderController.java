package com.example.demo.src.order;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.order.model.GetOrderReadyRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
     * @return
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

}
