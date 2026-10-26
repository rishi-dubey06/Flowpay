package com.cfs.Flowpay.settelment;

import com.cfs.Flowpay.common.ApiException;
import com.cfs.Flowpay.common.Idempotency;
import com.cfs.Flowpay.common.Ids;
import com.cfs.Flowpay.merchant.Merchant;
import com.cfs.Flowpay.merchant.MerchantService;
import com.cfs.Flowpay.transaction.LegerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettlementService {

    private final SettlementRepository repo;
    private final MerchantService merchants;
    private final LegerService leger;


    public SettlementService(SettlementRepository repo, MerchantService merchants, LegerService leger) {
        this.repo = repo;
        this.merchants = merchants;
        this.leger = leger;
    }

    public Settlement create(String m,String key){
        Idempotency.validate(key);
        Merchant merchant=merchants.lock(m);
        Settlement old=repo.findByMerchantIdAndIdempotencyKey(m,key).orElse(null);;
        if(old!=null) return old;
        if(merchant.balance<0) throw ApiException.conflict("No available balance to settle ");
        Settlement s=new Settlement();
        s.id= Ids.next("stl");
        s.merchantId=m;
        s.idempotencyKey=key;
        s.amount=merchant.balance;
        repo.save(s);
        leger.record(merchant,s.id,"SETTLEMENT",s.amount);
        return  s;
    }

    public List<Settlement> list(String s){
        return repo.findByMerchantIdOrderByCreatedAtDesc(s);
    }
}
