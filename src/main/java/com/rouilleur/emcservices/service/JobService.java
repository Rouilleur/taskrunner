package com.rouilleur.emcservices.service;

import com.rouilleur.emcservices.Exceptions.BadRequestException;
import com.rouilleur.emcservices.Exceptions.InternalErrorException;
import com.rouilleur.emcservices.Exceptions.ResourceNotFoundException;
import com.rouilleur.emcservices.jobs.EmcJob;

import java.util.Collection;

/**
 * Created by Rouilleur on 07/11/2016.
 */
public interface JobService {

    Iterable<EmcJob> findAllJobsFiltered(String submitter, String status) throws InternalErrorException;

    EmcJob findJobById(Long jobId) throws BadRequestException, InternalErrorException, ResourceNotFoundException;

    void deleteJob(Long jobId) throws BadRequestException, ResourceNotFoundException, InternalErrorException;

    void stopJob(Long jobId) throws BadRequestException, ResourceNotFoundException, InternalErrorException;

    Iterable<EmcJob> findJobsBySubmitter(String submitter) throws BadRequestException, InternalErrorException;

    Iterable<EmcJob> findJobsByStatus(String status) throws BadRequestException, InternalErrorException;

    void stopAllJobs();
}
