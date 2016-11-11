package com.rouilleur.emcservices.jobs.repository;


import com.rouilleur.emcservices.exceptions.InternalErrorException;
import com.rouilleur.emcservices.jobs.EmcJob;

/**
 * Created by Rouilleur on 31/10/2016.
 */


public interface EmcJobRepository {

    EmcJob save(EmcJob emcJob) throws InternalErrorException;

    Iterable save(Iterable<EmcJob> iterable) throws InternalErrorException;

    EmcJob findOne(Long aLong) throws InternalErrorException;

    boolean exists(Long aLong) throws InternalErrorException;

    Iterable<EmcJob> findAll() throws InternalErrorException;

    //Iterable<EmcJob> findAll(Iterable<Long> iterable);

    long count() throws InternalErrorException;

    void delete(Long jobId) throws InternalErrorException;

    //public void delete(EmcJob emcJob);

    //public void delete(Iterable<? extends EmcJob> iterable);

    void deleteAll();

    void init();
}

