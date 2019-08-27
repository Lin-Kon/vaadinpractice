package com.example.vaadinpractice.service;

import com.example.vaadinpractice.model.Movie;
import com.example.vaadinpractice.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MovieService {
//    private MovieRepository movieRepository;
    @Value("${baseUrl}/movies")
    private String baseUrl;

//    public MovieService(MovieRepository movieRepository){
//        this.movieRepository = movieRepository;
//        System.out.println("Base URL: "+baseUrl);
//    }

    public List<Movie> findAll() {
        System.out.println("find Base URL: "+ baseUrl);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<Movie>> response= restTemplate.exchange(
                baseUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Movie>>(){});
        List<Movie> movieList = response.getBody();
        return movieList;
    }

    public Movie save(Movie movie) {
        RestTemplate restTemplate = new RestTemplate();
        Movie savedMovie = restTemplate.postForObject(baseUrl, movie, Movie.class);
        return savedMovie;
    }

    public Movie update(Movie movie) {
        return movie;
    }

    public String delete(Movie movie) {
        Map<String, String> params = new HashMap();
        params.put("id","2");

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(baseUrl + "/" + movie.getId(), params);
        return null;
    }
}
