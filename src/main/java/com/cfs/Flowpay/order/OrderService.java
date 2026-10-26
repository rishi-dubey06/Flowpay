package com.cfs.Flowpay.order;

import com.cfs.Flowpay.common.ApiException;
import com.cfs.Flowpay.common.Ids;
import com.cfs.Flowpay.merchant.MerchantService;
import com.cfs.Flowpay.payment.PaymentService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final PaymentOrderRepository repo;
    private final MerchantService merchants;


    public OrderService(PaymentOrderRepository repo, MerchantService merchants) {
        this.repo = repo;
        this.merchants = merchants;
    }


    @Transactional
    public PaymentOrder create(String merchantId,OrderDtos.Create r){

        merchants.lock(merchantId);
        PaymentOrder o=new PaymentOrder();
        o.id= Ids.next("order");
        o.amount= r.amount();
        o.merchantId=merchantId;
        o.receipt=r.receipt();
        return repo.save(o);
    }


    public PaymentOrder get(String merchantId, String id){
        PaymentOrder o=repo.findById(id).orElseThrow(ApiException::missing);
        if(!o.merchantId.equals(merchantId)){
            throw  ApiException.missing();
        }
        return o;
    }

    public List<PaymentOrder> list(String merchantId){
        return repo.findByMerchantIdOrderByCreatedAtDesc(merchantId);
    }







}
