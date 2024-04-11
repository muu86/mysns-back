package com.mj.mysns.config;

import jakarta.annotation.PostConstruct;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "init.db.address", havingValue = "true")
public class InitDbAddressConfig {

    @Autowired
    private JobLauncher jobLauncher;

    // 주소 데이터 셋업
    @Autowired
    @Qualifier("createLegalAddressJob")
    private Job job;

    @PostConstruct
    void batchjob()
        throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobExecution run = jobLauncher.run(job, new JobParameters());
        String jobStatus = run.getExitStatus().toString();
    }

}
