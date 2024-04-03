package library.backend.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SignUpRequestDto(
                @NotBlank(message = "Name is required") String name,
                @NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email,
                @NotBlank(message = "Phone number is required") @Pattern(regexp = "\\d{10}", message = "Invalid phone number format") String phoneNo,
                @NotBlank(message = "Password is required") String password) {
}
