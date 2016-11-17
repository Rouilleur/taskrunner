package com.rouilleur.emcservices.jobs.asychronous;

import com.rouilleur.emcservices.exceptions.ErrorType;
import com.rouilleur.emcservices.exceptions.InternalErrorException;
import com.rouilleur.emcservices.exceptions.LockedResourceException;
import com.rouilleur.emcservices.jobs.EmcJob;
import com.rouilleur.emcservices.jobs.repository.EmcJobRepository;
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

    EmcJobRepository jobRepository;

    //TODO : catch Exceptions from asynchronous runners
    @Scheduled(initialDelayString ="${jobs.cleaning.initialDelay}", fixedDelayString = "${jobs.cleaning.delay}")
    private void runJobCleaning() throws InternalErrorException {
        logger.info("Running job cleaner.");

        try {
            for (EmcJob aJob: jobRepository.findAll()) {
                if (aJob.isMarkedForDeletion()){
                    //TODO : check error/success and log it
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

    @Autowired
    public JobCleaner(EmcJobRepository emcJobRepository) {
        this.jobRepository = emcJobRepository;
    }
}
