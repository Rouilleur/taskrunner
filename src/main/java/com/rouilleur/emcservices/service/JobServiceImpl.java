package com.rouilleur.emcservices.service;

import com.rouilleur.emcservices.exceptions.*;
import com.rouilleur.emcservices.jobs.EmcJob;
import com.rouilleur.emcservices.jobs.repository.EmcJobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by Rouilleur on 07/11/2016.
 */
@Service
public class JobServiceImpl implements JobService {

    private final static Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);

    EmcJobRepository emcJobRepository;


    //TODO : possible inconsistencies between user request and result
    //ex : if the job is modified (stopped, refreshed) after being added to result
    //Could be solved by returning a copy of the objects
    //TODO : improvement : filter by final data (ex : submitter) then refresh jobs then filter by variable data (ex : status)
    @Override
    public Iterable<EmcJob> findAllJobsFiltered(String submitter, String status) throws InternalErrorException, LockedResourceException {
        ArrayList<EmcJob> result = new ArrayList<>();


        for (EmcJob job: emcJobRepository.findAll()) {
            if (!job.isMarkedForDeletion()) {
                //TODO : just for test atm, we could avoid refresh on all jobs for each request

                if (submitter == null && status == null){
                    result.add(job);
                }else if( submitter != null && status == null){
                    if (submitter.equals(job.getSubmitter())) {
                        result.add(job);
                    }
                }else if( submitter == null) {
                    if (status.equals(job.getStatus().toString())) {
                        result.add(job);
                    }
                }else {
                    if (status.equals(job.getStatus().toString()) && submitter.equals(job.getSubmitter())) {
                        result.add(job);
                    }
                }
            }
        }

        return result;
    }

    @Override
    public EmcJob findJobById(Long jobId) throws BadRequestException, InternalErrorException, ResourceNotFoundException, LockedResourceException {
        if (jobId == null){
            throw new BadRequestException(ErrorType.NULL_PARAMETER, "Job Id is null");
        }else{
            //getting the job before testing existence
            //otherwise, it could be deleted between the exists and the get
            EmcJob theJob = emcJobRepository.findOne(jobId);
            if (theJob == null || theJob.isMarkedForDeletion()){
                //TODO : Bof... what if the method is reused and called with null param even the request was correct (exception would be misleading)
                throw new ResourceNotFoundException(ErrorType.RESOURCE_NOT_FOUND, "Can't find job "+ jobId);
            }else {
                return theJob;
            }
        }
    }

    @Override
    public void deleteJob(Long jobId) throws BadRequestException, ResourceNotFoundException, InternalErrorException {
        if (jobId == null){
            throw new BadRequestException(ErrorType.NULL_PARAMETER, "Job Id is null");
        }else{
            //getting the job before testing existence
            //otherwise, it could be deleted between the exists and the get
            EmcJob jobToDelete = emcJobRepository.findOne(jobId);
            if (jobToDelete == null){
                throw new ResourceNotFoundException(ErrorType.RESOURCE_NOT_FOUND, "Can't find job "+ jobId);
            }else {
                jobToDelete.markForDeletion();
            }
        }
    }

    //TODO : what if the job is finished or failed already, should we raise a 409 ?
    @Override
    public void stopJob(Long jobId) throws BadRequestException, ResourceNotFoundException, InternalErrorException, LockedResourceException {
        if (jobId == null){
            throw new BadRequestException(ErrorType.NULL_PARAMETER, "JobId is null");
        }else{
            EmcJob jobToStop = emcJobRepository.findOne(jobId);
            if (jobToStop == null  || jobToStop.isMarkedForDeletion()){
                throw new ResourceNotFoundException(ErrorType.RESOURCE_NOT_FOUND, "Can't find job "+ jobId);
            }else {
                jobToStop.stop(false);
            }
        }
    }

    @Override
    public Iterable<EmcJob> findJobsBySubmitter(String submitter) throws BadRequestException, InternalErrorException, LockedResourceException {
        return findAllJobsFiltered(submitter, null);
    }

    @Override
    public Iterable<EmcJob> findJobsByStatus(String status) throws BadRequestException, InternalErrorException, LockedResourceException {
        return findAllJobsFiltered(null, status);
    }

    @Override
    public void stopAllJobs() throws InternalErrorException, LockedResourceException {
        for (EmcJob job: emcJobRepository.findAll()) {
            if (!job.isMarkedForDeletion()) {
                job.stop(false
                );
            }
        }
    }

    @Override
    public void deleteAllFinishedJobs() throws InternalErrorException {
        for (EmcJob job: emcJobRepository.findAll()) {
            if (!job.isMarkedForDeletion() && (job.getStatus() == EmcJob.JobStatus.ABORTED || job.getStatus() == EmcJob.JobStatus.FAILED ||job.getStatus() == EmcJob.JobStatus.SUCCESS)) {
                job.markForDeletion();
            }
        }
    }

    @Override
    public void lockJob(Long jobId, int delay) throws ResourceNotFoundException, BadRequestException, InternalErrorException {
        if (jobId == null){
            throw new BadRequestException(ErrorType.NULL_PARAMETER, "JobId is null");
        }else{
            EmcJob jobToLock = emcJobRepository.findOne(jobId);
            if (jobToLock == null ){
                throw new ResourceNotFoundException(ErrorType.RESOURCE_NOT_FOUND, "Can't find job "+ jobId);
            }else {
                try {
                    jobToLock.getLock().lock();

                    TimeUnit.SECONDS.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    jobToLock.getLock().unlock();
                }
            }
        }
    }

    @Autowired
    public JobServiceImpl(EmcJobRepository emcJobRepository) {
        this.emcJobRepository = emcJobRepository;
    }
}
