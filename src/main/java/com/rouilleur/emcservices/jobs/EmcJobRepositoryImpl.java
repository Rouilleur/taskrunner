package com.rouilleur.emcservices.jobs;

import com.rouilleur.emcservices.Exceptions.ErrorType;
import com.rouilleur.emcservices.Exceptions.InternalErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by Rouilleur on 31/10/2016.
 */

@Repository
public class EmcJobRepositoryImpl implements EmcJobRepository {

    private final static Logger logger = LoggerFactory.getLogger(EmcJobRepositoryImpl.class);
    private  Map<Long,EmcJob> emcJobMap;

    @Override
    public void init() {
        logger.info("Initializing jobs repository");

        if(emcJobMap ==null){
            emcJobMap = new ConcurrentHashMap<>();
        }
        logger.info("Repository initialized");
    }


    @Override
    public EmcJob save(EmcJob emcJob) throws InternalErrorException {
        checkRepositoryInitialized();
        emcJobMap.putIfAbsent(emcJob.getId(),emcJob);
        return emcJob;
    }

    @Override
    public Iterable save(Iterable<EmcJob> iterable) throws InternalErrorException {
        checkRepositoryInitialized();
        return null;
    }

    @Override
    public EmcJob findOne(Long jobID) throws InternalErrorException {
        checkRepositoryInitialized();
        return emcJobMap.get(jobID);
    }

    @Override
    public boolean exists(Long jobID) throws InternalErrorException {
        checkRepositoryInitialized();
        return emcJobMap.containsKey(jobID);
    }

    @Override
    public Iterable<EmcJob> findAll() throws InternalErrorException {
        checkRepositoryInitialized();
        return emcJobMap.values();
    }

//    @Override
//    public Iterable<EmcJob> findAll(Iterable<Long> iterable) throws InternalErrorException {
//        checkRepositoryInitialized();
//        return null;
//    }

    @Override
    public long count() throws InternalErrorException {
        checkRepositoryInitialized();
        return emcJobMap.size();
    }

    @Override
    public void delete(Long jobId) throws InternalErrorException {
        checkRepositoryInitialized();
        emcJobMap.remove(jobId);
    }

//    @Override
//    public void delete(EmcJob emcJob) throws InternalErrorException {
//        checkRepositoryInitialized();
//    }
//
//    @Override
//    public void delete(Iterable<? extends EmcJob> iterable) throws InternalErrorException {
//        checkRepositoryInitialized();
//    }

    @Override
    public void deleteAll() {
        emcJobMap.clear();
    }

    private void checkRepositoryInitialized() throws InternalErrorException {
        if (emcJobMap ==null){
            logger.error("Trying to access an uninitialized repository!");
            throw new InternalErrorException(ErrorType.REPO_NOT_INITIALIZED);
        }
    }

}

