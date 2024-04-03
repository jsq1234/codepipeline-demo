package library.backend.api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "issues")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Issue date cannot be null")
    @PastOrPresent(message = "Issue date must be in the past or present")
    private LocalDate issueDate;

    @PastOrPresent(message = "Return date must be in the past or present")
    private LocalDate returnDate;

    @NotNull(message = "Issue period cannot be null")
    @Positive(message = "issuePeriod cannot be negative")
    private Integer issuePeriod;

    @NotNull(message = "User cannot be null")
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    // @NotNull(message = "Book cannot be null")
    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id", nullable = true)
    private Book book;


}
