package com.cfs.Flowpay.order;


import com.cfs.Flowpay.merchant.MerchantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService Orderservice;
    private final MerchantService merchantService;


    public OrderController(OrderService orderservice, MerchantService merchantService) {
        Orderservice = orderservice;
        this.merchantService = merchantService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentOrder create(@RequestHeader("X-Api-Key") String key, @Valid @RequestBody OrderDtos.Create r){
        return Orderservice.create(merchantService.authenticate(key).id,r);

    }


    @GetMapping("/{id}")
    public PaymentOrder list(@RequestHeader("X-Api-Key") String key,@PathVariable String id){
        return Orderservice.get(merchantService.authenticate(key).id,id);

    }




}
