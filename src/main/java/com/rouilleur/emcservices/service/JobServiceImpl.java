package com.rouilleur.emcservices.service;

import com.rouilleur.emcservices.Exceptions.BadRequestException;
import com.rouilleur.emcservices.Exceptions.InternalErrorException;
import com.rouilleur.emcservices.Exceptions.ResourceNotFoundException;
import com.rouilleur.emcservices.jobs.EmcJob;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Created by Rouilleur on 07/11/2016.
 */
@Service
public class JobServiceImpl implements JobService {
    @Override
    public Collection<EmcJob> findAllJobsFiltered(String submitter, String status) {
        return null;
    }

    @Override
    public EmcJob findJobById(Long jobId) throws BadRequestException, InternalErrorException {
        return null;
    }

    @Override
    public void deleteJob(Long jobId) throws BadRequestException, ResourceNotFoundException {

    }

    @Override
    public void stopJob(Long jobId) throws BadRequestException, ResourceNotFoundException {

    }

    @Override
    public Collection<EmcJob> findJobsBySubmitter(String submitter) throws BadRequestException, InternalErrorException {
        return null;
    }
}
