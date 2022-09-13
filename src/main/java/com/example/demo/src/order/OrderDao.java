package com.example.demo.src.order;

import com.example.demo.src.order.model.GetOrderDetailRes;
import com.example.demo.src.order.model.GetOrderReadyRes;
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

    public List<GetOrderDetailRes> getOrderDetails(int userOrderId){
        String getOrderDetailQuery = "select menuName\n" +
                "from Menu\n" +
                "inner join OrderDetail OD on Menu.menuId = OD.menuId\n" +
                "where userOrderId = ?";
        int getOrderDetailParams = userOrderId;
        return this.jdbcTemplate.query(getOrderDetailQuery,
                (rs, rsNum) -> new GetOrderDetailRes(
                        rs.getString("menuName")),
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
                "inner join Delivery D on UserOrder.userOrderId = D.userOrderId and deliveryStatus = '1'\n" +
                "where userId = ? and UserOrder.status = 'active'";
        int getOrderReadyParams = userId;
        return this.jdbcTemplate.query(getOrderReadyQuery,
                (rs, rsNum) -> new GetOrderReadyRes(
                        rs.getInt("userOrderId"),
                        rs.getString("orderStoreName"),
                        rs.getString("orderTime"),
                        rs.getString("orderStatus"),
                        rs.getInt("orderPrice"),
                        getOrderDetails(rs.getInt("userOrderId"))),
                getOrderReadyParams);
    }

}
