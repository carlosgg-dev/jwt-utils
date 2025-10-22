package jwt.jwt_utils.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class JwtUtilsServicesIntegrationTest {

    @Autowired
    private JwtSecretKeyGenerator jwtSecretKeyGenerator;

    @Autowired
    private PasswordEncoderService passwordEncoderService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void contextLoadsWithAllServices() {

        assertNotNull(jwtSecretKeyGenerator);
        assertNotNull(passwordEncoderService);
        assertNotNull(passwordEncoder);
    }

    @Test
    void jwtSecretKeyGeneratorWorksInSpringContext() {

        assertDoesNotThrow(() -> jwtSecretKeyGenerator.generate());
    }

    @Test
    void passwordEncoderServiceWorksInSpringContext() {

        assertDoesNotThrow(() -> passwordEncoderService.encode("testPassword"));
    }

    @Test
    void passwordEncoderServiceUsesRealPasswordEncoder() {

        String rawPassword = "testPassword";

        String directEncodedPassword = passwordEncoder.encode(rawPassword);

        assertNotNull(directEncodedPassword);
        assertNotEquals(rawPassword, directEncodedPassword);
    }

    @Test
    void servicesCanBeUsedTogether() {

        assertDoesNotThrow(() -> {
            jwtSecretKeyGenerator.generate();
            passwordEncoderService.encode("combinedTestPassword");
        });
    }
}
