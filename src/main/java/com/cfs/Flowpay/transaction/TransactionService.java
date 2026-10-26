package com.cfs.Flowpay.transaction;


import com.cfs.Flowpay.common.ApiException;
import com.cfs.Flowpay.payment.Payment;
import com.cfs.Flowpay.payment.PaymentService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class TransactionService {

    private final PaymentService payments;
    private final LegerEntryRepository leger;

    public TransactionService(PaymentService payments, LegerEntryRepository leger) {
        this.payments = payments;
        this.leger = leger;
    }

    public List<Payment> list(String merchant, String status, LocalDate from,LocalDate to){
        if(from!=null && to != null && from.isAfter(to)){
            throw ApiException.bad("From date must not be after to date");
        }
        return payments.list(merchant).stream()
                .filter(p->status==null || status.isBlank() || status.equals(p.status))
                .filter(p->
                        from== null || !p.createdAt.isBefore(from.atStartOfDay(ZoneOffset.UTC).toInstant()))
                .filter(p->
                        to==null || p.createdAt.isBefore(to.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant()))
                .toList();
    }

    public List<LegerEntity> leger(String m){
        return leger.findByMerchantIdOrderByCreatedAtDesc(m);
    }
}
