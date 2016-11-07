package com.rouilleur.emcservices.controller;

import com.rouilleur.emcservices.Exceptions.BadRequestException;
import com.rouilleur.emcservices.Exceptions.InternalErrorException;
import com.rouilleur.emcservices.Exceptions.ResourceNotFoundException;
import com.rouilleur.emcservices.jobs.EmcJob;
import com.rouilleur.emcservices.jobs.EmcJobRepository;
import com.rouilleur.emcservices.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Created by Rouilleur on 31/10/2016.
 */


@RestController
@RequestMapping("/emcJobs")
public class EmcJobRestController {
    private final JobService jobService;
    final static Logger logger = LoggerFactory.getLogger(EmcJobRestController.class);


    @ResponseBody
    @RequestMapping(method = RequestMethod.GET,
            produces="application/json")
    Collection<EmcJob> findAllJobsFiltered(@RequestParam(value = "submitter", required = false) String submitter,
                                   @RequestParam(value = "status", required = false) String status){

        return this.jobService.findAllJobsFiltered(submitter,status);
    }


    @ResponseBody
    @RequestMapping(value = "/{jobId}", method = RequestMethod.GET,
            produces="application/json")
    EmcJob findJobById(@PathVariable Long jobId) throws BadRequestException, InternalErrorException {

        return this.jobService.findJobById(jobId);

    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    @RequestMapping(value = "/{jobId}", method = RequestMethod.DELETE,
            produces="application/json")
    void deleteJob(@PathVariable  Long jobId) throws BadRequestException, ResourceNotFoundException {
        jobService.deleteJob(jobId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    @RequestMapping(value = "/{jobId}/stopJob", method = RequestMethod.PUT,
            produces="application/json")
    void stopJob(@PathVariable  Long jobId) throws BadRequestException, ResourceNotFoundException {
        jobService.stopJob(jobId);
    }

    @ResponseBody
    @RequestMapping(value = "/findBySubmitter", method = RequestMethod.GET,
            produces="application/json")
    Collection<EmcJob> findJobsBySubmitter(@RequestParam(value="Submitter", defaultValue="") String submitter) throws BadRequestException, InternalErrorException {

        return this.jobService.findJobsBySubmitter(submitter);
    }


    //TODO : Stop All jobs

    //TODO : Delete All finished jobs (param : older than)



    @Autowired
    public EmcJobRestController(JobService jobService) {
        this.jobService = jobService;
    }

}
