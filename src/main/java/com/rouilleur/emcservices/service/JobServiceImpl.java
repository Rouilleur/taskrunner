package com.rouilleur.emcservices.service;

import com.rouilleur.emcservices.Exceptions.BadRequestException;
import com.rouilleur.emcservices.Exceptions.ErrorType;
import com.rouilleur.emcservices.Exceptions.InternalErrorException;
import com.rouilleur.emcservices.Exceptions.ResourceNotFoundException;
import com.rouilleur.emcservices.jobs.EmcJob;
import com.rouilleur.emcservices.jobs.EmcJobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Rouilleur on 07/11/2016.
 */
@Service
public class JobServiceImpl implements JobService {

    private final static Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);

    @Autowired
    EmcJobRepository emcJobRepository;

    @Override
    public Iterable<EmcJob> findAllJobsFiltered(String submitter, String status) throws InternalErrorException {
        ArrayList<EmcJob> result = new ArrayList<>();
        if (submitter == null && status == null){
            return emcJobRepository.findAll();
        }else if( submitter != null && status == null){
            for (EmcJob job: emcJobRepository.findAll()) {
                if (submitter.equals(job.getSubmitter())) {
                    result.add(job);
                }
            }
        }else if( submitter == null){
            for (EmcJob job: emcJobRepository.findAll()) {
                if (status.equals(job.getStatus().toString())) {
                    result.add(job);
                }
            }
        }else {
            for (EmcJob job: emcJobRepository.findAll()) {
                if (status.equals(job.getStatus().toString()) && submitter.equals(job.getSubmitter())) {
                    result.add(job);
                }
            }
        }
        return result;
    }

    @Override
    public EmcJob findJobById(Long jobId) throws BadRequestException, InternalErrorException {
        if (emcJobRepository.exists(jobId)){
            return emcJobRepository.findOne(jobId);
        }else {
            //TODO : Bof... what if the method is reused and called with null param even the request was correct (exception would be misleading)
            throw new BadRequestException(ErrorType.NULL_PARAMETER);
        }
    }

    @Override
    public void deleteJob(Long jobId) throws BadRequestException, ResourceNotFoundException, InternalErrorException {
        if (jobId == null){
            throw new BadRequestException(ErrorType.NULL_PARAMETER);
        }else if (!emcJobRepository.exists(jobId)){
            throw new ResourceNotFoundException(ErrorType.RESSOURCE_NOT_FOUND);
        }else {
            logger.info("Stopping job " + jobId);
            //TODO : real stop method
            emcJobRepository.findOne(jobId).setStatus(EmcJob.JobStatus.ABORTED);
            logger.info("Removing job" + jobId);
            emcJobRepository.delete(jobId);
        }
    }

    //TODO : what if the job is finished or failed already, should we raise a 409 ?
    @Override
    public void stopJob(Long jobId) throws BadRequestException, ResourceNotFoundException, InternalErrorException {
        emcJobRepository.findOne(jobId).setStatus(EmcJob.JobStatus.ABORTED);

        if (jobId == null){
            throw new BadRequestException(ErrorType.NULL_PARAMETER);
        }else if (!emcJobRepository.exists(jobId)){
            throw new ResourceNotFoundException(ErrorType.RESSOURCE_NOT_FOUND);
        }else {
            EmcJob jobToDelete = emcJobRepository.findOne(jobId);

            if ( jobToDelete.getStatus().equals(EmcJob.JobStatus.CREATED) || jobToDelete.getStatus().equals(EmcJob.JobStatus.RUNNING)){
                logger.info("Stopping job");
                //TODO : real stop method
                jobToDelete.setStatus(EmcJob.JobStatus.ABORTED);
                jobToDelete.setEndDate(new Date());
            }else {
                logger.warn("Already Stopped");
            }
        }
    }

    @Override
    public Iterable<EmcJob> findJobsBySubmitter(String submitter) throws BadRequestException, InternalErrorException {
        return findAllJobsFiltered(submitter, null);
    }

    @Override
    public Iterable<EmcJob> findJobsByStatus(String status) throws BadRequestException, InternalErrorException {
        return findAllJobsFiltered(null, status);
    }

    @Override
    public void stopAllJobs() {
        //TODO
        //TODO : lock resource
    }
}
