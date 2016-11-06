package com.rouilleur.emcservices.jobs;


import com.rouilleur.emcservices.Exceptions.BadRequestException;
import com.rouilleur.emcservices.Exceptions.InternalErrorException;

import java.util.Collection;
import java.util.Map;

/**
 * Created by Rouilleur on 31/10/2016.
 */


public interface EmcJobRepository {

    void init();

    EmcJob findJobById(Long id);

    Collection<EmcJob> findAllJobs();

    Collection<EmcJob> findJobsBySubmitter(String submitter) throws BadRequestException, InternalErrorException;

    Collection<EmcJob> findJobsByStatus(String status);

    void save(EmcJob emcJob) throws InternalErrorException;

}

