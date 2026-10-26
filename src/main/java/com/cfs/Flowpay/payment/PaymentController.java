package com.cfs.Flowpay.payment;


import com.cfs.Flowpay.merchant.MerchantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.swing.plaf.PanelUI;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentservice;
    private final MerchantService merchantService;

    public PaymentController(PaymentService paymentservice, MerchantService merchantService) {
        this.paymentservice = paymentservice;
        this.merchantService = merchantService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Payment create(@RequestHeader("X-Api-Key") String key,
                          @RequestHeader ("Idempotency-Key") String ik,
                          @Valid @RequestBody PaymentDtos.Create r){
        return  paymentservice.create(merchantService.authenticate(key).id,ik,r);
    }


    @GetMapping("/{id}")
    public Payment get(@RequestHeader("X-Api-Key") String key, @PathVariable String id){
        return paymentservice.get(merchantService.authenticate(key).id,id);
    }

    @PostMapping("/{id}/process")
    public Payment process(@RequestHeader("X-Api-Key") String key, @PathVariable String id){
        return paymentservice.process(merchantService.authenticate(key).id,id);
    }

    @PostMapping("/{id}/check")
    public Payment check(@RequestHeader("X-Api-Key") String key, @PathVariable String id){
        return paymentservice.reconcile(merchantService.authenticate(key).id,id);
    }







}
