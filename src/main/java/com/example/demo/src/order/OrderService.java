package com.example.demo.src.order;

import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
