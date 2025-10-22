package jwt.jwt_utils.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jwt.jwt_utils.dto.PasswordDto;
import jwt.jwt_utils.service.JwtSecretKeyGenerator;
import jwt.jwt_utils.service.PasswordEncoderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

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
    void encodePassword_shouldReturnBadRequest_whenPasswordIsEmpty() throws Exception {

        PasswordDto emptyPasswordDto = PasswordDto.builder()
            .password("")
            .build();

        String emptyPasswordPayload = objectMapper.writeValueAsString(emptyPasswordDto);

        mockMvc.perform(post("/api/encode")
                .contentType(MediaType.APPLICATION_JSON)
                .content(emptyPasswordPayload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void encodePassword_shouldReturnBadRequest_whenPasswordIsMissing() throws Exception {

        String missingPasswordPayload = objectMapper.writeValueAsString(Map.of("someOtherField", "value"));

        mockMvc.perform(post("/api/encode")
                .contentType(MediaType.APPLICATION_JSON)
                .content(missingPasswordPayload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void encodePassword_shouldReturnOk_whenPasswordIsProvided() throws Exception {

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
    void generateSecret_shouldReturnOk_withGeneratedSecret() throws Exception {

        String generatedSecret = "generated_secret_key";

        given(jwtSecretKeyGenerator.generate()).willReturn(generatedSecret);

        mockMvc.perform(get("/api/generate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.secretKey").value(generatedSecret));
    }
}