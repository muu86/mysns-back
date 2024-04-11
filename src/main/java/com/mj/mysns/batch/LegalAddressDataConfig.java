package com.mj.mysns.batch;

import com.mj.mysns.batch.address.LegalAddressProcessor;
import com.mj.mysns.batch.address.LegalAddressReader;
import com.mj.mysns.batch.address.LegalAddressWriter;
import com.mj.mysns.location.entity.Address;
import com.mj.mysns.location.repository.AddressRepository;
import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class LegalAddressDataConfig {


    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final AddressRepository addressRepository;

    @Bean
    public ItemReader<Address> legalAddressItemReader() {
        return new LegalAddressReader(Path.of("/Users/mj/work/sns/data", "seoul_emd_geometry.csv"));
    }

    @Bean
    public ItemProcessor<Address, Address> legalAddressItemProcessor() {
        return new LegalAddressProcessor();
    }

    @Bean
    public ItemWriter<Address> legalAddressItemWriter() {
        return new LegalAddressWriter(addressRepository);
    }

    @Bean
    public Step createLegalAddressStep() {
        return new StepBuilder("createLegalAddressDataStep", jobRepository)
            .<Address, Address>chunk(100, platformTransactionManager)
            .reader(legalAddressItemReader())
            .processor(legalAddressItemProcessor())
            .writer(legalAddressItemWriter())
            .allowStartIfComplete(true)
            .build();
    }

    @Bean
    public Job createLegalAddressJob() {
        return new JobBuilder("createLegalAddressJob", jobRepository)
            .start(createLegalAddressStep())
            .build();
    }
}
