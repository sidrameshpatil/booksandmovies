package com.sidramesh.restapidemo.Controllers;

import com.sidramesh.restapidemo.Entities.Book;
import com.sidramesh.restapidemo.services.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BooksController {
    private final BookService service;

    public BooksController(BookService bookService){
        this.service = bookService;
    }

    // Returns books belonging to the authenticated user
    @GetMapping
    public ResponseEntity<?> getMyBooks(Authentication auth){
        if (auth == null || auth.getPrincipal() == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }
        Long userId = Long.valueOf(String.valueOf(auth.getPrincipal()));
        List<Book> books = service.findByOwnerId(userId);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBook(@PathVariable Long id, Authentication auth){
        if (auth == null || auth.getPrincipal() == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }
        Long userId = Long.valueOf(String.valueOf(auth.getPrincipal()));
        Book book = this.service.findByIdAndOwnerId(id, userId);
        if (book == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(book);
    }

    @PostMapping
    public ResponseEntity<?> createBook(@RequestBody Book book, Authentication auth){
        if (auth == null || auth.getPrincipal() == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }
        Long userId = Long.valueOf(String.valueOf(auth.getPrincipal()));
        Book saved = service.createForUser(userId, book);
        return ResponseEntity.ok(saved);
    }
}