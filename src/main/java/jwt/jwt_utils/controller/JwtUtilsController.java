package jwt.jwt_utils.controller;

import jakarta.validation.Valid;
import jwt.jwt_utils.dto.PasswordDto;
import jwt.jwt_utils.service.JwtSecretKeyGenerator;
import jwt.jwt_utils.service.PasswordEncoderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class JwtUtilsController {

    private final PasswordEncoderService passwordEncoderService;
    private final JwtSecretKeyGenerator jwtSecretKeyGenerator;

    @PostMapping("/encode")
    public Map<String, String> encodePassword(@Valid @RequestBody PasswordDto payload) {

        String encodedPassword = passwordEncoderService.encode(payload.getPassword());
        return Map.of("encodedPassword", encodedPassword);
    }

    @GetMapping("/generateHS512")
    public Map<String, String> generateSecretHS512() {

        String secretKey = jwtSecretKeyGenerator.generateSymmetricHS512();
        return Map.of("secretKey", secretKey);
    }

    @GetMapping("/generateECDSAP256")
    public Map<String, String> generateSecretECDSAP256() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {

        return jwtSecretKeyGenerator.generateAsymmetricECDSAP256();
    }
}
