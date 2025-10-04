package com.sidramesh.restapidemo.repository;

import com.sidramesh.restapidemo.Entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
