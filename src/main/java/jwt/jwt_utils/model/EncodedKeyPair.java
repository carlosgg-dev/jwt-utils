package jwt.jwt_utils.model;

/**
 * A generated asymmetric key pair, both members Base64 encoded in their standard
 * ASN.1 representation: PKCS#8 for the private key, X.509 for the public key.
 */
public record EncodedKeyPair(
    String publicKey,
    String privateKey) {

}
