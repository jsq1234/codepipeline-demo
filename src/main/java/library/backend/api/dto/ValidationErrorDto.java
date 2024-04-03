package library.backend.api.dto;

import library.backend.api.utils.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;

import java.util.HashMap;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ValidationErrorDto {
    private HttpStatus status;
    private HashMap<String, String> errors;
}
