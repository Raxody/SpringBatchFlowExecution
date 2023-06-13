package com.bank.repositories;

import com.bank.entities.TransferPaymentEntity;
import org.springframework.data.repository.CrudRepository;

public interface TransferPaymentRepository extends CrudRepository<TransferPaymentEntity, String> {

}
