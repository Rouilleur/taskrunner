package com.rouilleur.emcservices.jobs;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * Created by Rouilleur on 09/11/2016.
 */
public class EmcJobTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void emcJob_newJob_SomeFieldShouldBeSet() throws Exception {

        EmcJob aJob = new EmcJob("Annie", "Farming");

        assertEquals("Annie", aJob.getSubmitter());
        assertEquals("Farming", aJob.getDescription());
        assertNotNull(aJob.getSubmitDate());
        assertNotNull(aJob.getId());
        assertNull(aJob.getEndDate());
        assertEquals(EmcJob.JobStatus.CREATED, aJob.getStatus());

    }


    @Test
    public void initJobCounter_newJob_CounterShouldBeIncremented() throws Exception {

        EmcJob aJob = new EmcJob("Annie", "Farming");

        assertEquals(new Long(6),aJob.getId());

        EmcJob anotherJob = new EmcJob("Annie", "Farming");

        assertEquals(new Long(7),anotherJob.getId());
    }


}