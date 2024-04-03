package library.backend.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import library.backend.api.models.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

}
