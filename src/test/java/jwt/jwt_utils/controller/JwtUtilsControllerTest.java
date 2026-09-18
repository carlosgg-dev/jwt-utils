package jwt.jwt_utils.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jwt.jwt_utils.dto.PasswordDto;
import jwt.jwt_utils.exception.KeyGenerationException;
import jwt.jwt_utils.model.EncodedKeyPair;
import jwt.jwt_utils.service.JwtSecretKeyGenerator;
import jwt.jwt_utils.service.PasswordEncoderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JwtUtilsController.class)
@AutoConfigureMockMvc(addFilters = false)
class JwtUtilsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PasswordEncoderService passwordEncoderService;

    @MockitoBean
    private JwtSecretKeyGenerator jwtSecretKeyGenerator;

    @Test
    void encodePassword_whenPasswordIsEmpty_shouldReturnBadRequest() throws Exception {

        PasswordDto emptyPasswordDto = PasswordDto.builder()
            .password("")
            .build();

        String emptyPasswordPayload = objectMapper.writeValueAsString(emptyPasswordDto);

        mockMvc.perform(post("/api/encode")
                .contentType(MediaType.APPLICATION_JSON)
                .content(emptyPasswordPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("password must not be blank"));
    }

    @Test
    void encodePassword_whenPasswordIsMissing_shouldReturnBadRequest() throws Exception {

        String missingPasswordPayload = objectMapper.writeValueAsString(Map.of("someOtherField", "value"));

        mockMvc.perform(post("/api/encode")
                .contentType(MediaType.APPLICATION_JSON)
                .content(missingPasswordPayload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void encodePassword_whenPasswordIsProvided_shouldReturnEncodedPassword() throws Exception {

        PasswordDto validPasswordDto = PasswordDto.builder()
            .password("testPassword")
            .build();

        String validPasswordPayload = objectMapper.writeValueAsString(validPasswordDto);
        String encodedPassword = "encoded_testPassword";

        given(passwordEncoderService.encode(anyString())).willReturn(encodedPassword);

        mockMvc.perform(post("/api/encode")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validPasswordPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.encodedPassword").value(encodedPassword));
    }

    @Test
    void generateSecretHS512_shouldReturnGeneratedSecret() throws Exception {

        String generatedSecret = "generated_secret_key";

        given(jwtSecretKeyGenerator.generateSymmetricHS512()).willReturn(generatedSecret);

        mockMvc.perform(get("/api/generateHS512"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.secretKey").value(generatedSecret));
    }

    @Test
    void generateSecretECDSAP256_shouldReturnGeneratedKeyPair() throws Exception {

        EncodedKeyPair keyPair = new EncodedKeyPair("public_key", "private_key");

        given(jwtSecretKeyGenerator.generateAsymmetricECDSAP256()).willReturn(keyPair);

        mockMvc.perform(get("/api/generateECDSAP256"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.publicKey").value("public_key"))
                .andExpect(jsonPath("$.privateKey").value("private_key"));
    }

    @Test
    void generateSecretECDSAP256_whenKeyGenerationFails_shouldReturnInternalServerError() throws Exception {

        String failureMessage = "Unable to generate an ECDSA P-256 key pair";

        given(jwtSecretKeyGenerator.generateAsymmetricECDSAP256())
                .willThrow(new KeyGenerationException(failureMessage, new NoSuchAlgorithmException()));

        mockMvc.perform(get("/api/generateECDSAP256"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(failureMessage));
    }
}
