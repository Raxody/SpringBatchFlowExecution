package com.bank.repositories;

import com.bank.entities.TransferPaymentEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TransferPaymentRepository extends CrudRepository<TransferPaymentEntity, String> {

    @Modifying
    @Transactional
    @Query("UPDATE TransferPaymentEntity tpe SET tpe.isProcessed = ?1 WHERE tpe.transactionId = ?2")
    void updateTransactionStatusSuccess(Boolean newValue, String transactionId);

    @Modifying
    @Transactional
    @Query("UPDATE TransferPaymentEntity tpe SET tpe.isProcessed = ?1, tpe.error = ?2 WHERE tpe.transactionId = ?3")
    void updateTransactionStatusError(Boolean newValue, String error, String transactionId);


}
