package com.rouilleur.emcservices.jobs.persistence;

import java.util.Optional;

/**
 * Created by Rouilleur on 16/11/2016.
 */
public class PersistedJobDaoImpl implements PersistedJobDao {

    private String jobRepositoryBasePath = "D:\\developpement\\Dev_Home\\workarea\\jobs";




    public Optional<PersistedJob> get(String id) {
        return null;
    }

    @Override
    public Optional<PersistedJob> get(Long id) {
        return null;
    }

    public Iterable<PersistedJob> getAll() {
        return null;
    }

    public void save(PersistedJob myObject) {

    }

    @Override
    public void delete(Long id) {

    }

    public void delete(String id) {


    }
}