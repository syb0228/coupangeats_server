package com.example.demo.src.payment;

import com.example.demo.src.payment.model.GetPaymentsRes;
import com.example.demo.src.payment.model.GetPaymentsListRes;
import com.example.demo.src.payment.model.PostPaymentReq;
import com.example.demo.src.payment.model.PostPaymentRes;
import com.example.demo.src.userAddress.model.PatchUserAddressReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class PaymentDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetPaymentsRes> getPayments(int userId, int paymentMethod){
        String getPaymentsQuery = "select paymentBank\n" +
                "     , case\n" +
                "         when paymentMethod = 1 then replace(paymentNum, SUBSTRING(paymentNum, 1, 4), '****')\n" +
                "         else replace(replace(paymentNum, SUBSTRING(paymentNum, 1, 4), '****'), SUBSTRING(paymentNum, 8, 1), '*') \n" +
                "         end as paymentNum\n" +
                "from UserPayment\n" +
                "where userId = ? and paymentMethod = ? and status = 'active'";
        int getPaymentsParams1 = userId;
        int getPaymentsParams2 = paymentMethod;
        return this.jdbcTemplate.query(getPaymentsQuery,
                (rs, rsNum) -> new GetPaymentsRes(
                        rs.getString("paymentBank"),
                        rs.getString("paymentNum")),
                getPaymentsParams1, getPaymentsParams2);
    }

    public List<GetPaymentsListRes> getPaymentsList(int userId){
        String getPaymentsListQuery = "select distinct paymentMethod\n" +
                "    , case\n" +
                "        when paymentMethod = 0 then '신용/체크카드'\n" +
                "        else '계좌이체' end as paymentMethodName\n" +
                "from UserPayment\n" +
                "where userId = ? and status = 'active'";
        int getPaymentsListParams = userId;
        return this.jdbcTemplate.query(getPaymentsListQuery,
                (rs, rsNum) -> new GetPaymentsListRes(
                        rs.getInt("paymentMethod"),
                        rs.getString("paymentMethodName"),
                        getPayments(userId, rs.getInt("paymentMethod"))),
                getPaymentsListParams);
    }

    public int createPayment(int userId, PostPaymentReq postPaymentReq){
        String createPaymentQuery = "insert into UserPayment (userId, paymentMethod, paymentBank, paymentNum) VALUES (?, ?, ?, ?)";
        Object[] createPaymentParams = new Object[]{userId, postPaymentReq.getPaymentMethod(), postPaymentReq.getPaymentBank(), postPaymentReq.getPaymentNum()};
        this.jdbcTemplate.update(createPaymentQuery, createPaymentParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public int deletePayment(int paymentId){
        String deletePaymentQuery = "update UserPayment set status = 'deleted' where paymentId = ?";
        int deletePaymentParams = paymentId;
        return this.jdbcTemplate.update(deletePaymentQuery, deletePaymentParams);
    }

}
