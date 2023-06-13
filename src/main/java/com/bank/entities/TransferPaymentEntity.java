package com.bank.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "transfer_payment_history")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferPaymentEntity {

    @Id
    @Column(name = "transaction_id", nullable = false)
    private String transactionId;
    @Column(name = "available_balance", nullable = false)
    private Double availableBalance;
    @Column(name = "amount_paid", nullable = false)
    private Double amountPaid;
    @Column(nullable = false)
    private Boolean enabled;
    @Column( nullable = false)
    private Boolean processed;
    private String error;
}
