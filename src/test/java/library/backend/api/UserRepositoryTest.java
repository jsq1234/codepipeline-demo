package library.backend.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import library.backend.api.models.Issue;
import library.backend.api.models.User;
import library.backend.api.repositories.IssueRepository;
import library.backend.api.repositories.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDate;
import java.util.List;

@DataJpaTest
@ActiveProfiles(profiles = { "test" })
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        User user1 = User.builder()
                .email("test@example.com")
                .password("1234")
                .phoneNo("8178610509")
                .name("test")
                .role("USER")
                .build();

        User user2 = User.builder()
                .email("test2@example.com")
                .password("1234")
                .phoneNo("8178610509")
                .name("test2")
                .role("ADMIN")
                .build();

        Issue issue1 = Issue.builder()
                .issueDate(LocalDate.of(2024, 2, 2))
                .issuePeriod(14)
                .user(user1)
                .build();

        Issue issue2 = Issue.builder()
                .issueDate(LocalDate.of(2024, 2, 2))
                .issuePeriod(14)
                .user(user1)
                .build();

        Issue issue3 = Issue.builder()
                .issueDate(LocalDate.of(2024, 1, 2))
                .issuePeriod(14)
                .user(user1)
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        issueRepository.save(issue1);
        issueRepository.save(issue2);
        issueRepository.save(issue3);

        entityManager.clear();

    }

    @Test
    void insertingSameUserThrowsException() throws Exception {
        User user1 = User.builder()
                .email("test@example.com")
                .password("1234")
                .phoneNo("8178610509")
                .name("test")
                .role("USER")
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(user1));
    }

    @Test
    void fetchingUserIssues_GivesCorrectNumberOfIssues() throws Exception {

        User user = userRepository.findByEmail("test@example.com")
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        assertNotNull(user, () -> "Returned user should not be null");

        List<Issue> userIssues = user.getIssues();

        assertNotNull(userIssues, () -> "Returned user must have non-zero issues."); // Ensure that issues are

        assertEquals(3, user.getIssues().size());
    }

    @Test
    void fetchingIssuesReturnsAssociatedUser() throws Exception {
        Issue issue = issueRepository.findById(7L).orElse(null);
        assertNotNull(issue, () -> "Returned Issue must not be null.");
        assertNotNull(issue.getUser(), () -> "User associated with returned User must not be null");
    }
}
