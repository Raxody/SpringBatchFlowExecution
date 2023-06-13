package com.bank.batch;

import com.bank.entities.TransferPaymentEntity;
import com.bank.repositories.TransferPaymentRepository;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

public class ValidateAccountTasklet implements Tasklet {

    @Autowired
    private TransferPaymentRepository transferPaymentRepository;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {


        Boolean filterIsApproved = true;

        String transactionId = (String) chunkContext
                .getStepContext()
                .getJobParameters()
                .get("transactionId");

        TransferPaymentEntity transferPayment = transferPaymentRepository.findById(transactionId).orElseThrow();

        if(!transferPayment.getIsEnabled()){
            //Error porque la cuenta esta inactiva
            filterIsApproved = false;
            chunkContext.getStepContext()
                    .getStepExecution()
                    .getJobExecution()
                    .getExecutionContext()
                    .put("errorMessage","Error porque la cuenta esta inactica");

        }
        if(transferPayment.getAmountPaid() > transferPayment.getAvailableBalance()){
            // Saldo insuficiente
            filterIsApproved = false;
            chunkContext.getStepContext()
                    .getStepExecution()
                    .getJobExecution()
                    .getExecutionContext()
                    .put("errorMessage","Error, saldo insuficiente");
        }

        ExitStatus exitStatus = null;
        if(filterIsApproved){
           exitStatus = new ExitStatus("VALID");
        }else {
            exitStatus = new ExitStatus("INVALID");
        }
        stepContribution.setExitStatus(exitStatus);

        return RepeatStatus.FINISHED;
    }
}
