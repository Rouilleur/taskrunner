package com.rouilleur.emcservices.jobs;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by Rouilleur on 31/10/2016.
 */


@Entity
public class EmcJob {

    @Id
    @GeneratedValue
    private Long id;

    private String submiter;

    protected EmcJob(){

    }

    public EmcJob(String submiter){
        //this.id = 42L;
        this.submiter = submiter;
    }

    public Long getId() {
        return id;
    }

    public String getSubmiter() {
        return submiter;
    }
}
