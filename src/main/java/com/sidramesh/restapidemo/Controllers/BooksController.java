package com.sidramesh.restapidemo.Controllers;

import com.sidramesh.restapidemo.Entities.Book;
import com.sidramesh.restapidemo.services.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "http://localhost:5173")
public class BooksController {
    private final BookService service;

    @GetMapping
    public List<Book> getAllBooks(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Book getBook(@PathVariable Long id){
        return this.service.findById(id);
    }

    @PostMapping
    public Book createBook(@RequestBody Book book){
        return service.save(book);
    }

    public BooksController(BookService bookService){
        this.service = bookService;
    }


}
