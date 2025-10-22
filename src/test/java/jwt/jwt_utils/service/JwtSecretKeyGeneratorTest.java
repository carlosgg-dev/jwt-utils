package jwt.jwt_utils.service;

import io.jsonwebtoken.io.Decoders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtSecretKeyGeneratorTest {

    private JwtSecretKeyGenerator jwtSecretKeyGenerator;

    @BeforeEach
    void setUp() {
        jwtSecretKeyGenerator = new JwtSecretKeyGenerator();
    }

    @Test
    void generate_returnsValidBase64UrlKey() {

        String base64UrlKey = jwtSecretKeyGenerator.generate();

        assertNotNull(base64UrlKey);

        assertDoesNotThrow(() -> {
            byte[] decoded = Decoders.BASE64URL.decode(base64UrlKey);
            assertTrue(decoded.length >= 64);
        });
    }

    @Test
    void generate_generatesDifferentKeysOnMultipleCalls() {

        String firstKey = jwtSecretKeyGenerator.generate();
        String secondKey = jwtSecretKeyGenerator.generate();

        assertNotEquals(firstKey, secondKey);
    }
}