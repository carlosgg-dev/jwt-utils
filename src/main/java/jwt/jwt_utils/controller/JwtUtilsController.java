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

    @GetMapping("/generate")
    public Map<String, String> generateSecret() {

        String secretKey = jwtSecretKeyGenerator.generate();
        return Map.of("secretKey", secretKey);
    }
}
