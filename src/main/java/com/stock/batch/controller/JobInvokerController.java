package com.stock.batch.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobInvokerController {
	
    @Autowired
    JobLauncher jobLauncher;
 
    @Autowired
    Job importStockJob;
    
    @Autowired
    Job extractDataJob;
    
    
    @RequestMapping("/invokejob")
    public String handle() throws Exception {
 
            JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(importStockJob, jobParameters);        
        return "Batch job has been invoked";
    }
    
    @RequestMapping("/extractData")
    public String extactData() throws Exception {
 
            JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(extractDataJob, jobParameters);
 
        return "Extract Data Batch job has been invoked";
    }
    
    

}
