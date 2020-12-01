package com.hoaxify.hoaxify.user;

import com.hoaxify.hoaxify.shared.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by Martin Kotulac
 * on 30/11/2020
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public GenericResponse saveUser(@Valid @NotNull @RequestBody User user) {
        userService.save(user);
        return new GenericResponse("User saved");
    }
}
