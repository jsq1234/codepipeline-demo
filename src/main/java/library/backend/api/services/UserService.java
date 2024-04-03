package library.backend.api.services;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import library.backend.api.dto.SignUpRequestDto;
import library.backend.api.models.User;
import library.backend.api.models.Issue;
import library.backend.api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(SignUpRequestDto signUpRequestDto, String role) {
        var encodedPassword = passwordEncoder.encode(signUpRequestDto.password());

        var user = User.builder()
                .email(signUpRequestDto.email())
                .password(encodedPassword)
                .phoneNo(signUpRequestDto.phoneNo())
                .name(signUpRequestDto.name())
                .role(role)
                .build();

        userRepository.save(user);

        return user;
    }

    public List<Issue> getUserIssues(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User doesn't exists."));
        return user.getIssues();
    }
}
