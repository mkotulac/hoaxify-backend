package com.hoaxify.hoaxify.user;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by Martin Kotulac
 * on 30/11/2020
 */
@Data
@Entity
public class User {

    @Id
    @GeneratedValue
    private long id;

    private String username;

    private String displayName;

    private String password;

}
