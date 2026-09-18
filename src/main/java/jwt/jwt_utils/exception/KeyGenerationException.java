package jwt.jwt_utils.exception;

/**
 * Raised when the runtime cannot provide the cryptographic primitives required
 * to generate a key. This is an environment failure, not a client error.
 */
public class KeyGenerationException extends RuntimeException {

    public KeyGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
