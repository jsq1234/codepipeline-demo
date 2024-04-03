package library.backend.api.dto;

import org.springframework.http.HttpStatus;

public record ErrorDto(HttpStatus status, String error) {
}
