package com.hoaxify.hoaxify.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Martin Kotulac
 * on 07/12/2020
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    UserRepository userRepository;

    @Test
    public void findByUsername_whenUserExists_returnUser() {
        User user = new User();

        user.setUsername("test-user");
        user.setDisplayName("test-display-name");
        user.setPassword("P4ssword");

        testEntityManager.persist(user);

        User inDb = userRepository.findByUsername("test-user");

        assertThat(inDb).isNotNull();
    }

    @Test
    public void findByUsername_whenUserNotExists_returnNull() {
        User inDb = userRepository.findByUsername("nonexistinguser");

        assertThat(inDb).isNull();
    }

}