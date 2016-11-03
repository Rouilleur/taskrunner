package com.rouilleur.emcservices.jobs;


import java.util.Collection;

/**
 * Created by Rouilleur on 31/10/2016.
 */


public interface EmcJobRepository {
    Collection<EmcJob> findAllJobs();

    void save(EmcJob emcJob);

}

