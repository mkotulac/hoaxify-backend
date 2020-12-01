package com.hoaxify.hoaxify.user;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Martin Kotulac
 * on 01/12/2020
 */
public interface UserRepository extends JpaRepository<User, Long> {

}
