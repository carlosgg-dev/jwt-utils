package jwt.jwt_utils.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Covers the one thing a mocked unit test cannot: that the service wired with the
 * real BCryptPasswordEncoder produces a hash the same encoder can verify.
 */
@SpringBootTest
class PasswordEncoderServiceIntegrationTest {

    @Autowired
    private PasswordEncoderService passwordEncoderService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void encode_producesAHashThatMatchesTheRawPassword() {

        String rawPassword = "testPassword";

        String encodedPassword = passwordEncoderService.encode(rawPassword);

        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
    }
}
