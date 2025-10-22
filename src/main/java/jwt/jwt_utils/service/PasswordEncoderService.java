package jwt.jwt_utils.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service for password encoding operations.
 * This service provides functionality to encode raw passwords.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordEncoderService {

    private final PasswordEncoder passwordEncoder;

    /**
     * Encodes a raw password using the configured password encoder.
     *
     * @param rawPassword the plain text password to be encoded.
     * @return the encoded password.
     */
    public String encode(final String rawPassword) {

        String encodedPassword = passwordEncoder.encode(rawPassword);
        log.info("Encoded password generated successfully");

        return encodedPassword;
    }
}