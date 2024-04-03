package library.backend.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import library.backend.api.models.Issue;

public interface IssueRepository extends JpaRepository<Issue, Long> {

}
