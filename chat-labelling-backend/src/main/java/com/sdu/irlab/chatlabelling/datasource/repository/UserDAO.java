package com.sdu.irlab.chatlabelling.datasource.repository;

import com.sdu.irlab.chatlabelling.datasource.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Cacheable;

import java.util.List;

@Repository
public interface UserDAO  extends JpaRepository<User,Long> {
    public User findByName(String username);
    public List<User> findByRole(String role);
}
