package com.rouilleur.emcservices.jobs;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rouilleur.emcservices.config.TaskrunnerConfig;
import com.rouilleur.emcservices.exceptions.ErrorType;
import com.rouilleur.emcservices.exceptions.InternalErrorException;
import com.rouilleur.emcservices.exceptions.LockedResourceException;
import com.rouilleur.emcservices.jobs.repository.EmcJobRepositoryDevImpl;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Rouilleur on 31/10/2016.
 */

//TODO : ugly mix of persistence and business logic...
//refactoring needed
public class EmcJob {

    private final static Logger logger = LoggerFactory.getLogger(EmcJobRepositoryDevImpl.class);
    private Long id;
    private String submitter;
    @JsonFormat(pattern="dd/MM/yyyy HH:mm:ss")
    private Date submitDate;
    private String description;
    private JobStatus status;
    @JsonFormat(pattern="dd/MM/yyyy HH:mm:ss")
    private Date endDate;
    //Always assume that a job marked for deletion is currently being deleted
    //Don't try to access persisted information
    @JsonIgnore
    private boolean markedForDeletion = false;
    @JsonIgnore
    private Lock lock;
    @JsonIgnore
    private String workDirectory;


    public EmcJob(){

    }

    public EmcJob(String submitter, String description){
        this.submitter = submitter;
        this.description = description;
        this.submitDate = new Date();
        this.status = JobStatus.CREATED;

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
                throw new LockedResourceException(ErrorType.RESOURCE_LOCK_TIMEOUT, "Timeout while trying to lock job " + id + " for run");
            }

        } catch (InterruptedException e) {
            throw new InternalErrorException(ErrorType.UNDOCUMENTED_INTERNAL, "Error while waiting for resource lock", e, true);
        } catch (LockedResourceException e) {
            e.printStackTrace();
        }

    }

    public boolean stop(boolean canFail) throws InternalErrorException, LockedResourceException {
        try {
            if (lock.tryLock(TaskrunnerConfig.lockTimeout, TimeUnit.SECONDS)){
                try{
                    performStop();
                }finally{
                    lock.unlock();
                }
            }else{
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



    //TODO : quick and dirty to rework
    private void performStop() throws InternalErrorException {
        if ((status == JobStatus.CREATED) || (status == JobStatus.RUNNING)){
            logger.info("Stopping job {}", id);
            status = JobStatus.ABORTED;
            endDate = new Date();
        }else {
            logger.info("Job {} is already Stopped", id);
        }
    }


    public void markForDeletion() {
        logger.info("Marking job {} for deletion", id);
        this.markedForDeletion = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
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

    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWorkDirectory() {
        return workDirectory;
    }

    public void setWorkDirectory(String workDirectory) {
        this.workDirectory = workDirectory;
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
        ABORTED
    }
}
