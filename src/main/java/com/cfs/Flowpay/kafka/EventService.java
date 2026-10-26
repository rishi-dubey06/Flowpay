package com.cfs.Flowpay.kafka;


import com.cfs.Flowpay.common.Ids;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@Service
public class EventService
{
    private final EventRepository repo;
    private final ObjectMapper json;


    public EventService(EventRepository repo, ObjectMapper json) {
        this.repo = repo;
        this.json = json;
    }

    public void create(String merchantId,String type,String referenceId, long amount){

        Event e=new Event();
        e.id= Ids.next("evt");
        e.merchantId=merchantId;
        e.type=type;
        try{
            e.payload= json.writeValueAsString(Map.of("id",e.id,
                    "type",type,
                    "merchantId",merchantId,
                    "referenceId",referenceId,
                    "amount",amount,
                    "currency",
                    "INR",
                    "createdAt",
                    e.createdAt.toString()));
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
        repo.save(e);
    }
}
