package com.sdu.irlab.chatlabelling.datasource.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
public class WaitTime extends BaseEntity {
    @Column(unique = true)
    private String name;

    private String time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//     @Override
    public String getUsername() {
        return this.name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time= time;
    }


}