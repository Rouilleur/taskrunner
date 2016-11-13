package com.rouilleur.emcservices.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Created by Rouilleur on 13/11/2016.
 */

//TODO : This looks ugly, find a better way to do that

@Configuration
public class TaskrunnerConfig {

    public static int lockTimeout;

    @Value("${jobs.lock.timeout.seconds}")
    private int lockTimeoutToSet;

    public static int getLockTimeout() {
        return lockTimeout;
    }

    @PostConstruct
    public void init(){
        lockTimeout = lockTimeoutToSet;
    }
}
