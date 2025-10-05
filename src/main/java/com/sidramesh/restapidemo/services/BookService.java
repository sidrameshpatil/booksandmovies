package com.sidramesh.restapidemo.services;

import com.sidramesh.restapidemo.Entities.Book;
import com.sidramesh.restapidemo.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final BookRepository repo;

    public BookService(BookRepository repo) { this.repo = repo; }

    public List<Book> findByOwnerId(Long ownerId) {
        return repo.findByOwnerId(ownerId);
    }

    public Book findByIdAndOwnerId(Long id, Long ownerId) {
        return repo.findByIdAndOwnerId(id, ownerId).orElse(null);
    }

    public Book createForUser(Long ownerId, Book book) {
        book.setOwnerId(ownerId);
        return repo.save(book);
    }

    // keep any existing generic methods if you want, but controller should prefer owner-scoped methods
}