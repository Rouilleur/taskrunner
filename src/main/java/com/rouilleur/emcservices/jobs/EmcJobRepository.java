package com.rouilleur.emcservices.jobs;


import com.rouilleur.emcservices.Exceptions.BadRequestException;
import com.rouilleur.emcservices.Exceptions.InternalErrorException;
import com.rouilleur.emcservices.Exceptions.ResourceNotFoundException;

import java.util.Collection;

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

