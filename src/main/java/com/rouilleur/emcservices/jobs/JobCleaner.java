package com.rouilleur.emcservices.jobs;

import com.rouilleur.emcservices.Exceptions.ErrorType;
import com.rouilleur.emcservices.Exceptions.InternalErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by Rouilleur on 11/11/2016.
 */
@Component
@EnableScheduling
public class JobCleaner {
    private final static Logger logger = LoggerFactory.getLogger(JobCleaner.class);


    @Autowired
    EmcJobRepository jobRepository;


    @Scheduled(initialDelayString ="${jobs.cleaning.initialDelay}", fixedDelayString = "${jobs.cleaning.delay}")
    private void runJobCleaning() throws InternalErrorException {
        logger.info("Running job cleaner.");

        try {
            for (EmcJob aJob: jobRepository.findAll()) {
                if (aJob.isMarkedForDeletion()){
                    logger.info("Cleaning job {}", aJob.getId());
                    jobRepository.delete(aJob.getId());
                }
            }
        } catch (InternalErrorException e) {
            if (e.getErrorType() == ErrorType.REPO_NOT_INITIALIZED){
                logger.warn("Repo not initialized, skipping ...");
            }else{
                throw e;
            }
        }


    }


}
