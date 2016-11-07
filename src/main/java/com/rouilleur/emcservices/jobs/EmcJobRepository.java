package com.rouilleur.emcservices.jobs;


import com.rouilleur.emcservices.Exceptions.BadRequestException;
import com.rouilleur.emcservices.Exceptions.InternalErrorException;
import com.rouilleur.emcservices.Exceptions.ResourceNotFoundException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Created by Rouilleur on 31/10/2016.
 */


public interface EmcJobRepository {

    void init();

    EmcJob findJobById(Long id) throws BadRequestException;

    Collection<EmcJob> findAllJobs();

    Collection<EmcJob> findAllJobsFiltered(String submitter, String status);

    Collection<EmcJob> findJobsBySubmitter(String submitter) throws BadRequestException, InternalErrorException;

    Collection<EmcJob> findJobsByStatus(String status);

    void save(EmcJob emcJob) throws InternalErrorException;

    void stopJob(Long jobId) throws ResourceNotFoundException, BadRequestException;

    void deleteJob(Long jobId) throws ResourceNotFoundException, BadRequestException;


    public EmcJob findOne(Long aLong);

    public boolean exists(Long aLong);

    public Iterable<EmcJob> findAll();

    public Iterable<EmcJob> findAll(Iterable<Long> iterable);

    public long count();

    public void delete(Long aLong);

    //public void delete(EmcJob emcJob);

    //public void delete(Iterable<? extends EmcJob> iterable);

    public void deleteAll();
}

