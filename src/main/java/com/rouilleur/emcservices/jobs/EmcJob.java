package com.rouilleur.emcservices.jobs;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rouilleur.emcservices.config.TaskrunnerConfig;
import com.rouilleur.emcservices.exceptions.ErrorType;
import com.rouilleur.emcservices.exceptions.InternalErrorException;
import com.rouilleur.emcservices.exceptions.LockedResourceException;
import com.rouilleur.emcservices.jobs.repository.EmcJobRepositoryDevImpl;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Rouilleur on 31/10/2016.
 */


public class EmcJob {

    private final static Logger logger = LoggerFactory.getLogger(EmcJobRepositoryDevImpl.class);
    private static final AtomicLong jobCounter = new AtomicLong();
    private final Long id;
    private final String submitter;
    @JsonFormat(pattern="dd/MM/yy HH:mm:ss")
    private final Date submitDate;
    private final String description;
    private JobStatus status;
    @JsonFormat(pattern="dd/MM/yy HH:mm:ss")
    private Date endDate;
    //Always assume that a job marked for deletion is currently being deleted
    //Don't try to access persisted information
    @JsonIgnore
    private boolean markedForDeletion = false;
    @JsonIgnore
    private Lock lock;

    public EmcJob(String submitter, String description){
        this.id = jobCounter.incrementAndGet();
        this.status=JobStatus.CREATED;
        this.submitter = submitter;
        this.description=description;
        this.submitDate =  new Date();
        this.lock = new ReentrantLock();
    }

    static public void initJobCounter(long initialValue){
        jobCounter.set(initialValue);
    }

    public void run() throws InternalErrorException {

        try {
            if (lock.tryLock(TaskrunnerConfig.lockTimeout, TimeUnit.SECONDS)){
                try{
                    logger.info("Starting to run job nb {}", id);

                }finally{
                    lock.unlock();
                }
            }else{
                throw new LockedResourceException(ErrorType.RESOURCE_LOCK_TIMEOUT, "Timeout while trying to lock job " + id + " for deletion");
            }

        } catch (InterruptedException e) {
            throw new InternalErrorException(ErrorType.UNDOCUMENTED_INTERNAL, "Error while waiting for resource lock", e, true);
        } catch (LockedResourceException e) {
            e.printStackTrace();
        }

    }

    public boolean stop(boolean canFail) throws InternalErrorException, LockedResourceException {
        try {
            logger.error("lockTimeout : " + TaskrunnerConfig.lockTimeout);
            if (lock.tryLock(TaskrunnerConfig.lockTimeout, TimeUnit.SECONDS)){
                try{
                    logger.info("Got the lock");
                    if ((status == JobStatus.CREATED) || (status == JobStatus.RUNNING)){
                        logger.info("Stopping job {}", id);
                        status = JobStatus.ABORTED;
                        endDate = new Date();
                    }else {
                        logger.warn("Job {} is already Stopped", id);
                    }
                }finally{
                    lock.unlock();
                }
            }else{
                logger.info("don't got it");
                if (canFail){
                    return false;
                }else{
                    throw new LockedResourceException(ErrorType.RESOURCE_LOCK_TIMEOUT, "Timeout while trying to lock job " + id + " for deletion");
                }
            }
        } catch (InterruptedException e) {
            throw new InternalErrorException(ErrorType.UNDOCUMENTED_INTERNAL, "Error while waiting for resource lock", e, true);
        }
        return true;
    }

    /**
     *
     *
     *
     */
    public boolean refresh(boolean canFail) throws LockedResourceException, InternalErrorException {
        lock.tryLock();
        try{


        }finally{
            lock.unlock();
        }

        return true;
    }


    /**
     * Clear persisted job information
     * If the job wasn't stopped, delete will stop it before
     *
     * @return true if the job was correctly deleted
     */
    public boolean delete() throws LockedResourceException, InternalErrorException {
        //since deletion is managed asynchronously, if we can't get the lock, we skip this job
        if (lock.tryLock()){
            try{
                if (stop(true)){
                    logger.info("Deleting {}", id);

                }else{
                    logger.warn("Job {} is currently locked", id);
                    return false;
                }


            }finally{
                lock.unlock();
            }



        }else{
            logger.warn("Job {} is currently locked", id);
            return false;
        }

        return true;

    }

    public void markForDeletion() {
        logger.info("Marking job {} for deletion", id);
        this.markedForDeletion = true;
    }

    public Long getId() {
        return id;
    }

    public Date getSubmitDate() {
        return submitDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public String getSubmitter() {
        return submitter;
    }

    public String getDescription() {
        return description;
    }

    public boolean isMarkedForDeletion() {
        return markedForDeletion;
    }

    //TODO : is it a good idea to expose the lock ?
    //should it be kept internal to run/stop/delete method
    public Lock getLock() {
        return lock;
    }

    public enum JobStatus {
        CREATED,
        RUNNING,
        SUCCESS,
        FAILED,
        ABORTED;
    }
}
