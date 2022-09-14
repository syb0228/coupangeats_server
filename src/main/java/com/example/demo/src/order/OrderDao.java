package com.example.demo.src.order;

import com.example.demo.src.order.model.*;
import com.example.demo.src.review.model.PostReviewImgReq;
import com.example.demo.src.review.model.PostReviewReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class OrderDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetOrderDetailOptionRes> getOrderDetailOptions(int orderDetailId){
        String getOrderDetailOptionQuery = "select optionName\n" +
                "from OrderDetailOption\n" +
                "inner join MenuDetailOption MDO on OrderDetailOption.menuDetailOptionId = MDO.menuDetailOptionId\n" +
                "where orderDetailId = ?";
        int getOrderDetailOptionParams = orderDetailId;
        return this.jdbcTemplate.query(getOrderDetailOptionQuery,
                (rs, rsNum) -> new GetOrderDetailOptionRes(
                        rs.getString("optionName")),
                getOrderDetailOptionParams);
    }

    public List<GetOrderDetailRes> getOrderDetails(int userOrderId){
        String getOrderDetailQuery = "select orderDetailId, menuName\n" +
                "from Menu\n" +
                "inner join OrderDetail OD on Menu.menuId = OD.menuId\n" +
                "where userOrderId = ?";
        int getOrderDetailParams = userOrderId;
        return this.jdbcTemplate.query(getOrderDetailQuery,
                (rs, rsNum) -> new GetOrderDetailRes(
                        rs.getInt("orderDetailId"),
                        rs.getString("menuName"),
                        getOrderDetailOptions(rs.getInt("orderDetailId"))),
                getOrderDetailParams);
    }

    public List<GetOrderReadyRes> getOrderReady(int userId){
        String getOrderReadyQuery = "select UserOrder.userOrderId\n" +
                "       , (select storeName\n" +
                "            from Store\n" +
                "            where UserOrder.storeId = Store.storeId\n" +
                "        ) as orderStoreName\n" +
                "        , REPLACE(\n" +
                "            REPLACE(\n" +
                "                DATE_FORMAT(UserOrder.createdAt, '%Y-%m-%d %p %h:%i'), 'AM', '오전'\n" +
                "                ), 'PM', '오후') as orderTime\n" +
                "        , case\n" +
                "            when orderStatus = 1 then '주문 대기중'\n" +
                "            else '주문 완료'\n" +
                "            end as orderStatus\n" +
                "        , orderPrice\n" +
                "from UserOrder\n" +
                "inner join Delivery D on UserOrder.userOrderId = D.userOrderId and deliveryStatus = 1\n" +
                "where userId = ? and UserOrder.status = 'active'";
        int getOrderReadyParams = userId;
        return this.jdbcTemplate.query(getOrderReadyQuery,
                (rs, rsNum) -> new GetOrderReadyRes(
                        rs.getInt("userOrderId"),
                        rs.getString("orderStoreName"),
                        rs.getString("orderTime"),
                        rs.getString("orderStatus"),
                        getOrderDetails(rs.getInt("userOrderId")),
                        rs.getInt("orderPrice")),
                getOrderReadyParams);
    }

    public List<GetOrderHistRes> getOrderHist(int userId){
        String getOrderHistQuery = "select UserOrder.userOrderId\n" +
                "        , (select storeName\n" +
                "            from Store\n" +
                "            where UserOrder.storeId = Store.storeId\n" +
                "        ) as orderStoreName\n" +
                "        , REPLACE(\n" +
                "            REPLACE(\n" +
                "                DATE_FORMAT(UserOrder.createdAt, '%Y-%m-%d %p %h:%i'), 'AM', '오전'\n" +
                "                ), 'PM', '오후') as orderTime\n" +
                "        , case\n" +
                "            when orderStatus = 0 then '주문 취소됨'\n" +
                "            else '배달 완료' end as deliveryStatus\n" +
                "        , orderPrice\n" +
                "        , case\n" +
                "            when reviewStatus = 'Y' then '작성한 리뷰 보기'\n" +
                "            else '리뷰 쓰기'\n" +
                "            end as reviewStatus\n" +
                "from UserOrder\n" +
                "inner join Delivery D on UserOrder.userOrderId = D.userOrderId and deliveryStatus = 2\n" +
                "where userId = ? and UserOrder.status = 'active'";
        int getOrderHistParams = userId;
        return this.jdbcTemplate.query(getOrderHistQuery,
                (rs, rsNum) -> new GetOrderHistRes(
                        rs.getInt("userOrderId"),
                        rs.getString("orderStoreName"),
                        rs.getString("orderTime"),
                        rs.getString("deliveryStatus"),
                        getOrderDetails(rs.getInt("userOrderId")),
                        rs.getInt("orderPrice"),
                        rs.getString("reviewStatus")),
                getOrderHistParams);
    }

    public int createOrder(int userId, PostOrderReq postOrderReq){
        String createOrderQuery = "insert into UserOrder (userId, userAddressId, storeId, userPaymentId, storeRequest, disposableItems, deliveryRequest) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Object[] createOrderParams = new Object[]{userId, postOrderReq.getUserAddressId(), postOrderReq.getStoreId(), postOrderReq.getUserPaymentId()
                , postOrderReq.getStoreRequest(), postOrderReq.getDisposableItems(), postOrderReq.getDeliveryRequest()};
        this.jdbcTemplate.update(createOrderQuery, createOrderParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public int createOrderDetail(int userOrderId, PostOrderDetailReq postOrderDetailReq){
        String createOrderDetailQuery = "insert into OrderDetail (userOrderId, menuId, menuCount) VALUES (?, ?, ?)";
        Object[] createOrderDetailParams = new Object[]{userOrderId, postOrderDetailReq.getMenuId(), postOrderDetailReq.getMenuCount()};
        this.jdbcTemplate.update(createOrderDetailQuery, createOrderDetailParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public int createOrderDetailOption(int orderDetailId, PostOrderDetailOptionReq postOrderDetailOptionReq){
        String createOrderDetailOptionQuery = "insert into OrderDetailOption (orderDetailId, menuDetailId, menuDetailOptionId) VALUES (?, ?, ?)";
        Object[] createOrderDetailOptionParams = new Object[]{orderDetailId, postOrderDetailOptionReq.getMenuDetailId(), postOrderDetailOptionReq.getMenuDetailOptionId()};
        this.jdbcTemplate.update(createOrderDetailOptionQuery, createOrderDetailOptionParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }



}
