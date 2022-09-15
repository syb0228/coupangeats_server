package com.example.demo.src.payment;

import com.example.demo.config.BaseException;
import com.example.demo.src.payment.model.PostPaymentReq;
import com.example.demo.src.payment.model.PostPaymentRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
@Transactional
public class PaymentService {

    private final PaymentDao paymentDao;

    @Autowired
    public PaymentService(PaymentDao paymentDao) {
        this.paymentDao = paymentDao;
    }

    public PostPaymentRes createPayment(int userId, PostPaymentReq postPaymentReq) throws BaseException {
        try {
            int paymentId = paymentDao.createPayment(userId, postPaymentReq);
            return new PostPaymentRes(paymentId);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deletePayment(int paymentId) throws BaseException {
        try {
            int result = paymentDao.deletePayment(paymentId);
            if(result == 0){
                throw new BaseException(DELETE_FAIL_PAYMENT);
            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
