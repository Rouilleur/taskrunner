package com.rouilleur.emcservices.controller;

import com.rouilleur.emcservices.Exceptions.BadRequestException;
import com.rouilleur.emcservices.Exceptions.InternalErrorException;
import com.rouilleur.emcservices.jobs.EmcJob;
import com.rouilleur.emcservices.jobs.EmcJobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Created by Rouilleur on 31/10/2016.
 */


@RestController
@RequestMapping("/emcJobs")
public class EmcJobRestController {
    private final EmcJobRepository emcJobRepository;
    final static Logger logger = LoggerFactory.getLogger(EmcJobRestController.class);

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET
            //, produces={"application/xml", "application/json"}
            )
    Collection<EmcJob> findAllJobs(){

        return this.emcJobRepository.findAllJobs();

    }

    @ResponseBody
    @RequestMapping(value = "/{jobId}", method = RequestMethod.GET)
    EmcJob findJobById(@PathVariable Long jobId) throws BadRequestException, InternalErrorException {

        return this.emcJobRepository.findJobById(jobId);

    }

    @ResponseBody
    @RequestMapping(value = "/findBySubmitter", method = RequestMethod.GET)
    Collection<EmcJob> findJobBySubmitter(@RequestParam(value="Submitter", defaultValue="") String submitterId) throws BadRequestException, InternalErrorException {

        return this.emcJobRepository.findJobsBySubmitter(submitterId);

    }


    @Autowired
    public EmcJobRestController(EmcJobRepository emcJobRepository) {
        this.emcJobRepository = emcJobRepository;
    }

}
