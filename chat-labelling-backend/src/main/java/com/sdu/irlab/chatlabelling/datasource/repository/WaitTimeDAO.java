package com.sdu.irlab.chatlabelling.datasource.repository;

import com.sdu.irlab.chatlabelling.datasource.domain.WaitTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Cacheable;

@Repository
public interface WaitTimeDAO  extends JpaRepository<WaitTime,Long> {
    public WaitTime findByName(String username);
    public boolean existsByName(String name);
}