package com.cfs.Flowpay.transaction;


import com.cfs.Flowpay.common.Ids;
import com.cfs.Flowpay.merchant.Merchant;
import org.springframework.stereotype.Service;

@Service
public class LegerService {

    private final LegerEntryRepository repo;

    public LegerService(LegerEntryRepository repo) {
        this.repo = repo;
    }
public void record(Merchant m, String reference,String type, long delta){
        m.balance=Math.addExact(m.balance,delta);
        LegerEntity t=new LegerEntity();
        t.id= Ids.next("txn");
        t.merchantId=m.id;
        t.referenceId=reference;
        t.type=type;
        t.amount=delta;
        t.balanceAfter=m.balance;
        repo.save(t);
}

}
