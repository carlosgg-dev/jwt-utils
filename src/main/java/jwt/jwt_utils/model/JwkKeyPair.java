package jwt.jwt_utils.model;

/**
 * A generated key pair in JWK format (RFC 7517), each member a self-contained JSON
 * document carrying its own curve, algorithm and key id.
 */
public record JwkKeyPair(
    String publicKey,
    String privateKey) {

}
