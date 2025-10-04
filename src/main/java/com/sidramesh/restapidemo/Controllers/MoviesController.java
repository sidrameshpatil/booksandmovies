package com.sidramesh.restapidemo.Controllers;

import com.sidramesh.restapidemo.Entities.Book;
import com.sidramesh.restapidemo.Entities.Movie;
import com.sidramesh.restapidemo.repository.MovieRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@CrossOrigin(origins = "http://localhost:5173")
public class MoviesController {

    private final MovieRepository repository;

    @GetMapping()
    public List<Movie> getAllMovies(){
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Movie getMovieById(@PathVariable Long id){
        return repository.findById(id).orElse(null);
    }

    @PostMapping()
    public Movie createMovie(@RequestBody Movie movie){
        return repository.save(movie);
    }

    @DeleteMapping("/{id}")
    public void deleteMovieById(@PathVariable Long id){
        repository.deleteById(id);
    }

    public MoviesController(MovieRepository repository){
        this.repository = repository;
    }

}
