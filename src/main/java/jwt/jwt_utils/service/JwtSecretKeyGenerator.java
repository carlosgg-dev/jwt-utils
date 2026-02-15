package jwt.jwt_utils.service;

import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;
import java.util.Base64;
import java.util.Map;

/**
 * Service for generating cryptographically secure JWT secret keys.
 */
@Slf4j
@Service
public class JwtSecretKeyGenerator {

    private static final int HS512_KEY_LENGTH_BYTES = 64;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * Generates a secure JWT secret key for HS512 algorithm.
     * Creates a 512-bit (64 bytes) cryptographically strong random key.
     *
     * @return a Base64Url encoded secret key.
     */
    public String generateSymmetricHS512() {

        byte[] keyBytes = new byte[HS512_KEY_LENGTH_BYTES];
        SECURE_RANDOM.nextBytes(keyBytes);

        SecretKey key = Keys.hmacShaKeyFor(keyBytes);
        String base64UrlEncodedSecret = Encoders.BASE64URL.encode(key.getEncoded());

        log.info("Generated HS512 symmetric key successfully");
        return base64UrlEncodedSecret;
    }

    /**
     * Generates a secure ECDSA P-256 key pair for asymmetric JWT signing.
     * Uses elliptical curve which is widely supported and secure for JWTs.
     *
     * @return a map containing "privateKey" and "publicKey".
     * @throws NoSuchAlgorithmException if the EC algorithm is not available in the environment.
     * @throws InvalidAlgorithmParameterException if the specified EC curve is invalid.
     */
    public Map<String, String> generateAsymmetricECDSAP256() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {

        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("EC");
        ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256r1");
        keyGenerator.initialize(ecSpec);

        KeyPair keyPair = keyGenerator.generateKeyPair();
        String privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
        String publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());

        log.info("Generated ECDSA P-256 asymmetric key pair successfully");
        return Map.of("privateKey", privateKey, "publicKey", publicKey);
    }
}
