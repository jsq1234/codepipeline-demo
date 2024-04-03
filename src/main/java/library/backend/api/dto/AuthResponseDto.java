package library.backend.api.dto;

import library.backend.api.utils.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Data
public class AuthResponseDto {
    private final String email;
    private final String phoneNo;
    private final String token;
    private final String name;
    private final String role;
    private final Status status;
}
