package com.bank.config;

import com.bank.batch.CancelTransactionTasklet;
import com.bank.batch.ProcessPaymentSuccessTasklet;
import com.bank.batch.SendNotificationTasklet;
import com.bank.batch.ValidateAccountTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public ValidateAccountTasklet validateAccountTasklet(){
       return new ValidateAccountTasklet();
    }

    @Bean
    public ProcessPaymentSuccessTasklet processPaymentSuccessTasklet(){
        return new ProcessPaymentSuccessTasklet();
    }

    @Bean
    public SendNotificationTasklet sendNotificationTasklet(){
        return new SendNotificationTasklet();
    }

    @Bean
    public CancelTransactionTasklet cancelTransactionTasklet(){
        return new CancelTransactionTasklet();
    }

    @Bean
    @JobScope //solo va a tener el alcance del job y cuando termine el job vuela
    public Step validateAccountStep(){
        return stepBuilderFactory.get("validateAccountStep")
                .tasklet(validateAccountTasklet())
                .build();
    }

    @Bean
    public Step processPaymentStep(){
        return stepBuilderFactory.get("processPaymentStep")
                .tasklet(processPaymentSuccessTasklet())
                .build();
    }

    @Bean
    public Step cancelTransactionStep(){
        return stepBuilderFactory.get("cancelTransactionStep")
                .tasklet(cancelTransactionTasklet())
                .build();
    }


    @Bean
    public Step sendNotificationStep(){
        return stepBuilderFactory.get("sendNotificationStep")
                .tasklet(sendNotificationTasklet())
                .build();
    }

    @Bean
    public Job transactionPaymentsJob(){
        return jobBuilderFactory.get("transactionPaymentsJob")
                .start(validateAccountStep())//Comienza por aca
                    .on("VALID") // Valida segun el exit status definido
                    .to(processPaymentStep())//ejecuta la decision a la que fue
                .next(sendNotificationStep()) //continua flujo

                .from(validateAccountStep())// pero este es el caso del sino revisa del metodo validate que valor tiene
                    .on("INVALID") // Valida segun el exit status definido
                    .to(cancelTransactionStep())//ejecuta la decision a la que fue
                .next(sendNotificationStep())//continua flujo

                .end()//termina flujo
                .build();

    }


}
