package com.rouilleur.emcservices.service;

import com.rouilleur.emcservices.exceptions.BadRequestException;
import com.rouilleur.emcservices.exceptions.InternalErrorException;
import com.rouilleur.emcservices.exceptions.LockedResourceException;
import com.rouilleur.emcservices.exceptions.ResourceNotFoundException;
import com.rouilleur.emcservices.jobs.EmcJob;

/**
 * Created by Rouilleur on 07/11/2016.
 */
public interface JobService {

    Iterable<EmcJob> findAllJobsFiltered(String submitter, String status) throws InternalErrorException, LockedResourceException;

    EmcJob findJobById(Long jobId) throws BadRequestException, InternalErrorException, ResourceNotFoundException, LockedResourceException;

    void deleteJob(Long jobId) throws BadRequestException, ResourceNotFoundException, InternalErrorException;

    void stopJob(Long jobId) throws BadRequestException, ResourceNotFoundException, InternalErrorException, LockedResourceException;

    Iterable<EmcJob> findJobsBySubmitter(String submitter) throws BadRequestException, InternalErrorException, LockedResourceException;

    Iterable<EmcJob> findJobsByStatus(String status) throws BadRequestException, InternalErrorException, LockedResourceException;

    void stopAllJobs() throws InternalErrorException, LockedResourceException;

    void deleteAllFinishedJobs() throws InternalErrorException;

    void lockJob(Long jobId, int delay) throws ResourceNotFoundException, BadRequestException, InternalErrorException;
}
