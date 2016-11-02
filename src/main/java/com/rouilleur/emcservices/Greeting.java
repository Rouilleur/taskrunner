package com.rouilleur.emcservices;

/**
 * Created by Rouilleur on 31/10/2016.
 */
public class Greeting {




    private final String content;
    private final long id;


    public Greeting(long id, String content) {
        this.content = content;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }



}
