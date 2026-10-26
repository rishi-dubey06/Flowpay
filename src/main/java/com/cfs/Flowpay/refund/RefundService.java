package com.cfs.Flowpay.refund;


import com.cfs.Flowpay.common.ApiException;
import com.cfs.Flowpay.common.Idempotency;
import com.cfs.Flowpay.common.Ids;
import com.cfs.Flowpay.kafka.EventService;
import com.cfs.Flowpay.merchant.Merchant;
import com.cfs.Flowpay.merchant.MerchantService;
import com.cfs.Flowpay.payment.Payment;
import com.cfs.Flowpay.payment.PaymentService;
import com.cfs.Flowpay.transaction.LegerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RefundService {

    private final RefundRepository repo;
    private final MerchantService merchants;
    private final PaymentService payments;
    private final EventService events;
    private final LegerService leger;


    public RefundService(RefundRepository repo, MerchantService merchants, PaymentService payments, EventService events, LegerService leger) {
        this.repo = repo;
        this.merchants = merchants;
        this.payments = payments;
        this.events = events;
        this.leger = leger;
    }

    public Refund create(String m,String key,RefundDtos.Create request){
        Idempotency.validate(key);
        Merchant merchant=merchants.lock(m);
        Refund old=repo.findByMerchantIdAndIdempotencyKey(m,key).orElse(null);
        if(old!=null){
            if(!old.paymentId.equals(request.paymentId()) || old.amount!=request.amount()){
                throw ApiException.conflict("Idempotency key reused with different refund");
            }
            return  old;
        }

        Payment p= payments.get(m,request.paymentId());
        if(!p.status.equals("SUCCESS")){
            throw ApiException.conflict("Only Success payment can be refunded");
        }
        if (request.amount()<1 || request.amount()>p.amount-p.refunded){
            throw  ApiException.conflict("Refund exceeds remaining payment amount");
        }

        long before =p.refunded;
        long after =before+request.amount();
        long feeReturned=(p.fee*after/p.amount)-(p.fee*before/p.amount);
        long debit=request.amount()-feeReturned;
        if(merchant.balance<debit){
            throw ApiException.conflict("Insufficient availiable balance.");
        }

        Refund r=new Refund();
        r.id= Ids.next("rfnd");
        r.merchantId=m;
        r.paymentId=p.id;
        r.idempotencyKey=key;
        r.amount=request.amount();
        p.refunded=after;
        repo.save(r);
        leger.record(merchant,r.id,"REFUND",-debit);
//        events.create();
        return r;
    }

    public List<Refund> list (String s){
        return repo.findByMerchantIdOrderByCreatedAtDesc(s);
    }



}
