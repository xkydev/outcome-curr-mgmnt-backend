package co.edu.icesi.dev.outcome_curr_mgmt.testing.system.rs.management;

import co.edu.icesi.dev.outcome_curr.mgmt.model.stdindto.management.LoginInDTO;
import co.edu.icesi.dev.outcome_curr.mgmt.model.stdoutdto.management.LoginOutDTO;
import co.edu.icesi.dev.outcome_curr.mgmt.rs.management.AuthUserController;
import co.edu.icesi.dev.outcome_curr_mgmt.testing.system.rs.util.BaseSmokeIT;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(classes = {AuthUserController.class})
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerSmokeIT extends BaseSmokeIT {

    public static final String OUT_CURR_TEST_USER = "OutCurrTestUser";

    public static final String USER_PASSWORD = "123456";

    private static String testUserJWTToken;
    private final String LOGIN_ENDPOINT = "/v1/auth/users/login";

    @Value("${test.server.url}")
    private String server;

    @BeforeAll
    void init() {
        testUserJWTToken = getTestUserJWTToken(OUT_CURR_TEST_USER, USER_PASSWORD, server);
    }

    @Test
    void loginWithValidCredentials() {
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + testUserJWTToken);

        LoginInDTO loginRequest = new LoginInDTO("OutCurrTestUser", "123456");
        HttpEntity<LoginInDTO> request = new HttpEntity<>(loginRequest, headers);

        ResponseEntity<LoginOutDTO> response = testRestTemplate.exchange(
                server + LOGIN_ENDPOINT,
                HttpMethod.POST,
                request,
                LoginOutDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void loginWithInvalidCredentials() {
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + testUserJWTToken);

        LoginInDTO loginRequest = new LoginInDTO("WrongUser", "wrongpassword");
        HttpEntity<LoginInDTO> request = new HttpEntity<>(loginRequest, headers);

        ResponseEntity<LoginOutDTO> response = testRestTemplate.exchange(
                server + LOGIN_ENDPOINT,
                HttpMethod.POST,
                request,
                LoginOutDTO.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void loginWithEmptyCredentials() {
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + testUserJWTToken);

        LoginInDTO loginRequest = new LoginInDTO("", "");
        HttpEntity<LoginInDTO> request = new HttpEntity<>(loginRequest, headers);

        ResponseEntity<LoginOutDTO> response = testRestTemplate.exchange(
                server + LOGIN_ENDPOINT,
                HttpMethod.POST,
                request,
                LoginOutDTO.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void loginWithMissingPassword() {
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + testUserJWTToken);

        LoginInDTO loginRequest = new LoginInDTO("OutCurrTestUser", "");
        HttpEntity<LoginInDTO> request = new HttpEntity<>(loginRequest, headers);

        ResponseEntity<LoginOutDTO> response = testRestTemplate.exchange(
                server + LOGIN_ENDPOINT,
                HttpMethod.POST,
                request,
                LoginOutDTO.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void loginWithMissingUsername() {
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + testUserJWTToken);

        LoginInDTO loginRequest = new LoginInDTO("", "123456");
        HttpEntity<LoginInDTO> request = new HttpEntity<>(loginRequest, headers);

        ResponseEntity<LoginOutDTO> response = testRestTemplate.exchange(
                server + LOGIN_ENDPOINT,
                HttpMethod.POST,
                request,
                LoginOutDTO.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void loginWithInvalidJWTToken() {
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer invalid_token");

        LoginInDTO loginRequest = new LoginInDTO("OutCurrTestUser", "123456");
        HttpEntity<LoginInDTO> request = new HttpEntity<>(loginRequest, headers);

        ResponseEntity<LoginOutDTO> response = testRestTemplate.exchange(
                server + LOGIN_ENDPOINT,
                HttpMethod.POST,
                request,
                LoginOutDTO.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
    }
}
