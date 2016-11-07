package com.rouilleur.emcservices.jobs;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Rouilleur on 31/10/2016.
 */


public class EmcJob {

    final static Logger logger = LoggerFactory.getLogger(EmcJobRepositoryDevImpl.class);
    private static AtomicLong jobCounter = new AtomicLong();

    public enum JobStatus {
        CREATED,
        RUNNING,
        SUCCES,
        FAILED,
        ABORTED;

    }
    private final Long id;

    private JobStatus status;

    private final String submitter;

    @JsonFormat(pattern="dd/MM/yy HH:mm:ss")
    private final Date submitDate;

    @JsonFormat(pattern="dd/MM/yy HH:mm:ss")
    private Date endDate;

    private String description;

    public EmcJob(String submitter, String description){
        this.id = jobCounter.incrementAndGet();
        this.status=JobStatus.CREATED;
        this.submitter = submitter;
        this.description=description;
        this.submitDate =  new Date();
    }

    public void run(){
        logger.info("Starting to run job nb " + id);
        return;
    }

    static public void initJobCounter(long initialValue){
        jobCounter.set(initialValue);
        return;
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
}
