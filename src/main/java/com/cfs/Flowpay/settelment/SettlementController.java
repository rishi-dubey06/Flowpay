package com.cfs.Flowpay.settelment;

import com.cfs.Flowpay.merchant.MerchantService;
import com.cfs.Flowpay.refund.Refund;
import com.cfs.Flowpay.refund.RefundDtos;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/settlements")
public class SettlementController {

    private final SettlementService service;
    private final MerchantService merchants;

    public SettlementController(SettlementService service, MerchantService merchants) {
        this.service = service;
        this.merchants = merchants;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Settlement create(@RequestHeader("X-Api-Key") String key,
                         @RequestHeader ("Idempotency-Key") String ik){
        return  service.create(merchants.authenticate(key).id,ik);
    }


    @GetMapping
    public List<Settlement> list(@RequestHeader("X-Api-Key") String key){
        return service.list(merchants.authenticate(key).id);
    }
}
