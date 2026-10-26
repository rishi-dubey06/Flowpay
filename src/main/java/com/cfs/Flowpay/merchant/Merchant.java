package com.cfs.Flowpay.merchant;

import com.cfs.Flowpay.common.BaseEntity;
import com.cfs.Flowpay.common.Ids;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "merchant")
public class Merchant extends BaseEntity {

    @Column(nullable = false,length = 100)
    public String name;

    @Column(nullable = false,length = 160)
    public String email;

    @JsonIgnore
    @Column(nullable = false,length = 64)
    public String apiKeyHash;

    @JsonIgnore
    @Column(nullable = false,length = 100)
    public String webhookSecret;

    @Column(length = 500)
    public String webhookUrl;

    @Column(nullable = false)
    public long balance;


}
