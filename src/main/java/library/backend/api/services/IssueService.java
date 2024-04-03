package library.backend.api.services;

import library.backend.api.models.Book;
import library.backend.api.models.Issue;
import library.backend.api.models.User;
import library.backend.api.repositories.IssueRepository;
import library.backend.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@Service
public class IssueService {
    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private UserRepository userRepository;

    Issue issueBook(Book book, Long id){
        User user = userRepository.findById(id).orElse(null);
        Issue issue = Issue.builder()
                .issueDate(LocalDate.now())
                .issuePeriod(14)
                .book(book)
                .user(user)
                .build();
        issueRepository.save(issue);
        return issue;
    }

    List<Issue> getAllIssues(){
        return issueRepository.findAll();
    }
}
