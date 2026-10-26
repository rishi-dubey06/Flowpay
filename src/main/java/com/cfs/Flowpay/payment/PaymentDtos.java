package com.cfs.Flowpay.payment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import javax.swing.plaf.PanelUI;

public class PaymentDtos {

    public enum Method{
        UPI,CARD,WALLET,NETBANKING
    }

    public enum Scenario{
        SUCCESS,
        FAILURE,
        TIMEOUT,
        DELAYED_SUCCESS
    }



    public record Create(@NotBlank  String orderId, @NotNull Method method, @NotNull Scenario scenario){}


}
