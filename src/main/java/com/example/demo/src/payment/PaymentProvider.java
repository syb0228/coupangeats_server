package com.example.demo.src.payment;

import com.example.demo.config.BaseException;
import com.example.demo.src.payment.model.GetPaymentsListRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@Transactional(readOnly = true)
public class PaymentProvider {

    private final PaymentDao paymentDao;

    @Autowired
    public PaymentProvider(PaymentDao paymentDao) {
        this.paymentDao = paymentDao;
    }

    public List<GetPaymentsListRes> getPaymentsList(int userId) throws BaseException {
        try {
            List<GetPaymentsListRes> getPaymentsListRes = paymentDao.getPaymentsList(userId);
            return getPaymentsListRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
