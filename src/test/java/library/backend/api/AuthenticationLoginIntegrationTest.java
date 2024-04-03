package library.backend.api;

import com.fasterxml.jackson.databind.ObjectMapper;

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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.HashMap;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationLoginIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void LoginTestWithEmailReturnsJwtToken() throws Exception {
        // Create a mock user
        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setRole("USER");
        mockUser.setPassword(passwordEncoder.encode("password"));

        // Mock the behavior of UserRepository to return the mock user
        when(userRepository.findByEmail(any())).thenReturn(java.util.Optional.of(mockUser));

        // Login with the mock user
        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("email", "test@example.com");
        requestBody.put("password", "password");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists());

    }

    @Test
    public void LoginTestWithPhoneNoReturnsJwtToken() throws Exception {
        // Create a mock user
        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setPhoneNo("8178610509");
        mockUser.setRole("USER");
        mockUser.setPassword(passwordEncoder.encode("password"));

        // Mock the behavior of UserRepository to return the mock user
        when(userRepository.findByPhoneNo(any())).thenReturn(java.util.Optional.of(mockUser));

        // Login with the mock user
        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("phoneNo", "8178610509");
        requestBody.put("password", "password");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists());

    }

    @Test
    void LoginTestWithoutEmailAndPasswordReturnsException() throws Exception {
        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("password", "1234");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error").value("Incorrect login message format"));
    }

    @Test
    void IncorrectLoginReturnsUnauthorizedTest() throws Exception {
        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("phoneNo", "1234");
        requestBody.put("password", "12345");

        // Perform the request and capture the result
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error").value("Bad credentials"));
    }

}
