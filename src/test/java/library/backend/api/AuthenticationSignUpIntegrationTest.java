package library.backend.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import library.backend.api.dto.SignUpRequestDto;
import library.backend.api.models.User;
import library.backend.api.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.HashMap;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationSignUpIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void UserAlreadyExistsTest() throws Exception {
        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setRole("USER");
        mockUser.setName("test");
        mockUser.setPhoneNo("8178610509");
        mockUser.setPassword(passwordEncoder.encode("password"));

        when(userRepository.existsByEmail(any())).thenReturn(true);

        SignUpRequestDto dto = new SignUpRequestDto("test", "test@example.com", "8178610509", "password");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error").value("User already exists"));
    }

    @Test
    void SignUp_ReturnsJwtToken() throws Exception {
        when(userRepository.existsByEmail(any())).thenReturn(false);

        SignUpRequestDto dto = new SignUpRequestDto("manan", "text@example.com", "8178610509", "password");

        var request = MockMvcRequestBuilders.post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.status").value("REGISTER_SUCCESS"));

    }
}
