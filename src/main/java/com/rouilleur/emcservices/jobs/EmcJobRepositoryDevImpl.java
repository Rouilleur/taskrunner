package com.rouilleur.emcservices.jobs;


import com.rouilleur.emcservices.Exceptions.InternalErrorException;
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
public class EmcJobRepositoryDevImpl extends EmcJobRepositoryImpl{

    private final static Logger logger = LoggerFactory.getLogger(EmcJobRepositoryDevImpl.class);

    @Override
    public void init(){
        super.init();
        logger.info("Adding data for dev purpose");
        EmcJob.initJobCounter(3);

        try {
            EmcJob emcJob1= new EmcJob("Annie","Playing with Tibers");
            this.save(emcJob1);
            EmcJob emcJob2= new EmcJob("Riven", "Farming top");
            this.save(emcJob2);
        } catch (InternalErrorException e) {
            logger.error("Can't initialize repository");
            e.printStackTrace();
        }

    }
}
