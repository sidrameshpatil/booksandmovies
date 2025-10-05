package com.sidramesh.restapidemo.repository;

import com.sidramesh.restapidemo.Entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByOwnerId(Long ownerId);
    Optional<Book> findByIdAndOwnerId(Long id, Long ownerId);
}