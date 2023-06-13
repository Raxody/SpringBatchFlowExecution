package com.bank.batch;

import com.bank.repositories.TransferPaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class ProcessPaymentSuccessTasklet implements Tasklet {

    @Autowired
    private TransferPaymentRepository transferPaymentRepository;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        String transactionId = (String) chunkContext
                .getStepContext()
                .getJobParameters()
                .get("transactionId");

        log.info("------> Se procesa el pago de la transacción {} exitosamente",transactionId);

        transferPaymentRepository.updateTransactionStatusSuccess(true,transactionId);

        return RepeatStatus.FINISHED;
    }

}
