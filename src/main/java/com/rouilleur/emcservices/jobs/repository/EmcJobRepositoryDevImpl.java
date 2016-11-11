package com.rouilleur.emcservices.jobs.repository;


import com.rouilleur.emcservices.exceptions.InternalErrorException;
import com.rouilleur.emcservices.jobs.EmcJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;


/**
 * Created by Rouilleur on 31/10/2016.
 */

//TODO : choose between @Profile and @Configuration class
@Profile("Tests")
@Primary
@Repository
public class EmcJobRepositoryDevImpl extends EmcJobRepositoryImpl {

    private final static Logger logger = LoggerFactory.getLogger(EmcJobRepositoryDevImpl.class);

    @Override
    public void init(){
        super.init();
        logger.info("Adding data for dev purpose");
        EmcJob.initJobCounter(3);

        try {
            EmcJob emcJob= new EmcJob("Annie","Playing with Tibers");
            this.save(emcJob);
            emcJob= new EmcJob("Warwick", "Jungling");
            this.save(emcJob);
            emcJob= new EmcJob("Soraka", "Healing");
            this.save(emcJob);
            emcJob= new EmcJob("Kassadin", "Rifting");
            this.save(emcJob);
            emcJob= new EmcJob("Teemo", "Trippin' on shrooms");
            this.save(emcJob);
            emcJob= new EmcJob("Urf", "Joking");
            this.save(emcJob);
            emcJob= new EmcJob("Soraka", "Throwing bananas");
            this.save(emcJob);
            emcJob= new EmcJob("Teemo", "Feeding");
            this.save(emcJob);
            emcJob= new EmcJob("Teemo", "Hiding");
            this.save(emcJob);

        } catch (InternalErrorException e) {
            logger.error("Can't initialize repository");
            e.printStackTrace();
        }

    }
}
