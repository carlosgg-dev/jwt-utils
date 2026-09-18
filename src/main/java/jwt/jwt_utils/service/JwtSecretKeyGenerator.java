package jwt.jwt_utils.service;

import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import jwt.jwt_utils.exception.KeyGenerationException;
import jwt.jwt_utils.model.EncodedKeyPair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;
import java.util.Base64;

/**
 * Service for generating cryptographically secure JWT secret keys.
 */
@Slf4j
@Service
public class JwtSecretKeyGenerator {

    private static final int HS512_KEY_LENGTH_BYTES = 64;
    private static final String EC_ALGORITHM = "EC";
    private static final String P256_CURVE = "secp256r1";
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
     * @return the Base64 encoded key pair.
     * @throws KeyGenerationException if the runtime cannot provide the EC curve.
     */
    public EncodedKeyPair generateAsymmetricECDSAP256() {

        KeyPair keyPair = generateEcKeyPair();

        Base64.Encoder encoder = Base64.getEncoder();
        String publicKey = encoder.encodeToString(keyPair.getPublic().getEncoded());
        String privateKey = encoder.encodeToString(keyPair.getPrivate().getEncoded());

        log.info("Generated ECDSA P-256 asymmetric key pair successfully");
        return new EncodedKeyPair(publicKey, privateKey);
    }

    private KeyPair generateEcKeyPair() {

        try {
            KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance(EC_ALGORITHM);
            keyGenerator.initialize(new ECGenParameterSpec(P256_CURVE));
            return keyGenerator.generateKeyPair();
        } catch (GeneralSecurityException exception) {
            throw new KeyGenerationException("Unable to generate an ECDSA P-256 key pair", exception);
        }
    }
}
