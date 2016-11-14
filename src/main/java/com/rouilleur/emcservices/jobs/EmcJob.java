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
import java.util.HashSet;
import java.util.Map;
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
    private static final AtomicLong jobCounter = new AtomicLong();
    private final Long id;
    private final String submitter;
    @JsonFormat(pattern="dd/MM/yyyy HH:mm:ss")
    private final Date submitDate;
    private final String description;
    private JobStatus status;
    @JsonFormat(pattern="dd/MM/yyyy HH:mm:ss")
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

    //TODO : quick and dirty to rework
    //TODO : catch exceptions (ex : directory already exists) + check success
    //TODO : reuse the mapper
    public void create(){
        logger.info("Creating persistence structure for job {}", id);
        File jobInfoDir = new File(TaskrunnerConfig.jobRepositoryBasePath +"/"+ id + "/job_info");
        File jobInputDir = new File(TaskrunnerConfig.jobRepositoryBasePath +"/"+ id + "/input");
        File jobWorkDir = new File(TaskrunnerConfig.jobRepositoryBasePath +"/"+ id + "/work");
        File logDir = new File(TaskrunnerConfig.jobRepositoryBasePath +"/"+ id + "/logs");
        File outputDir = new File(TaskrunnerConfig.jobRepositoryBasePath +"/"+ id + "/output");

        jobInfoDir.mkdirs();
        jobInputDir.mkdirs();
        jobWorkDir.mkdirs();
        logDir.mkdirs();
        outputDir.mkdirs();

        performPersist();
    }



    //TODO : quick and dirty to rework
    //TODO : catch exceptions + check success
    //TODO : reuse the mapper
    public void performPersist(){
        logger.info("Saving job {}", id);
        ObjectMapper mapper = new ObjectMapper();

        try {
            mapper.writeValue(new File(TaskrunnerConfig.jobRepositoryBasePath +"/"+ id + "/job_info/job_"+ id + ".json"), this);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        performRefresh();
        if ((status == JobStatus.CREATED) || (status == JobStatus.RUNNING)){
            logger.info("Stopping job {}", id);
            status = JobStatus.ABORTED;
            endDate = new Date();
            performPersist();
        }else {
            logger.info("Job {} is already Stopped", id);
        }
    }

    //TODO : quick and dirty to rework
    public boolean refresh(boolean canFail) throws LockedResourceException, InternalErrorException {
        try {
            if (lock.tryLock(TaskrunnerConfig.lockTimeout, TimeUnit.SECONDS)){
                try{
                    performRefresh();
                }finally{
                    lock.unlock();
                }
            }else{
                if (canFail){
                    return false;
                }else{
                    throw new LockedResourceException(ErrorType.RESOURCE_LOCK_TIMEOUT, "Timeout while trying to lock job " + id + " for refresh");
                }
            }
        } catch (InterruptedException e) {
            throw new InternalErrorException(ErrorType.UNDOCUMENTED_INTERNAL, "Error while waiting for resource lock", e, true);
        }
        return true;
    }

    private void performRefresh() throws InternalErrorException {
        logger.info("refreshing job {}", id);

        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Object> resultMap;
        String inputJson = TaskrunnerConfig.jobRepositoryBasePath + "/" + id + "/job_info/job_" + id + ".json";

        try {
            File jsonInput = new File(inputJson);
            TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {};
            resultMap = mapper.readValue(jsonInput, typeRef);
            //TODO : refresh other attributes

        } catch (IOException e) {
           throw new InternalErrorException(ErrorType.REFRESH_ERROR, "Failed to parse json file for job "+ id , e, true);
        }

        if (resultMap.get("status") != null){
            this.status = JobStatus.valueOf((String) resultMap.get("status"));
        }else{
            //TODO : don't know exactly what...
        }

        if (resultMap.get("endDate") != null) {
            try {
                SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                this.endDate = parser.parse((String) resultMap.get("endDate"));
            } catch (ParseException e) {
                throw new InternalErrorException(ErrorType.REFRESH_ERROR, "Failed to parse endDate for job "+ id , e, true);
            }
        }
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
                    performDelete();
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

    //TODO : quick and dirty to rework
    private void performDelete() throws InternalErrorException {
        File directoryToDelete = new File (TaskrunnerConfig.jobRepositoryBasePath + "/" + id);
        try {
            FileUtils.deleteDirectory(directoryToDelete);
        } catch (IOException e) {
            throw new InternalErrorException(ErrorType.REFRESH_ERROR, "Failed to delete persisted info for job "+ id , e, true);
            //TODO handle exception
        }
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
        ABORTED
    }
}
