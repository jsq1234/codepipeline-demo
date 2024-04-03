package library.backend.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import library.backend.api.dto.SignUpRequestDto;
import library.backend.api.exceptions.UserAlreadyExistsException;
import library.backend.api.models.User;
import library.backend.api.repositories.UserRepository;
import library.backend.api.services.AuthService;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("SignUpWithExistingUserThrowsException: Signing up with using existing credentials throws error.")
    public void SignUpWithExistingUserThrowsException() {
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto("manan", "test@example.com", "8178610509", "password");
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);
        assertThrows(UserAlreadyExistsException.class, () -> authService.signUp(signUpRequestDto));
    }

    @Test
    @DisplayName("SignUp_SucessfullySavesUser: Signing Up sucessfully saves user to the database")
    public void SignUp_SucessfullySavesUser() {
        SignUpRequestDto dto = new SignUpRequestDto("manan", "test@example.com", "8178610509", "password");
        when(userRepository.existsByEmail(dto.email())).thenReturn(false);
        when(passwordEncoder.encode(dto.password())).thenReturn("encodedPassword");

        authService.signUp(dto);

        User user = User.builder()
                .email(dto.email())
                .password("encodedPassword")
                .role("USER")
                .phoneNo(dto.phoneNo())
                .name(dto.name())
                .build();

        verify(userRepository).save(user);
    }
}
