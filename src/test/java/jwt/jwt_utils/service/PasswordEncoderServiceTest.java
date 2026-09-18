package jwt.jwt_utils.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class PasswordEncoderServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private PasswordEncoderService passwordEncoderService;

    @Test
    void encode_shouldReturnThePasswordEncoderResult() {

        String rawPassword = "testPassword";
        String expectedHash = "$2a$10$expectedHash";

        given(passwordEncoder.encode(rawPassword)).willReturn(expectedHash);

        String encodedPassword = passwordEncoderService.encode(rawPassword);

        assertEquals(expectedHash, encodedPassword);
        then(passwordEncoder).should().encode(rawPassword);
    }
}
