package com.example.demo.src.order;

import com.example.demo.config.BaseException;
import com.example.demo.src.order.model.PostOrderReq;
import com.example.demo.src.order.model.PostOrderRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@Transactional
public class OrderService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final OrderDao orderDao;
    private final JwtService jwtService;

    @Autowired
    public OrderService(OrderDao orderDao, JwtService jwtService){
        this.orderDao = orderDao;
        this.jwtService = jwtService;
    }

    public PostOrderRes createOrder(int userId, PostOrderReq postOrderReq) throws BaseException {
//        try {
            int userOrderId = orderDao.createOrder(userId, postOrderReq);
            int orderDetailId = 0;
            int deliveryId = 0;
            int orderOptionId = 0;
            for(int i = 0; i < postOrderReq.getOrderDetails().size(); i++){
                orderDetailId = orderDao.createOrderDetail(userOrderId, postOrderReq.getOrderDetails().get(i));
                deliveryId = orderDao.createDelivery(userOrderId);
                for(int j = 0; j < postOrderReq.getOrderDetails().get(i).getOrderDetailOptions().size(); j++){
                    orderOptionId = orderDao.createOrderDetailOption(orderDetailId, postOrderReq.getOrderDetails().get(i).getOrderDetailOptions().get(j));
                }
            }
            return new PostOrderRes(userOrderId, orderDetailId, deliveryId, orderOptionId);
//        } catch (Exception exception){
//            throw new BaseException(DATABASE_ERROR);
//        }
    }

}
