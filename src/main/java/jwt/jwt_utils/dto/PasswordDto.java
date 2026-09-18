package jwt.jwt_utils.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class PasswordDto {

    // Pinned so the API contract does not shift with the JVM default locale.
    @NotBlank(message = "must not be blank")
    String password;
}
