package com.rouilleur.emcservices.jobs;

import com.rouilleur.emcservices.Exceptions.ErrorType;
import com.rouilleur.emcservices.Exceptions.InternalErrorException;
import com.rouilleur.emcservices.Exceptions.InternalErrorMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;

/**
 * Created by Rouilleur on 09/11/2016.
 */
public class EmcJobRepositoryImplTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void save_UninitializedRepository_ShouldThrowException() throws Exception {
        EmcJobRepositoryImpl aRepository = new EmcJobRepositoryImpl();
        EmcJob aJobs = new EmcJob("Annie", "Farming");
        exception.expect(InternalErrorException.class);
        exception.expect(InternalErrorMatcher.isOfType(ErrorType.REPO_NOT_INITIALIZED));
        aRepository.save(aJobs);

    }

    @Test
    public void save_AddOneJob_RepositoryShouldContainTheJob() throws Exception {
        EmcJobRepositoryImpl aRepository = new EmcJobRepositoryImpl();
        aRepository.init();
        EmcJob aJob = new EmcJob("Annie", "Farming");

        aRepository.save(aJob);

        EmcJob storedJob = aRepository.findAll().iterator().next();

        assertEquals(aJob, storedJob);
    }

    @Test
    public void stopJob() throws Exception {

    }

    @Test
    public void deleteJob() throws Exception {

    }

    @Test
    public void findOne() throws Exception {

    }

    @Test
    public void exists() throws Exception {

    }

    @Test
    public void findAll_AddTwoJob_ShouldGetTwoJobs() throws Exception {
        EmcJobRepositoryImpl aRepository = new EmcJobRepositoryImpl();
        aRepository.init();
        EmcJob aJob = new EmcJob("Annie", "Farming");
        EmcJob anotherJob = new EmcJob("Warwick", "Jungling");

        aRepository.save(aJob);
        aRepository.save(anotherJob);
        int counter = 0;
        for (Iterator i = aRepository.findAll().iterator(); i.hasNext();i.next()){
            counter++;
        }
        assertEquals(2,counter);
    }

    @Test
    public void findAll1() throws Exception {

    }

    @Test
    public void count_AddThreeJob_ShouldReturn3() throws Exception {
        EmcJobRepositoryImpl aRepository = new EmcJobRepositoryImpl();
        aRepository.init();
        EmcJob aJob = new EmcJob("Annie", "Farming");
        EmcJob anotherJob = new EmcJob("Warwick", "Jungling");
        EmcJob thirdJob = new EmcJob("Rammus", "isOK");

        aRepository.save(aJob);
        aRepository.save(anotherJob);
        aRepository.save(thirdJob);

        assertEquals(3,aRepository.count());

    }

    @Test
    public void delete() throws Exception {

    }

    @Test
    public void delete1() throws Exception {

    }

    @Test
    public void delete2() throws Exception {

    }

    @Test
    public void deleteAll() throws Exception {

    }

}