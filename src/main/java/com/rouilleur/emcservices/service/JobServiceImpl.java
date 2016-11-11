package com.rouilleur.emcservices.service;

import com.rouilleur.emcservices.exceptions.BadRequestException;
import com.rouilleur.emcservices.exceptions.ErrorType;
import com.rouilleur.emcservices.exceptions.InternalErrorException;
import com.rouilleur.emcservices.exceptions.ResourceNotFoundException;
import com.rouilleur.emcservices.jobs.EmcJob;
import com.rouilleur.emcservices.jobs.repository.EmcJobRepository;
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

    EmcJobRepository emcJobRepository;

    @Override
    public Iterable<EmcJob> findAllJobsFiltered(String submitter, String status) throws InternalErrorException {
        ArrayList<EmcJob> result = new ArrayList<>();
        if (submitter == null && status == null){
            for (EmcJob job: emcJobRepository.findAll()) {
                if (!job.isMarkedForDeletion()) {
                    result.add(job);
                }
            }
        }else if( submitter != null && status == null){
            for (EmcJob job: emcJobRepository.findAll()) {
                if (!job.isMarkedForDeletion() && submitter.equals(job.getSubmitter())) {
                    result.add(job);
                }
            }
        }else if( submitter == null){
            for (EmcJob job: emcJobRepository.findAll()) {
                if (!job.isMarkedForDeletion() && status.equals(job.getStatus().toString())) {
                    result.add(job);
                }
            }
        }else {
            for (EmcJob job: emcJobRepository.findAll()) {
                if (!job.isMarkedForDeletion() && status.equals(job.getStatus().toString()) && submitter.equals(job.getSubmitter())) {
                    result.add(job);
                }
            }
        }

        return result;
    }

    @Override
    public EmcJob findJobById(Long jobId) throws BadRequestException, InternalErrorException, ResourceNotFoundException {
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
    public void stopJob(Long jobId) throws BadRequestException, ResourceNotFoundException, InternalErrorException {
        if (jobId == null){
            throw new BadRequestException(ErrorType.NULL_PARAMETER, "JobId is null");
        }else{
            EmcJob jobToStop = emcJobRepository.findOne(jobId);
            if (jobToStop == null  || jobToStop.isMarkedForDeletion()){
                //TODO : job may be deleted after test
                throw new ResourceNotFoundException(ErrorType.RESOURCE_NOT_FOUND, "Can't find job "+ jobId);
            }else {
                jobToStop.stop();
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
    public void stopAllJobs() throws InternalErrorException {
        for (EmcJob job: emcJobRepository.findAll()) {
            if (!job.isMarkedForDeletion()) {
                job.stop();
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

    @Autowired
    public JobServiceImpl(EmcJobRepository emcJobRepository) {
        this.emcJobRepository = emcJobRepository;
    }
}
