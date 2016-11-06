package com.rouilleur.emcservices.jobs;

import com.rouilleur.emcservices.Exceptions.BadRequestException;
import com.rouilleur.emcservices.Exceptions.ErrorType;
import com.rouilleur.emcservices.Exceptions.InternalErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Rouilleur on 31/10/2016.
 */

@Repository
public class EmcJobRepositoryImpl implements EmcJobRepository {

    final static Logger logger = LoggerFactory.getLogger(EmcJobRepositoryImpl.class);
    static Map<Long,EmcJob> emcJobMap;

    @Override
    public void init() {
        logger.info("Initializing jobs repository");

        if(emcJobMap ==null){
            emcJobMap = new Hashtable<Long,EmcJob>();
        }
        logger.info("Repository initialized");
    }

    @Override
    public EmcJob findJobById(Long id) {
        if (emcJobMap != null){
            return emcJobMap.get(id);
        }else {
            logger.error("Trying to get job from an uninitialized repository");
            return null;
        }
    }


    @Override
    public Collection<EmcJob> findAllJobs() {
        return emcJobMap.values();
    }

    //TODO : Throw exceptions and return error message in abnormal situations instead of returning empty array
    @Override
    public Collection<EmcJob> findJobsBySubmitter(String submitter) throws BadRequestException, InternalErrorException {

        ArrayList<EmcJob> result = new ArrayList<>();
        if (submitter==null){
            logger.warn("Can't find jobs for null submitter");
            throw new BadRequestException(ErrorType.NULL_PARAMETER);
        }else if (emcJobMap == null) {
            logger.error("Trying to get job from an uninitialized repository");
            throw new InternalErrorException(ErrorType.REPO_NOT_INITIALIZED);
        }else {
            for (EmcJob job: emcJobMap.values()) {
                if (submitter.equals(job.getSubmitter())) {
                    result.add(job);
                }
            }
        }
        return result;
    }

    @Override
    public Collection<EmcJob> findJobsByStatus(String status) {
        return null;
    }

    @Override
    public void save(EmcJob emcJob) throws InternalErrorException {
        if (emcJobMap ==null){
            logger.warn("Trying to save a job in an uninitialized repository! Initializing first...");
            throw new InternalErrorException(ErrorType.REPO_NOT_INITIALIZED);
        }
        emcJobMap.put(emcJob.getId(),emcJob);
        return;
    }


}
