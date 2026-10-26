package com.cfs.Flowpay.bank;


import com.cfs.Flowpay.payment.PaymentRepository;
import com.cfs.Flowpay.payment.PaymentService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name="app.jobs-enabled",havingValue = "true")
public class BankJobs {

    private final PaymentRepository repo;
    private final PaymentService service;


    public BankJobs(PaymentRepository repo, PaymentService service) {
        this.repo = repo;
        this.service = service;
    }

    @Scheduled(fixedDelay = 2000)
    public void completeDelayed(){

    }
}



