package com.hoaxify.hoaxify.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by Martin Kotulac
 * on 01/12/2020
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
