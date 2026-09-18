package jwt.jwt_utils.dto;

import com.fasterxml.jackson.annotation.JsonRawValue;

/**
 * A JWK is itself a JSON document, so both members are embedded verbatim instead of
 * being escaped into a string. The values come from the JWK serializer, never from
 * client input.
 */
public record JwkPairResponse(
    @JsonRawValue String publicKey,
    @JsonRawValue String privateKey) {

}
