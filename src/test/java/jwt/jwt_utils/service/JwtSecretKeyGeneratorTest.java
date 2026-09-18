package jwt.jwt_utils.service;

import io.jsonwebtoken.io.Decoders;
import jwt.jwt_utils.model.EncodedKeyPair;
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
    void generateSymmetricHS512_returnsValidBase64UrlKey() {

        String base64UrlKey = jwtSecretKeyGenerator.generateSymmetricHS512();

        assertNotNull(base64UrlKey);

        assertDoesNotThrow(() -> {
            byte[] decoded = Decoders.BASE64URL.decode(base64UrlKey);
            assertTrue(decoded.length >= 64);
        });
    }

    @Test
    void generateSymmetricHS512_generatesDifferentKeysOnMultipleCalls() {

        String firstKey = jwtSecretKeyGenerator.generateSymmetricHS512();
        String secondKey = jwtSecretKeyGenerator.generateSymmetricHS512();

        assertNotEquals(firstKey, secondKey);
    }

    @Test
    void generateAsymmetricECDSAP256_returnsValidKeyPair() {

        EncodedKeyPair keyPair = jwtSecretKeyGenerator.generateAsymmetricECDSAP256();

        assertNotNull(keyPair);
        assertDoesNotThrow(() -> Decoders.BASE64.decode(keyPair.publicKey()));
        assertDoesNotThrow(() -> Decoders.BASE64.decode(keyPair.privateKey()));
    }
}