package jwt.jwt_utils.service;

import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.SecureRandom;

/**
 * Service for generating JWT secret keys.
 * This service provides functionality to generate cryptographically secure 
 * secret keys suitable for JWT signing using the HS512 algorithm.
 */
@Slf4j
@Service
public class JwtSecretKeyGenerator {

    private static final int KEY_LENGTH_BYTES = 64;

    /**
     * Generates a secure JWT secret key for HS512 algorithm.
     * Creates a 512-bit (64 bytes) cryptographically strong random key.
     *
     * @return a Base64Url encoded secret key.
     */
    public String generate() {

        byte[] keyBytes = new byte[KEY_LENGTH_BYTES];
        new SecureRandom().nextBytes(keyBytes);

        SecretKey key = Keys.hmacShaKeyFor(keyBytes);
        String base64UrlEncodedSecret = Encoders.BASE64URL.encode(key.getEncoded());
        log.info("Generated JWT secret key successfully");

        return base64UrlEncodedSecret;
    }
}
