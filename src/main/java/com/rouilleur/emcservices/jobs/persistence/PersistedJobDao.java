package com.rouilleur.emcservices.jobs.persistence;

import java.util.Optional;

/**
 * Created by Rouilleur on 16/11/2016.
 */
public interface PersistedJobDao {

    Optional<PersistedJob> get(Long id) ;

    Iterable<PersistedJob> getAll() ;

    void save(PersistedJob myObject) ;

    void delete(Long id) ;
}
