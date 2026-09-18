package jwt.jwt_utils.controller;

import jakarta.validation.Valid;
import jwt.jwt_utils.dto.EncodedPasswordResponse;
import jwt.jwt_utils.dto.KeyPairResponse;
import jwt.jwt_utils.dto.PasswordDto;
import jwt.jwt_utils.dto.SecretKeyResponse;
import jwt.jwt_utils.model.EncodedKeyPair;
import jwt.jwt_utils.service.JwtSecretKeyGenerator;
import jwt.jwt_utils.service.PasswordEncoderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class JwtUtilsController {

    private final PasswordEncoderService passwordEncoderService;
    private final JwtSecretKeyGenerator jwtSecretKeyGenerator;

    @PostMapping("/encode")
    public EncodedPasswordResponse encodePassword(@Valid @RequestBody PasswordDto payload) {

        return new EncodedPasswordResponse(passwordEncoderService.encode(payload.getPassword()));
    }

    @GetMapping("/generateHS512")
    public SecretKeyResponse generateSecretHS512() {

        return new SecretKeyResponse(jwtSecretKeyGenerator.generateSymmetricHS512());
    }

    @GetMapping("/generateECDSAP256")
    public KeyPairResponse generateSecretECDSAP256() {

        EncodedKeyPair keyPair = jwtSecretKeyGenerator.generateAsymmetricECDSAP256();
        return new KeyPairResponse(keyPair.publicKey(), keyPair.privateKey());
    }
}
