package com.rouilleur.emcservices.jobs;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rouilleur.emcservices.jobs.repository.EmcJobRepositoryDevImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Rouilleur on 31/10/2016.
 */


public class EmcJob {

    private final static Logger logger = LoggerFactory.getLogger(EmcJobRepositoryDevImpl.class);
    private static final AtomicLong jobCounter = new AtomicLong();



    public enum JobStatus {
        CREATED,
        RUNNING,
        SUCCESS,
        FAILED,
        ABORTED
    }

    private final Long id;
    private JobStatus status;

    private final String submitter;

    @JsonFormat(pattern="dd/MM/yy HH:mm:ss")
    private final Date submitDate;

    @JsonFormat(pattern="dd/MM/yy HH:mm:ss")
    private Date endDate;

    private final String description;

    //Always assume that a job marked for deletion is currently being deleted
    //Don't try to access persisted information
    @JsonIgnore
    private boolean markedForDeletion = false;

    public EmcJob(String submitter, String description){
        this.id = jobCounter.incrementAndGet();
        this.status=JobStatus.CREATED;
        this.submitter = submitter;
        this.description=description;
        this.submitDate =  new Date();
    }

    static public void initJobCounter(long initialValue){
        jobCounter.set(initialValue);
    }

    public void run(){
        logger.info("Starting to run job nb {}", id);
    }

    public void stop() {
        if ((status == JobStatus.CREATED) || (status == JobStatus.RUNNING)){
            logger.info("Stopping job {}", id);
            status = JobStatus.ABORTED;
            endDate = new Date();
        }else {
            logger.warn("Job {} is already Stopped", id);
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
}
