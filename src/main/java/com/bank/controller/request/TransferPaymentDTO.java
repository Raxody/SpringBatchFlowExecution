package com.bank.controller.request;

import lombok.Data;

@Data
public class TransferPaymentDTO {

    private Double availableBalance;
    private Boolean isEnabled;
    private Double amountPaid;

}
