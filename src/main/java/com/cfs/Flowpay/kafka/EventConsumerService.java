package com.cfs.Flowpay.kafka;

import com.cfs.Flowpay.common.Ids;
import com.cfs.Flowpay.merchant.Merchant;
import com.cfs.Flowpay.merchant.MerchantRepository;
import com.cfs.Flowpay.webhook.Webhook;
import com.cfs.Flowpay.webhook.*;
//import com.cfs.Flowpay.webhook.WebhookService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class EventConsumerService {


    private final EventRepository events;
    private final WebhookRepository webhooks;
    private final MerchantRepository merchants;


    public EventConsumerService(EventRepository repo, WebhookRepository webhooks, MerchantRepository merchants) {
        this.events=repo;
        this.webhooks = webhooks;
        this.merchants = merchants;
    }

    @Transactional
    public void consume(String id){
        Event e =events.lockById(id).orElseThrow();
        if(e.consumedAtl!=null)return;;
        Merchant m=merchants.findById(e.merchantId).orElseThrow();
        Webhook w=new Webhook();
        w.id= Ids.next("wh");
        w.eventId=e.id;
        w.merchantId=m.id;
        w.url=m.webhookUrl;
        if(w.url==null || w.url.isBlank()){
            w.status="SKIPPED";
            w.lastError="No Webhook URL configured";
        }
        webhooks.save(w);
        e.consumedAtl= Instant.now();

    }


}
