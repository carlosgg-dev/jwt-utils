package jwt.jwt_utils.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class PasswordDto {

    @NotBlank
    String password;
}
