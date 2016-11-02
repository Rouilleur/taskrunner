package com.rouilleur.emcservices.jobs;

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
    Collection<EmcJob> readAllJobs(){
        //return this.emcJobRepository.findAll();
        return this.emcJobRepository.findSomething();

    }


    @Autowired
    public EmcJobRestController(EmcJobRepository emcJobRepository) {
        this.emcJobRepository = emcJobRepository;
    }



}
