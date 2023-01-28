package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
        Reservation reservation = reservationRepository2.findById(reservationId).get();
        Payment payment = new Payment();
        payment.setPaymentCompleted(true);
        if(mode.equals("CASH")){
            payment.setPaymentMode(PaymentMode.CASH);
        }else if(mode.equals("CARD")){
            payment.setPaymentMode(PaymentMode.CARD);
        }else if(mode.equals("UPI")){
            payment.setPaymentMode(PaymentMode.UPI);
        }

        reservation.setPayment(payment);
        reservationRepository2.save(reservation);

        //no need to save as we are saving parent i.e. reservation

        return payment;
    }
}
