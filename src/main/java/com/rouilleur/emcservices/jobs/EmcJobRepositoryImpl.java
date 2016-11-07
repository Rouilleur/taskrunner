package com.rouilleur.emcservices.jobs;

import com.rouilleur.emcservices.Exceptions.BadRequestException;
import com.rouilleur.emcservices.Exceptions.ErrorType;
import com.rouilleur.emcservices.Exceptions.InternalErrorException;
import com.rouilleur.emcservices.Exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static javafx.scene.input.KeyCode.T;

/**
 * Created by Rouilleur on 31/10/2016.
 */

@Repository
public class EmcJobRepositoryImpl implements EmcJobRepository {

    final static Logger logger = LoggerFactory.getLogger(EmcJobRepositoryImpl.class);
    static Map<Long,EmcJob> emcJobMap;

    @Override
    public void init() {
        logger.info("Initializing jobs repository");

        if(emcJobMap ==null){
            emcJobMap = new Hashtable<Long,EmcJob>();
        }
        logger.info("Repository initialized");
    }

    @Override
    public EmcJob findJobById(Long id) throws BadRequestException {
        if (emcJobMap != null){
            return emcJobMap.get(id);
        }else {
            //TODO : Bof... what if the method is reused and called with null param even the request was correct (exception would be misleading)
            throw new BadRequestException(ErrorType.NULL_PARAMETER);
        }
    }


    @Override
    public Collection<EmcJob> findAllJobs() {
        return emcJobMap.values();
    }

    @Override
    public Collection<EmcJob> findAllJobsFiltered(String submitter, String status) {
        ArrayList<EmcJob> result = new ArrayList<>();
        if (submitter == null && status == null){
            return emcJobMap.values();
        }else if( submitter != null && status == null){
            for (EmcJob job: emcJobMap.values()) {
                if (submitter.equals(job.getSubmitter())) {
                    result.add(job);
                }
            }
        }else if( submitter == null && status != null){
            for (EmcJob job: emcJobMap.values()) {
                if (status.equals(job.getStatus().toString())) {
                    result.add(job);
                }
            }
        }else {
            for (EmcJob job: emcJobMap.values()) {
                if (status.equals(job.getStatus().toString()) && submitter.equals(job.getSubmitter())) {
                    result.add(job);
                }
            }
        }
        return result;
    }

    @Override
    public Collection<EmcJob> findJobsBySubmitter(String submitter) throws BadRequestException, InternalErrorException {

        ArrayList<EmcJob> result = new ArrayList<>();
        if (submitter==null){
            logger.warn("Can't find jobs for null submitter");
            throw new BadRequestException(ErrorType.NULL_PARAMETER);
        }else if (emcJobMap == null) {
            logger.error("Trying to get job from an uninitialized repository");
            throw new InternalErrorException(ErrorType.REPO_NOT_INITIALIZED);
        }else {
            for (EmcJob job: emcJobMap.values()) {
                if (submitter.equals(job.getSubmitter())) {
                    result.add(job);
                }
            }
        }
        return result;
    }

    @Override
    public Collection<EmcJob> findJobsByStatus(String status) {
        return null;
    }

    @Override
    public void save(EmcJob emcJob) throws InternalErrorException {
        if (emcJobMap ==null){
            logger.warn("Trying to save a job in an uninitialized repository! Initializing first...");
            throw new InternalErrorException(ErrorType.REPO_NOT_INITIALIZED);
        }
        emcJobMap.put(emcJob.getId(),emcJob);
        return;
    }

    //TODO : what if the job is finished or failed already, should we raise a 409 ?
    @Override
    public void stopJob(Long jobId) throws ResourceNotFoundException, BadRequestException {
        if (jobId == null){
            throw new BadRequestException(ErrorType.NULL_PARAMETER);
        }else if (emcJobMap.containsKey(jobId) == false){
            throw new ResourceNotFoundException(ErrorType.RESSOURCE_NOT_FOUND);
        }else if ( emcJobMap.get(jobId).getStatus().equals(EmcJob.JobStatus.CREATED) || emcJobMap.get(jobId).getStatus().equals(EmcJob.JobStatus.RUNNING)){
                logger.info("Stopping job");
                //TODO : real stop method
                emcJobMap.get(jobId).setStatus(EmcJob.JobStatus.ABORTED);
        }else {
            logger.warn("Already Stopped");
        }
    }

    @Override
    public void deleteJob(Long jobId) throws ResourceNotFoundException, BadRequestException {
        logger.info("Gonna sleep");
        if (jobId == null){
            throw new BadRequestException(ErrorType.NULL_PARAMETER);
        }else if (emcJobMap.containsKey(jobId) == false){
            throw new ResourceNotFoundException(ErrorType.RESSOURCE_NOT_FOUND);
        }else {
            logger.info("Stopping job " + jobId);
            //TODO : real stop method
            emcJobMap.get(jobId).setStatus(EmcJob.JobStatus.ABORTED);
            logger.info("Removing job" + jobId);
            emcJobMap.remove(jobId);
        }
    }


    public EmcJob findOne(Long aLong) {
        return null;
    }

    public boolean exists(Long aLong) {
        return false;
    }

    public Iterable<EmcJob> findAll() {
        return null;
    }

    public Iterable<EmcJob> findAll(Iterable<Long> iterable) {
        return null;
    }

    public long count() {
        return 0;
    }

    public void delete(Long aLong) {

    }

    public void delete(EmcJob emcJob) {

    }

    public void delete(Iterable<? extends EmcJob> iterable) {

    }

    public void deleteAll() {

    }
}

