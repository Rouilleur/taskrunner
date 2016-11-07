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

    Collection<EmcJob> findAllJobsFiltered(String submitter, String status);

    EmcJob findJobById(Long jobId) throws BadRequestException, InternalErrorException;

    void deleteJob(Long jobId) throws BadRequestException, ResourceNotFoundException;

    void stopJob(Long jobId) throws BadRequestException, ResourceNotFoundException;

    Collection<EmcJob> findJobsBySubmitter(String submitter) throws BadRequestException, InternalErrorException;

}