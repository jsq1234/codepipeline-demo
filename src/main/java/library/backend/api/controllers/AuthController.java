package library.backend.api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import library.backend.api.dto.AuthResponseDto;
import library.backend.api.dto.LoginRequestDto;
import library.backend.api.dto.SignUpRequestDto;
import library.backend.api.services.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        AuthResponseDto response = authService.login(loginRequestDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDto> signUp(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        AuthResponseDto response = authService.signUp(signUpRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

}
