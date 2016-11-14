package com.rouilleur.emcservices.controller;

import com.rouilleur.emcservices.exceptions.BadRequestException;
import com.rouilleur.emcservices.exceptions.InternalErrorException;
import com.rouilleur.emcservices.exceptions.LockedResourceException;
import com.rouilleur.emcservices.exceptions.ResourceNotFoundException;
import com.rouilleur.emcservices.jobs.EmcJob;
import com.rouilleur.emcservices.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    Iterable<EmcJob> findAllJobsFiltered(@RequestParam(value = "submitter", required = false) String submitter,
                                   @RequestParam(value = "status", required = false) String status) throws InternalErrorException, LockedResourceException {

        return this.jobService.findAllJobsFiltered(submitter,status);
    }


    @ResponseBody
    @RequestMapping(value = "/{jobId}", method = RequestMethod.GET,
            produces="application/json")
    EmcJob findJobById(@PathVariable Long jobId) throws BadRequestException, InternalErrorException, ResourceNotFoundException, LockedResourceException {

        return this.jobService.findJobById(jobId);

    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    @RequestMapping(value = "/{jobId}", method = RequestMethod.DELETE,
            produces="application/json")
    void deleteJob(@PathVariable  Long jobId) throws BadRequestException, ResourceNotFoundException, InternalErrorException {
        jobService.deleteJob(jobId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    @RequestMapping(value = "/{jobId}/stopJob", method = RequestMethod.PUT,
            produces="application/json")
    void stopJob(@PathVariable  Long jobId) throws BadRequestException, ResourceNotFoundException, InternalErrorException, LockedResourceException {
        jobService.stopJob(jobId);
    }

    @ResponseBody
    @RequestMapping(value = "/findBySubmitter", method = RequestMethod.GET,
            produces="application/json")
    Iterable<EmcJob> findJobsBySubmitter(@RequestParam(value="Submitter", defaultValue="") String submitter) throws BadRequestException, InternalErrorException, LockedResourceException {

        return this.jobService.findJobsBySubmitter(submitter);
    }

    @ResponseBody
    @RequestMapping(value = "/findByStatus", method = RequestMethod.GET,
            produces="application/json")
    Iterable<EmcJob> findJobsByStatus(@RequestParam(value="Status", defaultValue="") String status) throws BadRequestException, InternalErrorException, LockedResourceException {

        return this.jobService.findJobsByStatus(status);
    }

    @ResponseBody
    @RequestMapping(value = "/stopAllJobs", method = RequestMethod.PUT,
            produces="application/json")
    void stopAllJobs() throws InternalErrorException, LockedResourceException {

         this.jobService.stopAllJobs();
    }


    //TODO : Delete All finished jobs (param : older than)
    @ResponseBody
    @RequestMapping(value = "/deleteAllFinishedJobs", method = RequestMethod.DELETE,
            produces="application/json")
    void deleteAllFinishedJobs() throws InternalErrorException {
        this.jobService.deleteAllFinishedJobs();
    }


    //TODO : remove after tests
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    @RequestMapping(value = "/{jobId}/lockJob", method = RequestMethod.PUT,
            produces="application/json")
    void lockJob(@PathVariable  Long jobId, @RequestParam(value="Delay", defaultValue="5") int delay) throws BadRequestException, ResourceNotFoundException, InternalErrorException {
        jobService.lockJob(jobId, delay);
    }



    @Autowired
    public EmcJobRestController(JobService jobService) {
        this.jobService = jobService;
    }

}
