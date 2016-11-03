package com.rouilleur.emcservices.jobs;

import org.springframework.stereotype.Repository;


import java.util.Collection;
import java.util.HashSet;

/**
 * Created by Rouilleur on 31/10/2016.
 */

@Repository
public class EmcJobRepositoryImpl implements EmcJobRepository{


    static Collection<EmcJob> emcJobCollection;

    @Override
    public Collection<EmcJob> findAllJobs() {
        System.out.println("Searching ...");
        return emcJobCollection;
    }

    @Override
    public void save(EmcJob emcJob) {
        System.out.println("Saving ...");
        if (emcJobCollection==null){
            System.out.println("I'm null ...");
            emcJobCollection = new HashSet<EmcJob>();
        }
          emcJobCollection.add(emcJob);
        return;
    }


}
