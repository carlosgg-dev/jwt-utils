package jwt.jwt_utils.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PasswordEncoderServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private PasswordEncoderService passwordEncoderService;

    @Test
    void encode_shouldReturnEncodedPassword() {

        String rawPassword = "testPassword";
        String mockedEncodedPassword = "mockedEncodedPassword";

        given(passwordEncoder.encode(rawPassword)).willReturn(mockedEncodedPassword);

        String encodedPassword = passwordEncoderService.encode(rawPassword);

        assertNotEquals(rawPassword, encodedPassword);
    }
}
