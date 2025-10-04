package com.sidramesh.restapidemo.repository;


import com.sidramesh.restapidemo.Entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
