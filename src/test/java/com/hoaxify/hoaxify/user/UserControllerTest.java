package com.hoaxify.hoaxify.user;

import com.hoaxify.hoaxify.error.ApiError;
import com.hoaxify.hoaxify.shared.GenericResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Martin Kotulac
 * on 30/11/2020
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserControllerTest {

    public static final String URL = "/api/v1/users";

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    UserRepository userRepository;

    @Before
    public void cleanup() {
        userRepository.deleteAll();
    }

    private User createValidUser() {
        User user = new User();
        user.setUsername("test-user");
        user.setDisplayName("test-display");
        user.setPassword("P4ssword");
        return user;
    }

    public <T> ResponseEntity<T> postSignUp(Object request, Class<T> response) {
        return testRestTemplate.postForEntity(URL, request, response);
    }


    @Test
    public void postUser_whenUserIsValid_receiveOk() {
        User user = createValidUser();

        ResponseEntity<Object> response = postSignUp(user, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    public void postUser_whenUserIsValid_userSavedToDatabase() {
        User user = createValidUser();

        postSignUp(user, Object.class);

        assertThat(userRepository.count()).isEqualTo(1);
    }

    @Test
    public void postUser_whenUserIsValid_receiveSuccessMessage() {
        User user = createValidUser();

        ResponseEntity<GenericResponse> response = postSignUp(user, GenericResponse.class);

        assertThat(response.getBody().getMessage()).isNotNull();
    }

    @Test
    public void postUser_whenUserIsValid_passwordIsHashedInDatabase() {
        User user = createValidUser();

        postSignUp(user, GenericResponse.class);

        List<User> users = userRepository.findAll();
        User savedUser = users.get(0);

        assertThat(savedUser.getPassword()).isNotEqualTo(user.getPassword());
    }

    @Test
    public void postUser_whenUserHasNullUsername_recieveBadRequest() {
        User user = createValidUser();
        user.setUsername(null);

        ResponseEntity<Object> response = postSignUp(user, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasNullDisplayName_recieveBadRequest() {
        User user = createValidUser();
        user.setDisplayName(null);

        ResponseEntity<Object> response = postSignUp(user, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasNullPassword_recieveBadRequest() {
        User user = createValidUser();
        user.setPassword(null);

        ResponseEntity<Object> response = postSignUp(user, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasUsernameWithLessThanRequired_recieveBadRequest() {
        User user = createValidUser();
        user.setUsername("abc");

        ResponseEntity<Object> response = postSignUp(user, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasPasswordWithLessThanRequired_recieveBadRequest() {
        User user = createValidUser();
        user.setPassword("P4sswd");

        ResponseEntity<Object> response = postSignUp(user, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasDisplayNameExceedsLengthLimit_recieveBadRequest() {
        User user = createValidUser();
        String valueOf256Chars = IntStream.rangeClosed(1, 256).mapToObj(x -> "a").collect(Collectors.joining());
        user.setDisplayName(valueOf256Chars);

        ResponseEntity<Object> response = postSignUp(user, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasPasswordExceedsLengthLimit_recieveBadRequest() {
        User user = createValidUser();
        String valueOf256Chars = IntStream.rangeClosed(1, 256).mapToObj(x -> "a").collect(Collectors.joining());
        user.setPassword(valueOf256Chars);

        ResponseEntity<Object> response = postSignUp(user, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasUserNameExceedsLengthLimit_recieveBadRequest() {
        User user = createValidUser();
        String valueOf256Chars = IntStream.rangeClosed(1, 256).mapToObj(x -> "a").collect(Collectors.joining());
        user.setUsername(valueOf256Chars);

        ResponseEntity<Object> response = postSignUp(user, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasPasswordWithAllLowercase_recieveBadRequest() {
        User user = createValidUser();
        user.setPassword("alllowercase");

        ResponseEntity<Object> response = postSignUp(user, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasPasswordWithAllUppercase_recieveBadRequest() {
        User user = createValidUser();
        user.setPassword("ALLUPPERCASE");

        ResponseEntity<Object> response = postSignUp(user, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasPasswordWithAllNumbers_recieveBadRequest() {
        User user = createValidUser();
        user.setPassword("1234567890");

        ResponseEntity<Object> response = postSignUp(user, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserIsInvalid_recieveApiError() {
        User user = new User();

        ResponseEntity<ApiError> response = postSignUp(user, ApiError.class);

        assertThat(response.getBody().getUrl()).isEqualTo(URL);
    }

    @Test
    public void postUser_whenUserIsInvalid_recieveApiErrorWithValidationErrors() {
        User user = new User();

        ResponseEntity<ApiError> response = postSignUp(user, ApiError.class);

        assertThat(response.getBody().getValidationErrors().size()).isEqualTo(3);
    }

    @Test
    public void postUser_whenUserHasNullUsername_recieveMessageOfNullErrorForUsername() {
        User user = createValidUser();
        user.setUsername(null);

        ResponseEntity<ApiError> response = postSignUp(user, ApiError.class);
        Map<String, String> validationErrors = response.getBody().getValidationErrors();

        assertThat(validationErrors.get("username")).isEqualTo("Username cannot be null");
    }

    @Test
    public void postUser_whenUserHasNullPassword_recieveGenericMessageOfNullError() {
        User user = createValidUser();
        user.setPassword(null);

        ResponseEntity<ApiError> response = postSignUp(user, ApiError.class);
        Map<String, String> validationErrors = response.getBody().getValidationErrors();

        assertThat(validationErrors.get("password")).isEqualTo("Cannot be null");
    }

    @Test
    public void postUser_whenUserHasInvalidLengthUsername_recieveGenericMessageOfSizeError() {
        User user = createValidUser();
        user.setUsername("abc");

        ResponseEntity<ApiError> response = postSignUp(user, ApiError.class);
        Map<String, String> validationErrors = response.getBody().getValidationErrors();

        assertThat(validationErrors.get("username")).isEqualTo("It must have minimum 4 and maximum 255 characters");
    }

    @Test
    public void postUser_whenUserHasInvalidPasswordPattern_recieveMessageOfPaswwordPatternError() {
        User user = createValidUser();
        user.setPassword("alllowercase");

        ResponseEntity<ApiError> response = postSignUp(user, ApiError.class);
        Map<String, String> validationErrors = response.getBody().getValidationErrors();

        assertThat(validationErrors.get("password")).isEqualTo("Password must have at least one uppercase, one lowercase letter and one number");
    }

    @Test
    public void postUser_whenAnotherUserHasSameUsername_recieveBadRequest() {
        userRepository.save(createValidUser());

        User user = createValidUser();

        ResponseEntity<Object> response = postSignUp(user, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    public void postUser_whenAnotherUserHasSameUsername_recieveMessageOfDuplicateUsername() {
        userRepository.save(createValidUser());

        User user = createValidUser();

        ResponseEntity<ApiError> response = postSignUp(user, ApiError.class);
        Map<String, String> validationErrors = response.getBody().getValidationErrors();

        assertThat(validationErrors.get("username")).isEqualTo("This name is in use");

    }
}