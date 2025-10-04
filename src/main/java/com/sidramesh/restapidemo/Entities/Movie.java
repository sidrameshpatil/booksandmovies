package com.sidramesh.restapidemo.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "movies")
public class Movie {
    public Movie(Long id, String name, String director, Integer year, String language) {
        Id = id;
        Name = name;
        Director = director;
        Year = year;
        Language = language;
    }

    public Movie(){

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String Name;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDirector() {
        return Director;
    }

    public void setDirector(String director) {
        Director = director;
    }

    public Integer getYear() {
        return Year;
    }

    public void setYear(Integer year) {
        Year = year;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    private String Director;
    private Integer Year;
    private String Language;
}
