package com.sidramesh.restapidemo.Entities;

import jakarta.persistence.*;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String author;
    private String isbn;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId; // internal user id from users.id

    public Book(long id, String name, String author, String isbn) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.isbn = isbn;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getAuthor() {
        return author;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getIsbn() {
        return isbn;
    }

    public Book(){}
}
