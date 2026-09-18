package jwt.jwt_utils.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.io.Decoders;
import jwt.jwt_utils.model.EncodedKeyPair;
import jwt.jwt_utils.model.JwkKeyPair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtSecretKeyGeneratorTest {

    private static final int P256_PRIVATE_SCALAR_BYTES = 32;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

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

    @Test
    void generateJwkECDSAP256_returnsAPublicJwkWithoutThePrivateMember() throws Exception {

        JwkKeyPair jwkPair = jwtSecretKeyGenerator.generateJwkECDSAP256();

        Map<String, Object> publicJwk = parse(jwkPair.publicKey());

        assertEquals("EC", publicJwk.get("kty"));
        assertEquals("P-256", publicJwk.get("crv"));
        assertEquals("ES256", publicJwk.get("alg"));
        assertFalse(publicJwk.containsKey("d"));
    }

    @Test
    void generateJwkECDSAP256_returnsAPrivateJwkCarryingTheUnredactedScalar() throws Exception {

        JwkKeyPair jwkPair = jwtSecretKeyGenerator.generateJwkECDSAP256();

        Map<String, Object> privateJwk = parse(jwkPair.privateKey());
        byte[] privateScalar = Decoders.BASE64URL.decode((String) privateJwk.get("d"));

        assertEquals(P256_PRIVATE_SCALAR_BYTES, privateScalar.length);
    }

    @Test
    void generateJwkECDSAP256_sharesTheSameKeyIdAcrossBothKeys() throws Exception {

        JwkKeyPair jwkPair = jwtSecretKeyGenerator.generateJwkECDSAP256();

        Map<String, Object> publicJwk = parse(jwkPair.publicKey());
        Map<String, Object> privateJwk = parse(jwkPair.privateKey());

        assertEquals(publicJwk.get("kid"), privateJwk.get("kid"));
    }

    private Map<String, Object> parse(String json) throws Exception {

        return OBJECT_MAPPER.readValue(json, new TypeReference<>() {});
    }
}
