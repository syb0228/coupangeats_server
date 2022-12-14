package com.example.demo.src.order;

import com.example.demo.config.BaseException;
import com.example.demo.src.order.model.GetOrderHistRes;
import com.example.demo.src.order.model.GetOrderReadyRes;
import com.example.demo.src.order.model.GetOrderStatusRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@Transactional(readOnly = true)
public class OrderProvider {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final OrderDao orderDao;
    private final JwtService jwtService;

    @Autowired
    public OrderProvider(OrderDao orderDao, JwtService jwtService){
        this.orderDao = orderDao;
        this.jwtService = jwtService;
    }

    public List<GetOrderReadyRes> getOrderReady(int userId) throws BaseException {
        try {
            List<GetOrderReadyRes> getOrderReadyRes = orderDao.getOrderReady(userId);
            return getOrderReadyRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetOrderHistRes> getOrderHist(int userId) throws BaseException {
        try {
            List<GetOrderHistRes> getOrderHistRes = orderDao.getOrderHist(userId);
            return getOrderHistRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetOrderStatusRes getOrderStatus(int userOrderId) throws BaseException {
        try {
            GetOrderStatusRes getOrderStatusRes = orderDao.getOrderStatus(userOrderId);
            return getOrderStatusRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
