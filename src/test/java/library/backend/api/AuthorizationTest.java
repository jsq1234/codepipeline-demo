package library.backend.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import library.backend.api.dto.AuthResponseDto;
import library.backend.api.models.Book;
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

import java.util.HashMap;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthorizationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void authorizedAccessToBookCreationForAdminsTest() throws Exception {

        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setRole("ROLE_ADMIN");
        mockUser.setPassword(passwordEncoder.encode("password"));

        when(userRepository.findByEmail(any())).thenReturn(java.util.Optional.of(mockUser));

        String jwtToken = getJwtToken("test@example.com", "password");

        Book book = new Book();
        book.setTitle("Book1");
        book.setPrice(100.45f);

        mockMvc.perform(MockMvcRequestBuilders.post("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .header("AUTHORIZATION", "Bearer " + jwtToken)
                .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isOk());
    }

    @Test
    void unauthorizedAccessToBookCreationForUserForbidden() throws Exception {
        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setRole("ROLE_USER");
        mockUser.setPassword(passwordEncoder.encode("password"));

        when(userRepository.findByEmail(any())).thenReturn(java.util.Optional.of(mockUser));

        String jwtToken = getJwtToken("test@example.com", "password");

        Book book = new Book();
        book.setTitle("Book1");
        book.setPrice(100.45f);

        mockMvc.perform(MockMvcRequestBuilders.post("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .header("AUTHORIZATION", "Bearer " + jwtToken)
                .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isForbidden());
    }

    private String getJwtToken(String email, String password) throws Exception {
        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("email", email);
        requestBody.put("password", password);

        var request = MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody));

        String jsonResult = mockMvc.perform(request)
                .andReturn()
                .getResponse()
                .getContentAsString();

        AuthResponseDto dto = objectMapper.readValue(jsonResult, AuthResponseDto.class);

        return dto.getToken();
    }

}
