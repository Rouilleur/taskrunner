package com.rouilleur.emcservices.jobs;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

/**
 * Created by Rouilleur on 31/10/2016.
 */


public interface EmcJobRepository {
    Collection<EmcJob> findSomething();

    void save(EmcJob emcJob);

}


//public interface EmcJobRepository extends JpaRepository<EmcJob, Long >{
//    Collection<EmcJob> findBySubmiter(String Submiter);
//}
