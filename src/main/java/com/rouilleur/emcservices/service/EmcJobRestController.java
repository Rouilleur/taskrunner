package com.rouilleur.emcservices.service;

import com.rouilleur.emcservices.jobs.EmcJob;
import com.rouilleur.emcservices.jobs.EmcJobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * Created by Rouilleur on 31/10/2016.
 */


@RestController
@RequestMapping("/emcJobs")
public class EmcJobRestController {
    private final EmcJobRepository emcJobRepository;

    @RequestMapping(method = RequestMethod.GET)
    Collection<EmcJob> findAllJobs(){
        return this.emcJobRepository.findAllJobs();

    }


    @Autowired
    public EmcJobRestController(EmcJobRepository emcJobRepository) {
        this.emcJobRepository = emcJobRepository;
    }



}
