package com.sidramesh.restapidemo.services;

import com.sidramesh.restapidemo.Entities.Book;
import com.sidramesh.restapidemo.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final BookRepository repository;

    public BookService(BookRepository bookRepository) {
        this.repository = bookRepository;
    }

    public List<Book> findAll(){
       return this.repository.findAll();
    }

    public Book findById(long id){
        return this.repository.findById(id).orElse(null);
    }

    public Book save(Book book){
        return this.repository.save(book);
    }

    public void delete(long id){
        this.repository.deleteById(id);
    }

}
