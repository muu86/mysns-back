package com.mj.mysns.batch;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/batch")
public class BatchController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired @Qualifier("createAllJob")
    private Job createAllJob;

    @Autowired @Qualifier("createLegalAddressJob")
    private Job createLegalAddressJob;

    @GetMapping(path = "/all")
    public String batchAll()
        throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobExecution run = jobLauncher.run(createAllJob, new JobParameters());
        return run.getExitStatus().toString();
    }

    @GetMapping(path = "/address")
    public String batchAddress()
        throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobExecution run = jobLauncher.run(createLegalAddressJob, new JobParameters());
        return run.getExitStatus().toString();
    }
}
