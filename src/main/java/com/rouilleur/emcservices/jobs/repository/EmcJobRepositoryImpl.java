package com.rouilleur.emcservices.jobs.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rouilleur.emcservices.config.TaskrunnerConfig;
import com.rouilleur.emcservices.exceptions.ErrorType;
import com.rouilleur.emcservices.exceptions.InternalErrorException;
import com.rouilleur.emcservices.jobs.EmcJob;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Rouilleur on 31/10/2016.
 */

@Repository
public class EmcJobRepositoryImpl implements EmcJobRepository {

    private final static Logger logger = LoggerFactory.getLogger(EmcJobRepositoryImpl.class);
    private String jobRepositoryBasePath = "D:\\developpement\\Dev_Home\\workarea\\jobs";
    private final AtomicLong jobCounter = new AtomicLong();
    ObjectMapper jobMapper;

    private  Map<Long,EmcJob> emcJobMap;

    @Override
    public void init() {
        logger.info("Initializing jobs repository");
        jobMapper = new ObjectMapper();

        if(emcJobMap ==null){
            emcJobMap = new ConcurrentHashMap<>();
        }
        logger.info("Repository initialized");
    }


    @Override
    //TODO : quick and dirty to rework
    //TODO : catch exceptions (ex : directory already exists) + check success
    //TODO : reuse the mapper
    public void create(EmcJob job) throws InternalErrorException {
        logger.info("Creating persistence structure for job {}", job.getId());

        long jobId = jobCounter.incrementAndGet();
        job.setId(jobId);
        job.setSubmitDate(new Date());


        File jobInfoDir = new File(jobRepositoryBasePath +"/"+ jobId + "/job_info");
        File jobInputDir = new File(jobRepositoryBasePath +"/"+ job.getId() + "/input");
        File jobWorkDir = new File(jobRepositoryBasePath +"/"+ job.getId() + "/work");
        File logDir = new File(jobRepositoryBasePath +"/"+ job.getId() + "/logs");
        File outputDir = new File(jobRepositoryBasePath +"/"+ job.getId() + "/output");

        jobInfoDir.mkdirs();
        jobInputDir.mkdirs();
        jobWorkDir.mkdirs();
        logDir.mkdirs();
        outputDir.mkdirs();

        save(job);

    }

    @Override
    public void save(EmcJob emcJob) throws InternalErrorException {
//        checkRepositoryInitialized();
//        emcJobMap.putIfAbsent(emcJob.getId(),emcJob);
        logger.info("Saving job {}", emcJob.getId());

        try {
            jobMapper.writeValue(new File(jobRepositoryBasePath +"/"+ emcJob.getId() + "/job_info/job_"+ emcJob.getId() + ".json"), emcJob);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public EmcJob findOne(Long jobId) throws InternalErrorException {
        checkRepositoryInitialized();
        logger.info("refreshing job {}", jobId);

        HashMap<String, Object> resultMap;
        String inputJson = jobRepositoryBasePath + "/" + jobId + "/job_info/job_" + jobId + ".json";




//        try {
//            File jsonInput = new File(inputJson);
//            EmcJob resultJob = jobMapper.readValue(jsonInput, EmcJob.class);
//
//        } catch (IOException e) {
//            throw new InternalErrorException(ErrorType.PERSISTENCE_ERROR, "Failed to parse json file for job "+ jobiD , e, true);
//        }



        EmcJob resultJob = new EmcJob();

        try {
            File jsonInput = new File(inputJson);
            TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {};
            resultMap = jobMapper.readValue(jsonInput, typeRef);
            //TODO : refresh other attributes

        } catch (IOException e) {
            throw new InternalErrorException(ErrorType.PERSISTENCE_ERROR, "Failed to parse json file for job "+ jobId , e, true);
        }



        if (resultMap.get("id") != null){
            long id = new Long((Integer) resultMap.get("id"));
            resultJob.setId(id);
        }else{
            //TODO : don't know exactly what...
        }

        if (resultMap.get("status") != null){
            resultJob.setStatus(EmcJob.JobStatus.valueOf((String) resultMap.get("status")));
        }else{
            //TODO : don't know exactly what...
        }

        if (resultMap.get("submitter") != null){
            resultJob.setSubmitter((String) resultMap.get("submitter"));
        }else{
            //TODO : don't know exactly what...
        }

        resultJob.setSubmitter((String) resultMap.get("description"));

        if (resultMap.get("submitDate") != null) {
            try {
                SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                resultJob.setSubmitDate(parser.parse((String) resultMap.get("submitDate")));
            } catch (ParseException e) {
                throw new InternalErrorException(ErrorType.PERSISTENCE_ERROR, "Failed to parse submitDate for job "+ resultJob.getId() , e, true);
            }
        }

        if (resultMap.get("endDate") != null) {
            try {
                SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                resultJob.setEndDate(parser.parse((String) resultMap.get("endDate")));
            } catch (ParseException e) {
                throw new InternalErrorException(ErrorType.PERSISTENCE_ERROR, "Failed to parse endDate for job " + resultJob.getId(), e, true);
            }
        }

        return resultJob;
    }

    @Override
    public boolean exists(Long jobID) throws InternalErrorException {
//        checkRepositoryInitialized();
//        return emcJobMap.containsKey(jobID);
        return false;
    }

    @Override
    public Iterable<EmcJob> findAll() throws InternalErrorException {
        checkRepositoryInitialized();
        return emcJobMap.values();
    }

    @Override
    public long count() throws InternalErrorException {
        checkRepositoryInitialized();
        return emcJobMap.size();
    }

    @Override
    public void delete(Long jobId) throws InternalErrorException {
        checkRepositoryInitialized();
        logger.info("Deleting {}", jobId);

        File directoryToDelete = new File (jobRepositoryBasePath + "/" + jobId);
        try {
            FileUtils.deleteDirectory(directoryToDelete);
        } catch (IOException e) {
            throw new InternalErrorException(ErrorType.PERSISTENCE_ERROR, "Failed to delete persisted info for job "+ jobId , e, true);
        }

        emcJobMap.remove(jobId);
    }

    @Override
    public void deleteAll() {
        emcJobMap.clear();
    }

    private void checkRepositoryInitialized() throws InternalErrorException {
        if (emcJobMap ==null){
            logger.error("Trying to access an uninitialized repository!");
            throw new InternalErrorException(ErrorType.REPO_NOT_INITIALIZED);
        }
    }


    private boolean isValidToBePersisted(EmcJob job){
        return true;
    }

}

